/*******************************************************************************
 * Copyright (c) 2019 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Sanatt Abrol - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.export.svg;

import java.io.Writer;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.converter.ChartToGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class SVGFactory {

	SVGGraphics2D svgGraphics2D;

	public SVGFactory() {
		
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		String svgNS = "http://www.w3.org/2000/svg";
		Document document = domImpl.createDocument(svgNS, "svg", null);
		this.svgGraphics2D = new SVGGraphics2D(document);
	}

	public void createSvg(Chart chart, int indexXAxis, int indexYAxis) {
		
		svgGraphics2D = (SVGGraphics2D) new ChartToGraphics2D(chart, indexXAxis, indexYAxis, svgGraphics2D)
				.getGraphics2D();
	}

	public boolean stream(Writer output, boolean useCss) {
		try {
			svgGraphics2D.stream(output, useCss);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public Document getDOM() {
		return svgGraphics2D.getDOMFactory();
	}
	

}
