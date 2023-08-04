/*******************************************************************************
 * Copyright (c) 2022, 2023 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtchart.extensions.clipboard.ImageClipboardSupport;
import org.eclipse.swtchart.extensions.clipboard.TextClipboardSupport;
import org.eclipse.swtchart.extensions.core.BaseChart;
import org.eclipse.swtchart.extensions.core.IKeyboardSupport;

public class CopyToClipboardEvent extends AbstractHandledEventProcessor implements IHandledEventProcessor {

	private int copyTextMask = SWT.MOD2;
	private int copyVectorMask = SWT.MOD3;

	@Override
	public int getEvent() {

		return IKeyboardSupport.EVENT_KEY_UP;
	}

	@Override
	public int getButton() {

		return IKeyboardSupport.KEY_CODE_LC_C;
	}

	@Override
	public int getStateMask() {

		return SWT.MOD1;
	}

	@Override
	public void handleEvent(BaseChart baseChart, Event event) {

		if((event.stateMask & copyTextMask) == copyTextMask) {
			TextClipboardSupport.transfer(event.display, baseChart);
		} else {
			if((event.stateMask & copyVectorMask) == copyVectorMask) {
				ImageClipboardSupport.transfer(event.display, baseChart, true);
			} else {
				ImageClipboardSupport.transfer(event.display, baseChart, false);
			}
		}
	}
}