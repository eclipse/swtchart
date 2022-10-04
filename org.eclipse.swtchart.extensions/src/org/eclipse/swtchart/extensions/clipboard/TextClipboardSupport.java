/*******************************************************************************
 * Copyright (c) 2022 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.clipboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeriesSet;
import org.eclipse.swtchart.extensions.core.BaseChart;

public class TextClipboardSupport {

	private static final String VALUE_DELIMITER = "\t";
	private static final String LINE_DELIMITER = "\r\n";

	public static void transfer(Display display, BaseChart baseChart) {

		List<String> seriesIds = new ArrayList<>(baseChart.getSeriesIds());
		Collections.sort(seriesIds);
		ISeriesSet seriesSet = baseChart.getSeriesSet();
		//
		StringBuilder builder = new StringBuilder();
		for(String seriesId : seriesIds) {
			/*
			 * ID
			 */
			ISeries<?> series = seriesSet.getSeries(seriesId);
			builder.append("#");
			builder.append(seriesId);
			builder.append(LINE_DELIMITER);
			/*
			 * Data
			 */
			double[] x = series.getXSeries();
			double[] y = series.getYSeries();
			if(x.length == y.length) {
				for(int i = 0; i < x.length; i++) {
					builder.append(x[i] + VALUE_DELIMITER + y[i]);
					builder.append(LINE_DELIMITER);
				}
			}
		}
		//
		TextTransfer textTransfer = TextTransfer.getInstance();
		Object[] data = new Object[]{builder.toString()};
		Transfer[] dataTypes = new Transfer[]{textTransfer};
		Clipboard clipboard = new Clipboard(display);
		try {
			clipboard.setContents(data, dataTypes);
		} finally {
			if(clipboard != null && !clipboard.isDisposed()) {
				clipboard.dispose();
			}
		}
	}
}