/*******************************************************************************
 * Copyright (c) 2017, 2020 Lablicate GmbH.
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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
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
	public static final String ICON_CHECKED = "ICON_CHECKED"; // $NON-NLS-1$
	public static final String ICON_UNCHECKED = "ICON_UNCHECKED"; // $NON-NLS-1$
	public static final String ICON_LEGEND = "ICON_LEGEND"; // $NON-NLS-1$
	public static final String ICON_POSITION = "ICON_POSITION"; // $NON-NLS-1$
	public static final String ICON_SETTINGS = "ICON_SETTINGS"; // $NON-NLS-1$
	public static final String ICON_MAPPINGS = "ICON_MAPPINGS"; // $NON-NLS-1$
	public static final String ICON_DELETE = "ICON_DELETE"; // $NON-NLS-1$
	public static final String ICON_DELETE_ALL = "ICON_DELETE_ALL"; // $NON-NLS-1$
	//
	public static final String ARROW_LEFT = "ARROW_LEFT"; // $NON-NLS-1$
	public static final String ARROW_RIGHT = "ARROW_RIGHT"; // $NON-NLS-1$
	public static final String ARROW_UP = "ARROW_UP"; // $NON-NLS-1$
	public static final String ARROW_DOWN = "ARROW_DOWN"; // $NON-NLS-1$
	//
	private static Activator plugin;
	private ImageRegistry imageRegistry;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {

		plugin = null;
		super.stop(context);
	}

	public Image getImage(String key) {

		if(imageRegistry == null) {
			initializeImageRegistry();
		}
		return imageRegistry.get(key);
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

		if(Display.getCurrent() == null) {
			throw new SWTException(SWT.ERROR_THREAD_INVALID_ACCESS);
		} else {
			imageRegistry = new ImageRegistry();
		}
		//
		Map<String, String> imageHashMap = new HashMap<String, String>();
		//
		imageHashMap.put(ICON_SET_RANGE, "icons/16x16/set_range.gif"); // $NON-NLS-1$
		imageHashMap.put(ICON_HIDE, "icons/16x16/hide.gif"); // $NON-NLS-1$
		imageHashMap.put(ICON_RESET, "icons/16x16/reset.gif"); // $NON-NLS-1$
		imageHashMap.put(ICON_CHECKED, "icons/16x16/checked.gif"); // $NON-NLS-1$
		imageHashMap.put(ICON_UNCHECKED, "icons/16x16/unchecked.gif"); // $NON-NLS-1$
		imageHashMap.put(ICON_LEGEND, "icons/16x16/legend.gif"); // $NON-NLS-1$
		imageHashMap.put(ICON_POSITION, "icons/16x16/position.gif"); // $NON-NLS-1$
		imageHashMap.put(ICON_SETTINGS, "icons/16x16/preferences.gif"); // $NON-NLS-1$
		imageHashMap.put(ICON_MAPPINGS, "icons/16x16/mappings.gif"); // $NON-NLS-1$
		imageHashMap.put(ICON_DELETE, "icons/16x16/delete.gif"); // $NON-NLS-1$
		imageHashMap.put(ICON_DELETE_ALL, "icons/16x16/deleteAll.gif"); // $NON-NLS-1$
		//
		imageHashMap.put(ARROW_LEFT, "icons/16x16/arrowLeft.gif"); // $NON-NLS-1$
		imageHashMap.put(ARROW_RIGHT, "icons/16x16/arrowRight.gif"); // $NON-NLS-1$
		imageHashMap.put(ARROW_UP, "icons/16x16/arrowUp.gif"); // $NON-NLS-1$
		imageHashMap.put(ARROW_DOWN, "icons/16x16/arrowDown.gif"); // $NON-NLS-1$
		//
		for(Map.Entry<String, String> entry : imageHashMap.entrySet()) {
			imageRegistry.put(entry.getKey(), createImageDescriptor(getBundle(), entry.getValue()));
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
