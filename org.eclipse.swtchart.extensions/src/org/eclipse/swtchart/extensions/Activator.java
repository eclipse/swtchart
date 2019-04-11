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
package org.eclipse.swtchart.extensions;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	public static final String ICON_SET_RANGE = "ICON_SET_RANGE"; // $NON-NLS-1$
	public static final String ICON_HIDE = "ICON_HIDE"; // $NON-NLS-1$
	public static final String ICON_RESET = "ICON_RESET"; // $NON-NLS-1$
	//
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {

		super.start(context);
		plugin = this;
		if(PlatformUI.isWorkbenchRunning()) {
			initializeImageRegistry();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {

		plugin = null;
		super.stop(context);
	}

	public Image getImage(String key) {

		return getImageRegistry().get(key);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {

		return plugin;
	}

	private void initializeImageRegistry() {

		Map<String, String> imageHashMap = new HashMap<String, String>();
		imageHashMap.put(ICON_SET_RANGE, "icons/16x16/set_range.gif"); // $NON-NLS-1$
		imageHashMap.put(ICON_HIDE, "icons/16x16/hide.gif"); // $NON-NLS-1$
		imageHashMap.put(ICON_RESET, "icons/16x16/reset.gif"); // $NON-NLS-1$
		//
		ImageRegistry imageRegistry = getImageRegistry();
		if(imageRegistry != null) {
			/*
			 * Set the image/icon values.
			 */
			for(Map.Entry<String, String> entry : imageHashMap.entrySet()) {
				imageRegistry.put(entry.getKey(), createImageDescriptor(getBundle(), entry.getValue()));
			}
		}
	}

	/**
	 * Helps to create an image descriptor.
	 * 
	 * @param bundle
	 * @param string
	 * @return ImageDescriptor
	 */
	private ImageDescriptor createImageDescriptor(Bundle bundle, String string) {

		ImageDescriptor imageDescriptor = null;
		IPath path = new Path(string);
		URL url = FileLocator.find(bundle, path, null);
		imageDescriptor = ImageDescriptor.createFromURL(url);
		return imageDescriptor;
	}
}
