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

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.IEnumLabel;
import org.eclipse.swtchart.extensions.barcharts.IBarSeriesSettings;
import org.eclipse.swtchart.extensions.core.ISeriesSettings;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesSettings;
import org.eclipse.swtchart.extensions.piecharts.ICircularSeriesSettings;
import org.eclipse.swtchart.extensions.scattercharts.IScatterSeriesSettings;
import org.eclipse.swtchart.extensions.widgets.ExtendedComboViewer;

public abstract class AbstractSeriesSettingsDialog<T extends ISeriesSettings> extends TitleAreaDialog {

	private AtomicReference<ComboViewer> settingStatusControl = new AtomicReference<>();
	private AtomicReference<Text> descriptionControl = new AtomicReference<>();
	private AtomicReference<Button> visibleControl = new AtomicReference<>();
	private AtomicReference<Button> visibleInLegendControl = new AtomicReference<>();
	//
	private String title = "";
	private T settingsNormal = null;
	private T settingsHighlight = null;
	private T settingsSelected = null;

	@SuppressWarnings("unchecked")
	public AbstractSeriesSettingsDialog(Shell parentShell, T settings) {

		super(parentShell);
		//
		this.settingsNormal = settings;
		this.settingsHighlight = (T)settings.getSeriesSettingsHighlight();
		this.settingsSelected = this.settingsNormal;
		//
		String type;
		if(settings instanceof IBarSeriesSettings) {
			type = "Bar";
		} else if(settings instanceof ICircularSeriesSettings) {
			type = "Circular";
		} else if(settings instanceof ILineSeriesSettings) {
			type = "Line";
		} else if(settings instanceof IScatterSeriesSettings) {
			type = "Scatter";
		} else {
			type = "";
		}
		//
		this.title = (type + " Series Settings").trim();
	}

