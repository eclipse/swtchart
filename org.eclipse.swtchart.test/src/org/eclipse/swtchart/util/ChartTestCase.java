/*******************************************************************************
 * Copyright (c) 2008, 2023 SWTChart project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * yoshitaka - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Chart;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

/**
 * Test case for chart title.
 */
public class ChartTestCase {

	/** the fixed chart size (expected values in the test rely on this size) */
	private static final Point fixedChartSize = new Point(400, 300);
	/** the duration in millisec to show chart */
	private static final long durationToShowChart = 100;
	/** the chart */
	protected Chart chart;
	private Shell shell;
	private int fileNameIndex;
	private boolean refreshChart = true;
	private boolean saveChart = false;
	@Rule
	public TestName name = new TestName();

	@Before
	public void setUp() {

		Locale.setDefault(Locale.ENGLISH);
		shell = createShell(name.getMethodName());
		chart = createChart(shell);
		if(!shell.isVisible()) {
			shell.open();
		}
		fileNameIndex = 0;
	}

	@After
	public void tearDown() {

		chart.dispose();
		shell.dispose();
	}

	/**
	 * Show the chart on window to check the appearance.
	 */
	protected void showChart() {

		if(refreshChart) {
			chart.redraw();
			long time = System.currentTimeMillis();
			while(!shell.isDisposed() && System.currentTimeMillis() - time < durationToShowChart) {
				Display.getDefault().readAndDispatch();
			}
			if(saveChart) {
				saveIntoFile(chart, fileNameIndex++);
			}
		}
	}

	/**
	 * Starts tracking SWT resource creation and disposal.
	 * <p>
	 * When instantiating a class that extends
	 * {@link org.eclipse.swt.graphics.Resource}, the instance is supposed to be disposed
	 * by explicitly calling {@link org.eclipse.swt.graphics.Resource#dispose()}.
	 * <p>
	 * To detect the missing disposal of SWT resources, this method starts tracking the
	 * SWT resource creation and disposal.
	 */
	protected void startTrackingSwtResources() throws Exception {

		// initialize the tracking data
		Field field = Device.class.getDeclaredField("trackingLock");
		field.setAccessible(true);
		field.set(Display.getDefault(), new Object());
		field = Device.class.getDeclaredField("errors");
		field.setAccessible(true);
		field.set(Display.getDefault(), new Error[127]);
		field = Device.class.getDeclaredField("objects");
		field.setAccessible(true);
		field.set(Display.getDefault(), new Object[127]);
		// start tracking
		field = Device.class.getDeclaredField("tracking");
		field.setAccessible(true);
		field.set(Display.getDefault(), Boolean.TRUE);
	}

	/**
	 * Gets the count of SWT resources that are created and remain after tracking is started.
	 * 
	 * @return the count of SWT resources
	 */
	protected int getSwtResourceCount() throws Exception {

		Field field = Device.class.getDeclaredField("objects");
		field.setAccessible(true);
		Object[] objects = (Object[])field.get(Display.getDefault());
		// count SWT resources
		int count = 0;
		for(Object object : objects) {
			if(object != null) {
				count++;
			}
		}
		return count;
	}

	private static Shell createShell(String title) {

		Display display = Display.getDefault();
		// sufficient window size to show chart with fixed size
		Point windowSize = new Point(500, 450);
		Shell shell = new Shell(display);
		shell.setSize(windowSize);
		shell.setLocation(0, 0);
		shell.setText(title);
		shell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		return shell;
	}

	private static Chart createChart(Shell shell) {

		Chart chart = new Chart(shell, SWT.NONE);
		shell.setLayout(new GridLayout());
		chart.setBackground(chart.getBackground());
		GridData data = new GridData();
		data.widthHint = fixedChartSize.x;
		data.heightHint = fixedChartSize.y;
		chart.setLayoutData(data);
		return chart;
	}

	/**
	 * Saves screenshots at img_actual directory in project. Typical usage is
	 * following.
	 * <ol>
	 * <li>execute tests.
	 * <li>copy the directory img_actual and its content to img_expected.
	 * <li>do some changes in code.
	 * <li>execute tests.
	 * <li>select both img_actual and img_expected in Package Explorer, and select
	 * the context menu Compare With > Each Other.
	 * <li>check differences.
	 * </ol>
	 * 
	 * @param chart
	 *            the chart
	 * @param index
	 *            the file index
	 */
	private static void saveIntoFile(Chart chart, int index) {

		StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[3];
		String[] elems = stackTrace.getClassName().split("\\.");
		String dir = System.getProperty("user.dir") + File.separator + "img_actual";
		new File(dir).mkdirs();
		String subDir = dir + File.separator + elems[elems.length - 1];
		new File(subDir).mkdirs();
		String filename = stackTrace.getMethodName() + "_" + index + ".png";
		String filenameWithPath = subDir + File.separator + filename;
		chart.save(filenameWithPath, SWT.IMAGE_PNG);
	}
}
