/*******************************************************************************
 * Copyright (c) 2010, 2019 VectorGraphics2D project.
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
 *******************************************************************************/
package org.eclipse.swtchart.vectorgraphics2d.visual;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

// import org.apache.batik.transcoder.TranscoderException;
// import org.apache.batik.transcoder.TranscoderInput;
// import org.apache.batik.transcoder.TranscoderOutput;
import org.eclipse.swtchart.vectorgraphics2d.core.Document;
import org.eclipse.swtchart.vectorgraphics2d.core.Processor;
import org.eclipse.swtchart.vectorgraphics2d.core.VectorGraphics2D;
import org.eclipse.swtchart.vectorgraphics2d.eps.EPSProcessor;
import org.eclipse.swtchart.vectorgraphics2d.pdf.PDFProcessor;
import org.eclipse.swtchart.vectorgraphics2d.svg.SVGProcessor;
import org.eclipse.swtchart.vectorgraphics2d.util.PageSize;
// import org.ghost4j.Ghostscript;
// import org.ghost4j.GhostscriptException;

public abstract class TestCase {

	@SuppressWarnings("unused")
	private static final double EPSILON = 1;
	private final PageSize pageSize;
	private final BufferedImage reference;
	private final VectorGraphics2D vectorGraphics;
	private final Processor epsProcessor;
	private final Processor pdfProcessor;
	private final Processor svgProcessor;
	private BufferedImage rasterizedEPS;
	private BufferedImage rasterizedPDF;
	private BufferedImage rasterizedSVG;

	public TestCase() throws IOException {

		int width = 300;
		int height = 300;
		pageSize = new PageSize(0.0, 0.0, width, height);
		vectorGraphics = new VectorGraphics2D();
		draw(vectorGraphics);
		epsProcessor = new EPSProcessor();
		pdfProcessor = new PDFProcessor(false);
		svgProcessor = new SVGProcessor();
		reference = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D referenceGraphics = reference.createGraphics();
		referenceGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		referenceGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		referenceGraphics.setBackground(new Color(1f, 1f, 1f, 0f));
		referenceGraphics.clearRect(0, 0, reference.getWidth(), reference.getHeight());
		referenceGraphics.setColor(Color.BLACK);
		draw(referenceGraphics);
		File referenceImage = File.createTempFile(getClass().getName() + ".reference", ".png");
		referenceImage.deleteOnExit();
		ImageIO.write(reference, "png", referenceImage);
	}

	public abstract void draw(Graphics2D g);

	public PageSize getPageSize() {

		return pageSize;
	}

	public BufferedImage getReference() {

		return reference;
	}

	public InputStream getEPS() {

		Document document = epsProcessor.getDocument(vectorGraphics.getCommands(), getPageSize());
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		try {
			document.writeTo(byteOutput);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(byteOutput.toByteArray());
	}

	public InputStream getPDF() {

		Document document = pdfProcessor.getDocument(vectorGraphics.getCommands(), getPageSize());
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		try {
			document.writeTo(byteOutput);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(byteOutput.toByteArray());
	}

	public InputStream getSVG() {

		Document document = svgProcessor.getDocument(vectorGraphics.getCommands(), getPageSize());
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		try {
			document.writeTo(byteOutput);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(byteOutput.toByteArray());
	}

	public BufferedImage getRasterizedEPS() {

		if(rasterizedEPS != null) {
			return rasterizedEPS;
		}
		// try {
		// File epsInputFile = File.createTempFile(getClass().getName() + ".testEPS", ".eps");
		// epsInputFile.deleteOnExit();
		// OutputStream epsInput = new FileOutputStream(epsInputFile);
		// epsProcessor.getDocument(vectorGraphics.getCommands(), getPageSize()).writeTo(epsInput);
		// epsInput.close();
		// File pngOutputFile = File.createTempFile(getClass().getName() + ".testEPS", "png");
		// pngOutputFile.deleteOnExit();
		// Ghostscript gs = Ghostscript.getInstance();
		// gs.initialize(new String[]{"-dBATCH", "-dQUIET", "-dNOPAUSE", "-dSAFER", String.format("-g%dx%d", Math.round(getPageSize().getWidth()), Math.round(getPageSize().getHeight())), "-dGraphicsAlphaBits=4", "-dAlignToPixels=0", "-dEPSCrop", "-dPSFitPage", "-sDEVICE=pngalpha", "-sOutputFile=" + pngOutputFile.toString(), epsInputFile.toString()});
		// gs.exit();
		// rasterizedEPS = ImageIO.read(pngOutputFile);
		// } catch(IOException | GhostscriptException e) {
		// e.printStackTrace();
		// }
		return rasterizedEPS;
	}

	public BufferedImage getRasterizedPDF() {

		if(rasterizedPDF != null) {
			return rasterizedPDF;
		}
		// try {
		// File pdfInputFile = File.createTempFile(getClass().getName() + ".testPDF", ".pdf");
		// pdfInputFile.deleteOnExit();
		// OutputStream pdfInput = new FileOutputStream(pdfInputFile);
		// pdfProcessor.getDocument(vectorGraphics.getCommands(), getPageSize()).writeTo(pdfInput);
		// pdfInput.close();
		// File pngOutputFile = File.createTempFile(getClass().getName() + ".testPDF", "png");
		// pngOutputFile.deleteOnExit();
		// Ghostscript gs = Ghostscript.getInstance();
		// gs.initialize(new String[]{"-dBATCH", "-dQUIET", "-dNOPAUSE", "-dSAFER", String.format("-g%dx%d", Math.round(getPageSize().getWidth()), Math.round(getPageSize().getHeight())), "-dGraphicsAlphaBits=4",
		// // TODO: More robust settings for gs? DPI value is estimated.
		// "-r25", "-dAlignToPixels=0", "-dPDFFitPage", "-sDEVICE=pngalpha", "-sOutputFile=" + pngOutputFile.toString(), pdfInputFile.toString()});
		// gs.exit();
		// rasterizedPDF = ImageIO.read(pngOutputFile);
		// } catch(IOException | GhostscriptException e) {
		// e.printStackTrace();
		// }
		return rasterizedPDF;
	}

	public BufferedImage getRasterizedSVG() {

		if(rasterizedSVG != null) {
			return rasterizedSVG;
		}
		// try {
		// rasterizedSVG = new BufferedImage((int)Math.round(getPageSize().getWidth()), (int)Math.round(getPageSize().getHeight()), BufferedImage.TYPE_INT_ARGB);
		// ImageTranscoder transcoder = new ImageTranscoder() {
		//
		// @Override
		// public BufferedImage createImage(int width, int height) {
		//
		// return rasterizedSVG;
		// }
		//
		// @Override
		// public void writeImage(BufferedImage bufferedImage, TranscoderOutput transcoderOutput) throws TranscoderException {
		//
		// }
		// };
		// transcoder.transcode(new TranscoderInput(getSVG()), null);
		// } catch(TranscoderException e) {
		// e.printStackTrace();
		// }
		return rasterizedSVG;
	}
}