	@Override
	public void create() {

		super.create();
		setTitle(title);
		setMessage("Modify the selected series settings.");
		// getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	public T getSettings() {

		return settingsSelected;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite)super.createDialogArea(parent);
		//
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(getGridData(GridData.FILL_BOTH, 1));
		GridLayout layout = new GridLayout(3, false);
		composite.setLayout(layout);
		//
		createInputSection(composite);
		//
		initialize();
		return container;
	}

	protected void createInputSection(Composite parent) {

		createHighlightSection(parent);
		createDescriptionSection(parent);
		createVisibleSection(parent);
		createVisibleInLegendSection(parent);
	}

	protected void initialize() {

		descriptionControl.get().setText(settingsSelected != null ? settingsSelected.getDescription() : "");
		visibleControl.get().setSelection(settingsSelected != null ? settingsSelected.isVisible() : false);
		visibleInLegendControl.get().setSelection(settingsSelected != null ? settingsSelected.isVisibleInLegend() : false);
	}

	protected Label createSectionLabel(Composite parent, String title) {

		Label label = new Label(parent, SWT.NONE);
		label.setText(title);
		label.setLayoutData(getGridData(GridData.FILL_HORIZONTAL, 1));
		//
		return label;
	}

	protected ComboViewer createComboViewer(Composite parent, String tooltip, IEnumLabel[] input, Object selection, GridData gridData, Consumer<Object> consumer) {

		ComboViewer comboViewer = new ExtendedComboViewer(parent, SWT.READ_ONLY);
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setInput(input);
		comboViewer.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {

				if(element instanceof IEnumLabel) {
					return ((IEnumLabel)element).label();
				}
				//
				return null;
			}
		});
		//
		Combo combo = comboViewer.getCombo();
		combo.setToolTipText(tooltip);
		combo.setLayoutData(gridData);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				consumer.accept(comboViewer.getStructuredSelection().getFirstElement());
			}
		});
		//
		if(selection != null) {
			comboViewer.setSelection(new StructuredSelection(selection));
		}
		//
		return comboViewer;
	}

	protected Text createText(Composite parent, String title, String tooltip, GridData gridData, Consumer<String> consumer) {

		Text text = new Text(parent, SWT.BORDER);
		text.setText(title);
		text.setToolTipText(tooltip);
		text.setLayoutData(gridData);
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				consumer.accept(text.getText().trim());
			}
		});
		//
		return text;
	}

	protected Text createColorChoser(Composite parent, String title, GridData gridData, Consumer<Color> consumer) {

		Text text = new Text(parent, SWT.BORDER | SWT.READ_ONLY);
		text.setText("");
		text.setToolTipText("");
		text.setLayoutData(getGridData(GridData.FILL_HORIZONTAL, 1));
		//
		Button button = new Button(parent, SWT.PUSH);
		button.setText("...");
		button.setToolTipText("Select the " + title);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				ColorDialog colorDialog = new ColorDialog(e.display.getActiveShell());
				colorDialog.setText(title);
				RGB rgbNew = colorDialog.open();
				if(rgbNew != null) {
					Color colorNew = ResourceSupport.getColor(rgbNew);
					text.setBackground(colorNew);
					consumer.accept(colorNew);
				}
			}
		});
		//
		return text;
	}

	protected Button createCheckBox(Composite parent, String title, String tooltip, GridData gridData, Consumer<Boolean> consumer) {

		Button button = new Button(parent, SWT.CHECK);
		button.setText(title);
		button.setToolTipText(tooltip);
		button.setLayoutData(gridData);
		//
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				consumer.accept(button.getSelection());
			}
		});
		//
		return button;
	}

	protected Spinner createSpinner(Composite parent, String tooltip, int min, int max, int selection, GridData gridData, Consumer<Integer> consumer) {

		Spinner spinner = new Spinner(parent, SWT.BORDER);
		spinner.setToolTipText(tooltip);
		spinner.setLayoutData(gridData);
		spinner.setMinimum(min);
		spinner.setMaximum(max);
		spinner.setSelection(selection);
		spinner.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				consumer.accept(spinner.getSelection());
			}
		});
		//
		return spinner;
	}

	protected GridData getGridData(int style, int horizontalSpan) {

		GridData gridData = new GridData(style);
		gridData.horizontalSpan = horizontalSpan;
		//
		return gridData;
	}

	private void createHighlightSection(Composite parent) {

		String title = "Setting Status";
		createSectionLabel(parent, title);
		//
		ComboViewer comboViewer = createComboViewer(parent, title, SettingsStatus.values(), SettingsStatus.NORMAL, getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Object>() {

			@Override
			public void accept(Object object) {

				if(settingsSelected != null) {
					if(object instanceof SettingsStatus) {
						SettingsStatus settingsStatus = (SettingsStatus)object;
						if(SettingsStatus.HIGHLIGHT.equals(settingsStatus)) {
							settingsSelected = settingsHighlight;
						} else {
							settingsSelected = settingsNormal;
						}
						initialize();
					}
				}
			}
		});
		//
		settingStatusControl.set(comboViewer);
	}

	private void createDescriptionSection(Composite parent) {

		String title = "Description";
		createSectionLabel(parent, title);
		//
		Text text = createText(parent, "", "The series description can be modified here.", getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<String>() {

			@Override
			public void accept(String text) {

				if(settingsSelected != null) {
					settingsSelected.setDescription(text);
				}
			}
		});
		//
		descriptionControl.set(text);
	}

	private void createVisibleSection(Composite parent) {

		createSectionLabel(parent, "");
		//
		Button button = createCheckBox(parent, "Visible", "Show or hide the series in the chart.", getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Boolean>() {

			@Override
			public void accept(Boolean selection) {

				if(settingsSelected != null) {
					settingsSelected.setVisible(selection);
				}
			}
		});
		//
		visibleControl.set(button);
	}

	private void createVisibleInLegendSection(Composite parent) {

		createSectionLabel(parent, "");
		//
		Button button = createCheckBox(parent, "Visible in Legend", "Show or hide the series in the chart legend.", getGridData(GridData.FILL_HORIZONTAL, 2), new Consumer<Boolean>() {

			@Override
			public void accept(Boolean selection) {

				if(settingsSelected != null) {
					settingsSelected.setVisibleInLegend(selection);
				}
			}
		});
		//
		visibleInLegendControl.set(button);
	}
}