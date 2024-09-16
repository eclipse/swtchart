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
package org.eclipse.swtchart.vectorgraphics2d.core;

import org.eclipse.swtchart.vectorgraphics2d.eps.EPSTests;
import org.eclipse.swtchart.vectorgraphics2d.intermediate.IRTests;
import org.eclipse.swtchart.vectorgraphics2d.pdf.PDFTests;
import org.eclipse.swtchart.vectorgraphics2d.svg.SVGTests;
import org.eclipse.swtchart.vectorgraphics2d.util.UtilTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestUtilsTest.class, UtilTests.class, IRTests.class, VectorGraphics2DTest.class, ProcessorsTest.class, EPSTests.class, PDFTests.class, SVGTests.class})
public class AllTests {
}
