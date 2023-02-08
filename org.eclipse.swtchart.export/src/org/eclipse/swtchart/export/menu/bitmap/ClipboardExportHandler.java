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
package org.eclipse.swtchart.export.menu.bitmap;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtchart.export.core.AbstractSeriesExportHandler;
import org.eclipse.swtchart.export.core.ISeriesExportConverter;
import org.eclipse.swtchart.extensions.clipboard.ImageClipboardSupport;
import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.core.ScrollableChart;

public class ClipboardExportHandler extends AbstractSeriesExportHandler implements ISeriesExportConverter {

	@Override
	public String getName() {

		return Messages.getString(Messages.COPY_IMAGE_TO_CLIPBOARD);
	}

	@Override
	public Image getIcon() {

		return ResourceSupport.getImage(ResourceSupport.ICON_BITMAP);
	}

	@Override
	public void execute(Shell shell, ScrollableChart scrollableChart) {

		ImageClipboardSupport.transfer(shell.getDisplay(), scrollableChart.getBaseChart());
	}
}
