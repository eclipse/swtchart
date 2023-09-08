/*******************************************************************************
 * Copyright (c) 2019, 2023 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christian Georgi (SAP SE) - Bug 540440
 * Matthias Mailänder - adapted for SWT Chart
 *******************************************************************************/
package org.eclipse.swtchart.extensions.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Geometry;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.Resources;

public class ClickBindingHelpDialog extends Window {

	private static final int POPUP_FONT_SIZEFACTOR_KEY_LABEL = 2;
	private static final int POPUP_FONT_SIZEFACTOR_KEY = POPUP_FONT_SIZEFACTOR_KEY_LABEL + 1;
	private static final int MARGIN_BOTTOM = 25;
	private final List<Resource> resources = new ArrayList<>(3);
	private final int timeToClose;
	private String shortcut;
	private String name;
	private String description;
	private boolean readyToClose = true;

	public ClickBindingHelpDialog(Shell parentShell, int timeToClose) {

		super(parentShell);
		this.timeToClose = timeToClose;
		setShellStyle((SWT.NO_TRIM | SWT.ON_TOP | SWT.TOOL) & ~SWT.APPLICATION_MODAL);
	}

	public void setShortcut(String shortcut, String shortcutText, String shortcutDescription) {

		this.shortcut = shortcut;
		this.name = shortcutText;
		this.description = shortcutDescription;
	}

	@Override
	public int open() {

		scheduleClose();
		Shell shell = getShell();
		if(shell == null || shell.isDisposed()) {
			create();
			shell = getShell();
		}
		constrainShellSize();
		shell.setVisible(true);
		return OK;
	}

	private void scheduleClose() {

		this.readyToClose = true;
		Display.getDefault().timerExec(this.timeToClose, () -> {
			if(ClickBindingHelpDialog.this.readyToClose && getShell() != null && !getShell().isDisposed()) {
				close();
			}
		});
	}

	@Override
	public boolean close() {

		boolean closed = super.close();
		for(Resource resource : this.resources) {
			resource.dispose();
		}
		this.resources.clear();
		return closed;
	}

	@Override
	protected void configureShell(Shell newShell) {

		super.configureShell(newShell);
		Color color = Resources.getColor("66,66,66");
		newShell.setBackground(color);
		newShell.setAlpha(170);
	}

	@Override
	protected Control createContents(Composite parent) {

		Font font = JFaceResources.getDialogFont();
		FontData[] defaultFontData = font.getFontData();
		Color foregroundColor = parent.getDisplay().getSystemColor(SWT.COLOR_WHITE);
		Composite contents = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().applyTo(contents);
		contents.setBackground(parent.getBackground());
		String primaryText = null;
		if(shortcut != null && name != null) {
			primaryText = shortcut + " – " + name; //$NON-NLS-1$
		} else if(shortcut != null) {
			primaryText = shortcut;
		} else if(name != null) {
			primaryText = name;
		}
		if(primaryText != null) {
			Label shortcutLabel = new Label(contents, SWT.CENTER);
			GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(shortcutLabel);
			FontData fontData = new FontData(defaultFontData[0].getName(), defaultFontData[0].getHeight() * POPUP_FONT_SIZEFACTOR_KEY, SWT.BOLD);
			Font shortcutFont = new Font(getShell().getDisplay(), fontData);
			this.resources.add(shortcutFont);
			shortcutLabel.setBackground(parent.getBackground());
			shortcutLabel.setForeground(foregroundColor);
			shortcutLabel.setFont(shortcutFont);
			shortcutLabel.setText(primaryText);
		}
		if(description != null) {
			Label shortcutDescriptionLabel = new Label(contents, SWT.CENTER);
			GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(shortcutDescriptionLabel);
			FontData fontData = new FontData(defaultFontData[0].getName(), (int)(defaultFontData[0].getHeight() * 1.3), SWT.NORMAL);
			Font shortcutFont = new Font(getShell().getDisplay(), fontData);
			this.resources.add(shortcutFont);
			shortcutDescriptionLabel.setFont(shortcutFont);
			shortcutDescriptionLabel.setBackground(parent.getBackground());
			shortcutDescriptionLabel.setForeground(foregroundColor);
			shortcutDescriptionLabel.setText(description);
		}
		return contents;
	}

	@Override
	protected Point getInitialLocation(Point initialSize) {

		Composite parent = getShell().getParent();
		Rectangle parentBounds = parent.getBounds();
		Monitor monitor = parent.getMonitor();
		Rectangle monitorBounds = monitor.getClientArea();
		Point centerPoint = Geometry.centerPoint(parent.getBounds());
		return new Point(centerPoint.x - (initialSize.x / 2), //
				Math.max(monitorBounds.y, parentBounds.y + parentBounds.height - (initialSize.y) - MARGIN_BOTTOM));
	}
}
