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

public interface IPrimaryAxisSettings extends IAxisSettings {

	boolean isEnableCategory();

	/**
	 * Only works for X-Axis.
	 * 
	 * @param enableCategory
	 */
	void setEnableCategory(boolean enableCategory);

	String[] getCategorySeries();

	/**
	 * Only works for X-Axis.
	 * 
	 * @param categorySeries
	 */
	void setCategorySeries(String[] categorySeries);
}