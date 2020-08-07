/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

import org.eclipse.swtchart.extensions.events.MouseDownEvent;
import org.eclipse.swtchart.extensions.events.MouseMoveSelectionEvent;
import org.eclipse.swtchart.extensions.events.MouseMoveShiftEvent;
import org.eclipse.swtchart.extensions.events.MouseUpEvent;

import junit.framework.TestCase;

public class ChartSettings_1_Test extends TestCase {

	private ChartSettings chartSettings = new ChartSettings();

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		chartSettings.clearHandledEventProcessors();
		chartSettings.addHandledEventProcessor(new MouseDownEvent());
		chartSettings.addHandledEventProcessor(new MouseMoveSelectionEvent());
		chartSettings.addHandledEventProcessor(new MouseUpEvent());
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertNotNull(chartSettings.getHandledEventProcessorByClass(MouseDownEvent.class));
	}

	public void test2() {

		assertNotNull(chartSettings.getHandledEventProcessorByClass(MouseMoveSelectionEvent.class));
	}

	public void test3() {

		assertNotNull(chartSettings.getHandledEventProcessorByClass(MouseUpEvent.class));
	}

	public void test4() {

		assertNull(chartSettings.getHandledEventProcessorByClass(MouseMoveShiftEvent.class));
	}
}
