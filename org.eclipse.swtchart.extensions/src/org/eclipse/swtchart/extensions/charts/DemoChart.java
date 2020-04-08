package org.eclipse.swtchart.extensions.charts;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.extensions.examples.parts.LineSeries_1_Part;

public class DemoChart {

	public static void main(String args[]) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("DemoChart");
		shell.setSize(500, 400);
		shell.setLayout(new FillLayout());
		LineSeries_1_Part chart = new LineSeries_1_Part(shell);
		shell.open();
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
