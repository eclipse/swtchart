/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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