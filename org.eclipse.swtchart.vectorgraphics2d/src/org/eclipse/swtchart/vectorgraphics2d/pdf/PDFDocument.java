/*******************************************************************************
 * Copyright (c) 2010, 2024 VectorGraphics2D project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Erich Seifert - initial API and implementation
 * Michael Seifert - initial API and implementation
 * Philip Wenig - fixed PDF output
 *******************************************************************************/
package org.eclipse.swtchart.vectorgraphics2d.pdf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.eclipse.swtchart.vectorgraphics2d.core.SizedDocument;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.CommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.AffineTransformCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Command;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.DrawShapeCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.DrawStringCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.FillShapeCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetColorCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetFontCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetStrokeCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetTransformCommand;
import org.eclipse.swtchart.vectorgraphics2d.util.PageSize;

class PDFDocument extends SizedDocument {

	private static final float POINTS_PER_INCH = 72; // DPI
	private static final float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;
	//
	private PDDocument document = null;

	private class Scale {

		private double x = 1.0d;
		private double y = 1.0d;

		public Scale(double x, double y) {

			this.x = x;
			this.y = y;
		}

		public double getX() {

			return x;
		}

		public double getY() {

			return y;
		}
	}

	public PDFDocument(CommandSequence commands, PageSize pageSize, boolean compressed) throws IOException {

		super(pageSize, compressed);
		document = createDocument(commands, pageSize);
	}

	@Override
	public void writeTo(OutputStream outputStream) throws IOException {

		if(document != null) {
			document.save(outputStream);
			document.close();
			document = null;
		}
	}

