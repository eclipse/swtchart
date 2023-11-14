/*******************************************************************************
 * Copyright (c) 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.menu.legend;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.menu.legend.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	//
	public static String addSeriesMapping = "addSeriesMapping";
	public static String removeSeriesMapping = "removeSeriesMapping";
	public static String addRemoveSeriesMapping = "addRemoveSeriesMapping";
	public static String adjustColorOfSelectedSeries = "adjustColorOfSelectedSeries";
	public static String description = "description";
	public static String descriptionMustNotBeEmpty = "descriptionMustNotBeEmpty";
	public static String forgotToSetDescription = "forgotToSetDescription";
	public static String hideSelectedSeries = "hideSelectedSeries";
	public static String hideSelectedSeriesInLegend = "hideSelectedSeriesInLegend";
	public static String hideSeries = "hideSeries";
	public static String hideSeriesInLegend = "hideSeriesInLegend";
	public static String setColor = "setColor";
	public static String setDescription = "setDescription";
	public static String setDescriptionForSelectedSeries = "setDescriptionForSelectedSeries";
	public static String setSeriesColor = "setSeriesColor";
	public static String showSelectedSeries = "showSelectedSeries";
	public static String showSelectedSeriesInLegend = "showSelectedSeriesInLegend";
	public static String showSeries = "showSeries";
	public static String showSeriesInLegend = "showSeriesInLegend";

	private Messages() {

	}

	public static String getString(String key) {

		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch(MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
