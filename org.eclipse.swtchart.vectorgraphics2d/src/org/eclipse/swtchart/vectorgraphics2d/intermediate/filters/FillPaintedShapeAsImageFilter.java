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
package org.eclipse.swtchart.vectorgraphics2d.intermediate.filters;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import org.eclipse.swtchart.vectorgraphics2d.intermediate.CommandSequence;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.Command;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.DisposeCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.DrawImageCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.FillShapeCommand;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.commands.SetPaintCommand;

public class FillPaintedShapeAsImageFilter extends StreamingFilter {

	private SetPaintCommand lastSetPaintCommand;

	public FillPaintedShapeAsImageFilter(CommandSequence stream) {

		super(stream);
	}

	@Override
	public Command<?> next() {

		Command<?> nextCommand = super.next();
		if(nextCommand instanceof SetPaintCommand) {
			lastSetPaintCommand = (SetPaintCommand)nextCommand;
		} else if(nextCommand instanceof DisposeCommand) {
			lastSetPaintCommand = null;
		}
		return nextCommand;
	}

	private DrawImageCommand getDrawImageCommand(FillShapeCommand shapeCommand, SetPaintCommand paintCommand) {

		Shape shape = shapeCommand.getValue();
		Rectangle2D shapeBounds = shape.getBounds2D();
		double x = shapeBounds.getX();
		double y = shapeBounds.getY();
		double width = shapeBounds.getWidth();
		double height = shapeBounds.getHeight();
		int imageWidth = (int)Math.round(width);
		int imageHeight = (int)Math.round(height);
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D imageGraphics = (Graphics2D)image.getGraphics();
		imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		imageGraphics.scale(imageWidth / width, imageHeight / height);
		imageGraphics.translate(-shapeBounds.getX(), -shapeBounds.getY());
		imageGraphics.setPaint(paintCommand.getValue());
		imageGraphics.fill(shape);
		imageGraphics.dispose();
		return new DrawImageCommand(image, imageWidth, imageHeight, x, y, width, height);
	}

	@Override
	protected List<Command<?>> filter(Command<?> command) {

		if(lastSetPaintCommand != null && command instanceof FillShapeCommand) {
			FillShapeCommand fillShapeCommand = (FillShapeCommand)command;
			DrawImageCommand drawImageCommand = getDrawImageCommand(fillShapeCommand, lastSetPaintCommand);
			return Collections.singletonList(drawImageCommand);
		}
		return Collections.singletonList(command);
	}
}