	private PDDocument createDocument(CommandSequence commands, PageSize pageSize) {

		Scale scale = getScale(pageSize, commands);
		PDDocument document = new PDDocument();
		PDRectangle rectangle = getRectangle(pageSize, scale);
		PDPage page = new PDPage(rectangle);
		document.addPage(page);
		float height = rectangle.getHeight();
		//
		Matrix matrixScale = Matrix.getScaleInstance(1, -1);
		Matrix matrixTranslate = Matrix.getTranslateInstance(0, -(float)height);
		Matrix matrixFlip = Matrix.concatenate(matrixScale, matrixTranslate);
		//
		AffineTransform affineTransform = null;
		boolean useAffineTransform = false;
		Color colorBackground = Color.WHITE;
		Color colorForeground = Color.BLACK;
		PDFont font = PDType1Font.HELVETICA;
		float fontSize = 8.0f;
		Map<Integer, PDExtendedGraphicsState> opacityMap = new HashMap<>();
		//
		try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
			for(Command<?> command : commands) {
				if(command instanceof AffineTransformCommand c) {
					/*
					 * Master Affine Transformation
					 */
					affineTransform = c.getValue();
				} else if(command instanceof SetColorCommand c) {
					/*
					 * Color
					 */
					colorBackground = c.getValue();
					colorForeground = c.getValue();
					int alpha = c.getValue().getAlpha();
					PDExtendedGraphicsState graphicsState = opacityMap.get(alpha);
					if(graphicsState == null) {
						graphicsState = new PDExtendedGraphicsState();
						graphicsState.setNonStrokingAlphaConstant((1.0f / 255) * alpha);
						opacityMap.putIfAbsent(alpha, graphicsState);
					}
					contentStream.setGraphicsStateParameters(graphicsState);
				} else if(command instanceof SetStrokeCommand c) {
					/*
					 * Stroke
					 */
					Stroke stroke = c.getValue();
					if(stroke instanceof BasicStroke basicStroke) {
						contentStream.setLineCapStyle(basicStroke.getEndCap());
						if(basicStroke.getDashArray() != null) {
							float[] dashArray = basicStroke.getDashArray();
							float[] dashArrayAdjusted = new float[dashArray.length];
							for(int i = 0; i < dashArray.length; i++) {
								dashArrayAdjusted[i] = (float)(dashArray[i]);
							}
							contentStream.setLineDashPattern(dashArrayAdjusted, (float)(basicStroke.getDashPhase()));
						}
						contentStream.setLineJoinStyle(basicStroke.getLineJoin());
						contentStream.setLineWidth((float)(basicStroke.getLineWidth()));
					}
				} else if(command instanceof SetFontCommand c) {
					/*
					 * Font
					 */
					Font fontx = c.getValue();
					fontSize = (float)fontx.getSize2D();
				} else if(command instanceof SetTransformCommand c) {
					/*
					 * Detect Rotation
					 * TODO: also map stretch, shear, ...
					 */
					affineTransform = c.getValue();
					double[] matrixFromAffineTransform = new double[6];
					affineTransform.getMatrix(matrixFromAffineTransform);
					if(matrixFromAffineTransform[0] == -0) {
						useAffineTransform = true;
					}
				} else if(command instanceof DrawShapeCommand || command instanceof FillShapeCommand) {
					/*
					 * Don't use the master affine transformation here, as full scale is used.
					 */
					boolean fillShape = command instanceof FillShapeCommand;
					Object object = command.getValue();
					if(object instanceof Shape shape) {
						if(shape instanceof Line2D || shape instanceof Path2D || shape instanceof Polygon || shape instanceof Rectangle) {
							contentStream.setNonStrokingColor(colorBackground);
							contentStream.setStrokingColor(colorForeground);
							PathIterator iterator = shape.getPathIterator(null);
							double[] coordsCur = new double[6];
							while(!iterator.isDone()) {
								int segmentType = iterator.currentSegment(coordsCur);
								switch(segmentType) {
									case PathIterator.SEG_MOVETO:
										Point2D.Float pointMoveTo = matrixFlip.transformPoint((float)coordsCur[0], (float)coordsCur[1]);
										contentStream.moveTo(pointMoveTo.x, pointMoveTo.y);
										break;
									case PathIterator.SEG_LINETO:
										Point2D.Float pointLineTo = matrixFlip.transformPoint((float)coordsCur[0], (float)coordsCur[1]);
										contentStream.lineTo(pointLineTo.x, pointLineTo.y);
										break;
									case PathIterator.SEG_CLOSE:
										contentStream.closePath();
										break;
									default:
										/*
										 * TODO
										 * PathIterator.SEG_CUBICTO
										 * PathIterator.SEG_QUADTO
										 */
										break;
								}
								iterator.next();
							}
							/*
							 * Fill or stroke
							 */
							if(fillShape) {
								contentStream.fill();
							} else {
								contentStream.stroke();
							}
						} else {
							/*
							 * TODO
							 * Ellipse2D
							 */
						}
					}
				} else if(command instanceof DrawStringCommand c) {
					/*
					 * Label
					 */
					String label = c.getValue();
					Point2D.Float point = new Point2D.Float((float)c.getX(), (float)c.getY());
					point = matrixFlip.transformPoint(point.x, point.y);
					contentStream.setFont(font, fontSize);
					contentStream.setNonStrokingColor(colorForeground);
					contentStream.beginText();
					if(useAffineTransform) {
						double rotationDegree = getRotationDegree(affineTransform);
						contentStream.setTextMatrix(Matrix.getRotateInstance(Math.toRadians(rotationDegree), point.x, point.y));
						useAffineTransform = false;
					} else {
						contentStream.newLineAtOffset(point.x, point.y);
					}
					contentStream.showText(label);
					contentStream.endText();
				} else {
					/*
					 * TODO
					 * The following commands need to be inspected and handled:
					 * SetClipCommand
					 * ScaleCommand
					 * SetHintCommand
					 * SetBackgroundCommand
					 * SetPaintCommand
					 * CreateCommand
					 * StateCommand
					 * CreateCommand
					 * Group
					 * DisposeCommand
					 */
				}
			}
			//
			contentStream.close();
			scaleToTargetSize(pageSize, document, page);
		} catch(IOException e) {
			// System.out.println(e);
		}
		//
		return document;
	}

	private double getRotationDegree(AffineTransform affineTransform) {

		double scaleX = affineTransform.getScaleX();
		double shearY = affineTransform.getShearY();
		double theta = Math.atan2(shearY, scaleX);
		//
		return Math.toDegrees(theta) - 180;
	}

	private void scaleToTargetSize(PageSize pageSize, PDDocument document, PDPage page) throws IOException {

		float width = (float)(pageSize.getWidth() * POINTS_PER_MM);
		float height = (float)(pageSize.getHeight() * POINTS_PER_MM);
		PDRectangle targetSize = new PDRectangle(width, height);
		PDRectangle originalSize = page.getMediaBox();
		float scaleX = targetSize.getWidth() / originalSize.getWidth();
		float scaleY = targetSize.getHeight() / originalSize.getHeight();
		PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, false);
		Matrix matrix = Matrix.getScaleInstance(scaleX, scaleY);
		contentStream.transform(matrix);
		contentStream.close();
		page.setMediaBox(targetSize);
	}

	/*
	 * Returns the original size.
	 */
	private PDRectangle getRectangle(PageSize pageSize, Scale scale) {

		float width = (float)(pageSize.getWidth() * scale.getX());
		float height = (float)(pageSize.getHeight() * scale.getY());
		//
		return new PDRectangle(width, height);
	}

	private Scale getScale(PageSize pageSize, CommandSequence commands) {

		double scaleX = 1.0d;
		double scaleY = 1.0d;
		//
		for(Command<?> command : commands) {
			if(command instanceof AffineTransformCommand affineTransformCommand) {
				AffineTransform affineTransform = affineTransformCommand.getValue();
				scaleX = affineTransform.getScaleX();
				scaleY = affineTransform.getScaleY();
			}
		}
		//
		double x = (scaleX != 0) ? (1 / scaleX) : 1.0d;
		double y = (scaleY != 0) ? (1 / scaleY) : 1.0d;
		//
		return new Scale(x, y);
	}
}