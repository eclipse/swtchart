/*******************************************************************************
 * Copyright (c) 2017, 2019 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.core;

public class PrimaryAxisSettings extends AbstractAxisSettings implements IPrimaryAxisSettings {

	private boolean enableCategory;
	private String[] categorySeries;

	public PrimaryAxisSettings(String title) {
		super(title);
		enableCategory = false;
		categorySeries = new String[]{};
	}

	@Override
	public boolean isEnableCategory() {

		return enableCategory;
	}

	@Override
	public void setEnableCategory(boolean enableCategory) {

		this.enableCategory = enableCategory;
	}

	@Override
	public String[] getCategorySeries() {

		return categorySeries;
	}

	@Override
	public void setCategorySeries(String[] categorySeries) {

		this.categorySeries = categorySeries;
	}
}
