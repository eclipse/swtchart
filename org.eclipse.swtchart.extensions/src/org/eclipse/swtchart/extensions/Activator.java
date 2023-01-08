/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - lazy init of ImageRegistry
 *******************************************************************************/
package org.eclipse.swtchart.extensions;

import org.eclipse.swtchart.extensions.core.ResourceSupport;
import org.eclipse.swtchart.extensions.internal.mappings.MappingsIO;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	private static Activator plugin;
	private Bundle bundle;

	/**
	 * The constructor
	 */
	public Activator() {

	}

	@Override
	public void start(BundleContext context) throws Exception {

		plugin = this;
		this.bundle = context.getBundle();
		MappingsIO.restoreSettings();
	}

	@Override
	public void stop(BundleContext context) throws Exception {

		ResourceSupport.savePreferenceStore();
		plugin = null;
	}

	/**
	 * Returns the bundle associated with this plug-in.
	 *
	 * @return the associated bundle
	 */
	public final Bundle getBundle() {

		return bundle;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {

		return plugin;
	}
}