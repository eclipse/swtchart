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
import org.eclipse.swtchart.IBarSeries.BarWidthStyle;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;

public class BarSeriesSettingsDialog extends AbstractSeriesSettingsDialog<IBarSeriesSettings> {

	private AtomicReference<Text> barColorControl = new AtomicReference<>();
	private AtomicReference<Spinner> barPaddingControl = new AtomicReference<>();
	private AtomicReference<Spinner> barWidthControl = new AtomicReference<>();
	private AtomicReference<Button> barOverlayControl = new AtomicReference<>();
	private AtomicReference<ComboViewer> barWidthStyleControl = new AtomicReference<>();
	private AtomicReference<Button> enableStackControl = new AtomicReference<>();

	public BarSeriesSettingsDialog(Shell parentShell, IBarSeriesSettings settings) {

		super(parentShell, settings);
	}

	@Override
	protected void createInputSection(Composite parent) {

		super.createInputSection(parent);
		createBarColorSection(parent);
		createBarOverlaySection(parent);
		createBarPaddingSection(parent);
		createBarWidthSection(parent);
		createBarWidthStyleSection(parent);
		createEnableStackSection(parent);
	}

	@Override
	protected void initialize() {

		super.initialize();
		IBarSeriesSettings settings = getSettings();
		if(settings != null) {
			barColorControl.get().setBackground(settings.getBarColor());
			barOverlayControl.get().setSelection(settings.isBarOverlay());
			barPaddingControl.get().setSelection(settings.getBarPadding());
			barWidthControl.get().setSelection(settings.getBarWidth());
			barWidthStyleControl.get().setSelection(new StructuredSelection(settings.getBarWidthStyle()));
			enableStackControl.get().setSelection(settings.isEnableStack());
		}
	}

	private void createBarColorSection(Composite parent) {

		String title = "Bar Color";
		createSectionLabel(parent, title);
		//
		Text text = createColorChoser(parent, title, getGridData(GridData.FILL_HORIZONTAL, 1), new Consumer<Color>() {

			@Override
			public void accept(Color color) {

				IBarSeriesSettings settings = getSettings();
				if(settings != null) {
					settings.setBarColor(color);
				}
			}
		});
		//
		barColorControl.set(text);
	}

	private void createBarOverlaySection(Composite parent) {

		createSectionLabel(parent, "");
		//
		Button button = createCheckBox(parent, "Bar Overlay", "Enable or disable to display the bar overlay.", getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Boolean>() {

			@Override
			public void accept(Boolean selection) {

				IBarSeriesSettings settings = getSettings();
				if(settings != null) {
					settings.setBarOverlay(selection);
				}
			}
		});
		//
		barOverlayControl.set(button);
	}

	private void createBarPaddingSection(Composite parent) {

		String title = "Bar Padding";
		createSectionLabel(parent, title);
		//
		Spinner spinner = createSpinner(parent, title, 1, 50, 1, getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Integer>() {

			@Override
			public void accept(Integer selection) {

				IBarSeriesSettings settings = getSettings();
				if(settings != null) {
					settings.setBarPadding(selection);
				}
			}
		});
		//
		barPaddingControl.set(spinner);
	}

	private void createBarWidthSection(Composite parent) {

		String title = "Bar Width";
		createSectionLabel(parent, title);
		//
		Spinner spinner = createSpinner(parent, title, 1, 50, 1, getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Integer>() {

			@Override
			public void accept(Integer selection) {

				IBarSeriesSettings settings = getSettings();
				if(settings != null) {
					settings.setBarWidth(selection);
				}
			}
		});
		//
		barWidthControl.set(spinner);
	}

	private void createBarWidthStyleSection(Composite parent) {

		String title = "Bar Width Style";
		createSectionLabel(parent, title);
		//
		ComboViewer comboViewer = createComboViewer(parent, title, BarWidthStyle.values(), BarWidthStyle.STRETCHED, getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Object>() {

			@Override
			public void accept(Object object) {

				IBarSeriesSettings settings = getSettings();
				if(settings != null) {
					if(object instanceof BarWidthStyle) {
						settings.setBarWidthStyle((BarWidthStyle)object);
					}
				}
			}
		});
		//
		barWidthStyleControl.set(comboViewer);
	}

	private void createEnableStackSection(Composite parent) {

		createSectionLabel(parent, "");
		//
		Button button = createCheckBox(parent, "Enable Stack", "Enable or disable the stack modus.", getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Boolean>() {

			@Override
			public void accept(Boolean selection) {

				IBarSeriesSettings settings = getSettings();
				if(settings != null) {
					settings.setEnableStack(selection);
				}
			}
		});
		//
		enableStackControl.set(button);
	}
}