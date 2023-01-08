/*******************************************************************************
 * Copyright (c) 2019, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Sanatt Abrol - initial API and implementation
 * Philip Wenig - series mapping
 *******************************************************************************/
package org.eclipse.swtchart.export.extended.svg;

import java.io.Writer;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.export.extended.awt.ChartToGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class SVGFactory {

	private SVGGraphics2D svgGraphics2D;

	public SVGFactory() {

		DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = "http://www.w3.org/2000/svg"; //$NON-NLS-1$
		Document document = domImpl.createDocument(svgNS, "svg", null); //$NON-NLS-1$
		this.svgGraphics2D = new SVGGraphics2D(document);
	}

	public void createSvg(Chart chart, int indexXAxis, int indexYAxis) {

		svgGraphics2D = (SVGGraphics2D)new ChartToGraphics2D(chart, indexXAxis, indexYAxis, svgGraphics2D).getGraphics2D();
	}

	public boolean stream(Writer output, boolean useCss) {

		try {
			svgGraphics2D.stream(output, useCss);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	public Document getDOM() {

		return svgGraphics2D.getDOMFactory();
	}
}