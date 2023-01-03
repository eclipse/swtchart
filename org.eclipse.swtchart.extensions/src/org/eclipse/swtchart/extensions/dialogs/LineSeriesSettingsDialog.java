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
package org.eclipse.swtchart.extensions.dialogs;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.Antialias;
import org.eclipse.swtchart.LineStyle;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;

public class LineSeriesSettingsDialog extends AbstractPointSeriesSettingsDialog<ILineSeriesSettings> {

	private AtomicReference<ComboViewer> lineStyleControl = new AtomicReference<>();
	private AtomicReference<Spinner> lineWidthControl = new AtomicReference<>();
	private AtomicReference<Text> lineColorControl = new AtomicReference<>();
	private AtomicReference<ComboViewer> antialiasControl = new AtomicReference<>();
	private AtomicReference<Button> enableAreaControl = new AtomicReference<>();
	private AtomicReference<Button> enableStackControl = new AtomicReference<>();
	private AtomicReference<Button> enableStepControl = new AtomicReference<>();

	public LineSeriesSettingsDialog(Shell parentShell, ILineSeriesSettings settings) {

		super(parentShell, settings);
	}

	@Override
	protected void createInputSection(Composite parent) {

		super.createInputSection(parent);
		createLineStyleSection(parent);
		createLineWidthSection(parent);
		createLineColorSection(parent);
		createAntialiasSection(parent);
		createEnableAreaSection(parent);
		createEnableStackSection(parent);
		createEnableStepSection(parent);
	}

	@Override
	protected void initialize() {

		super.initialize();
		ILineSeriesSettings settings = getSettings();
		if(settings != null) {
			lineStyleControl.get().setSelection(new StructuredSelection(settings.getLineStyle()));
			lineWidthControl.get().setSelection(settings.getLineWidth());
			lineColorControl.get().setBackground(settings.getLineColor());
			antialiasControl.get().setSelection(new StructuredSelection(settings.getAntialias()));
			enableAreaControl.get().setSelection(settings.isEnableArea());
			enableStackControl.get().setSelection(settings.isEnableStack());
			enableStepControl.get().setSelection(settings.isEnableStep());
		}
	}

	private void createLineStyleSection(Composite parent) {

		String title = "Line Style";
		createSectionLabel(parent, title);
		//
		ComboViewer comboViewer = createComboViewer(parent, title, LineStyle.values(), LineStyle.NONE, getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Object>() {

			@Override
			public void accept(Object object) {

				ILineSeriesSettings settings = getSettings();
				if(settings != null) {
					if(object instanceof LineStyle) {
						settings.setLineStyle((LineStyle)object);
					}
				}
			}
		});
		//
		lineStyleControl.set(comboViewer);
	}

	private void createLineWidthSection(Composite parent) {

		String title = "Line Width";
		createSectionLabel(parent, title);
		//
		Spinner spinner = createSpinner(parent, title, 1, 50, 1, getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Integer>() {

			@Override
			public void accept(Integer selection) {

				ILineSeriesSettings settings = getSettings();
				if(settings != null) {
					settings.setLineWidth(selection);
				}
			}
		});
		//
		lineWidthControl.set(spinner);
	}

	private void createLineColorSection(Composite parent) {

		String title = "Line Color";
		createSectionLabel(parent, title);
		//
		Text text = createColorChoser(parent, title, getGridData(GridData.FILL_HORIZONTAL, 1), new Consumer<Color>() {

			@Override
			public void accept(Color color) {

				ILineSeriesSettings settings = getSettings();
				if(settings != null) {
					settings.setLineColor(color);
				}
			}
		});
		//
		lineColorControl.set(text);
	}

	private void createAntialiasSection(Composite parent) {

		String title = "Antialias";
		createSectionLabel(parent, title);
		//
		ComboViewer comboViewer = createComboViewer(parent, title, Antialias.values(), Antialias.ON, getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Object>() {

			@Override
			public void accept(Object object) {

				ILineSeriesSettings settings = getSettings();
				if(settings != null) {
					if(object instanceof Antialias) {
						settings.setAntialias(((Antialias)object).value());
					}
				}
			}
		});
		//
		antialiasControl.set(comboViewer);
	}

	private void createEnableAreaSection(Composite parent) {

		createSectionLabel(parent, "");
		//
		Button button = createCheckBox(parent, "Enable Area", "Enable or disable to display the area.", getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Boolean>() {

			@Override
			public void accept(Boolean selection) {

				ILineSeriesSettings settings = getSettings();
				if(settings != null) {
					settings.setEnableArea(selection);
				}
			}
		});
		//
		enableAreaControl.set(button);
	}

	private void createEnableStackSection(Composite parent) {

		createSectionLabel(parent, "");
		//
		Button button = createCheckBox(parent, "Enable Stack", "Enable or disable the stack modus.", getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Boolean>() {

			@Override
			public void accept(Boolean selection) {

				ILineSeriesSettings settings = getSettings();
				if(settings != null) {
					settings.setEnableStack(selection);
				}
			}
		});
		//
		enableStackControl.set(button);
	}

	private void createEnableStepSection(Composite parent) {

		createSectionLabel(parent, "");
		//
		Button button = createCheckBox(parent, "Enable Step", "Enable or disable the step modus.", getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Boolean>() {

			@Override
			public void accept(Boolean selection) {

				ILineSeriesSettings settings = getSettings();
				if(settings != null) {
					settings.setEnableStep(selection);
				}
			}
		});
		//
		enableStepControl.set(button);
	}
}