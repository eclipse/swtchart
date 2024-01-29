/*******************************************************************************
 * Copyright (c) 2020, 2024 Lablicate GmbH.
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
package org.eclipse.swtchart.extensions.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swtchart.Resources;
import org.eclipse.swtchart.extensions.preferences.PreferenceInitializer;

public class ResourceSupport extends Resources {

	public static final String ICON_SET_RANGE = "set_range.gif"; // $NON-NLS-1$7
	public static final String ICON_HIDE = "hide.gif"; // $NON-NLS-1$
	public static final String ICON_RESET = "reset.gif"; // $NON-NLS-1$
	public static final String ICON_CHECKED = "checked.gif"; // $NON-NLS-1$
	public static final String ICON_UNCHECKED = "unchecked.gif"; // $NON-NLS-1$
	public static final String ICON_CHECK_ALL = "checkAll.gif"; // $NON-NLS-1$
	public static final String ICON_UNCHECK_ALL = "uncheckAll.gif"; // $NON-NLS-1$
	public static final String ICON_LEGEND = "legend.gif"; // $NON-NLS-1$
	public static final String ICON_SORT_ENABLED = "sort.gif"; // $NON-NLS-1$
	public static final String ICON_SORT_DISABLED = "sort_disabled.gif"; // $NON-NLS-1$
	public static final String ICON_POSITION = "position.gif"; // $NON-NLS-1$
	public static final String ICON_SETTINGS = "preferences.gif"; // $NON-NLS-1$
	public static final String ICON_MAPPINGS = "mappings.gif"; // $NON-NLS-1$
	public static final String ICON_DELETE = "delete.png"; // $NON-NLS-1$
	public static final String ICON_DELETE_ALL = "deleteAll.png"; // $NON-NLS-1$
	public static final String ARROW_LEFT = "arrowLeft.gif"; // $NON-NLS-1$
	public static final String ARROW_RIGHT = "arrowRight.gif"; // $NON-NLS-1$
	public static final String ARROW_UP = "arrowUp.gif"; // $NON-NLS-1$
	public static final String ARROW_DOWN = "arrowDown.gif"; // $NON-NLS-1$
	public static final String ICON_SERIES_MARKER = "seriesMarker.gif"; // $NON-NLS-1$
	public static final String ICON_IMPORT = "import.gif"; // $NON-NLS-1$
	public static final String ICON_EXPORT = "export.gif"; // $NON-NLS-1$
	public static final String ICON_RESET_SELECTED = "resetSelected.gif"; // $NON-NLS-1$
	public static final String ICON_RESET_ALL = "resetAll.gif"; // $NON-NLS-1$
	public static final String ICON_RESET_SELECTION = "reset-selection.gif"; // $NON-NLS-1$
	public static final String ICON_REDO = "redo.gif"; // $NON-NLS-1$
	public static final String ICON_UNDO = "undo.gif"; // $NON-NLS-1$
	public static final String ICON_COPY_CLIPBOARD = "copy-clipboard.png"; // $NON-NLS-1$
	public static final String ICON_CSV = "csv.gif"; // $NON-NLS-1$
	public static final String ICON_FIGURE = "figure.gif"; // $NON-NLS-1$
	public static final String ICON_BITMAP = "bitmap.gif"; // $NON-NLS-1$
	public static final String ICON_PRINT = "print.gif"; // $NON-NLS-1$
	public static final String ICON_TEX = "tex.gif"; // $NON-NLS-1$
	public static final String ICON_R = "r.gif"; // $NON-NLS-1$
	public static final String ICON_TRANSFER = "transfer.png"; // $NON-NLS-1$
	public static final String ICON_SAVE = "save.gif"; // $NON-NLS-1$
	public static final String ICON_ADD = "add.gif"; // $NON-NLS-1$
	//
	private static ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());
	private static IPreferenceStore preferenceStore = null;
	private static ImageRegistry imageRegistry = null;

	private ResourceSupport() {

	}

	/**
	 * Returns the current preference store.
	 * 
	 * @return {@link IPreferenceStore}
	 */
	public static IPreferenceStore getPreferenceStore() {

		if(preferenceStore == null) {
			/*
			 * SWTChart may be used also in a non Eclipse context.
			 * Hence, a simple file preference store instead of a ScopedPreferenceStore is used.
			 */
			String filename = System.getProperty("user.home") + File.separator + ".eclipseswtchartsettings";
			preferenceStore = new PreferenceStore(filename);
			PreferenceInitializer preferenceInitializer = new PreferenceInitializer();
			preferenceInitializer.initializeDefaultPreferences();
			/*
			 * Load existing values.
			 */
			try {
				((PreferenceStore)preferenceStore).load();
			} catch(FileNotFoundException e) {
				// Ignore
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return preferenceStore;
	}

	public static void savePreferenceStore() {

		IPreferenceStore preferenceStore = getPreferenceStore();
		if(preferenceStore instanceof PreferenceStore savablePreferenceStore) {
			try {
				savablePreferenceStore.save();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns the given image. There is no need to
	 * dispose this image. It's handled by the
	 * resource support.
	 * 
	 * @param key
	 * @return {@link Image}
	 */
	public static Image getImage(String key) {

		if(imageRegistry == null) {
			imageRegistry = initializeImageRegistry();
		}
		return resourceManager.createImage(imageRegistry.getDescriptor(key));
	}

	@Override
	protected void finalize() throws Throwable {

		/*
		 * Images
		 */
		if(imageRegistry != null) {
			imageRegistry.dispose();
		}
		super.finalize();
	}

	private static ImageRegistry initializeImageRegistry() {

		if(getDisplay() == null) {
			throw new SWTException(SWT.ERROR_THREAD_INVALID_ACCESS);
		} else {
			imageRegistry = new ImageRegistry();
		}
		//
		Set<String> imageSet = new HashSet<>();
		imageSet.add(ICON_SET_RANGE);
		imageSet.add(ICON_HIDE);
		imageSet.add(ICON_RESET);
		imageSet.add(ICON_CHECKED);
		imageSet.add(ICON_UNCHECKED);
		imageSet.add(ICON_CHECK_ALL);
		imageSet.add(ICON_UNCHECK_ALL);
		imageSet.add(ICON_LEGEND);
		imageSet.add(ICON_SORT_ENABLED);
		imageSet.add(ICON_SORT_DISABLED);
		imageSet.add(ICON_POSITION);
		imageSet.add(ICON_SETTINGS);
		imageSet.add(ICON_MAPPINGS);
		imageSet.add(ICON_DELETE);
		imageSet.add(ICON_DELETE_ALL);
		imageSet.add(ARROW_LEFT);
		imageSet.add(ARROW_RIGHT);
		imageSet.add(ARROW_UP);
		imageSet.add(ARROW_DOWN);
		imageSet.add(ICON_SERIES_MARKER);
		imageSet.add(ICON_IMPORT);
		imageSet.add(ICON_EXPORT);
		imageSet.add(ICON_RESET_SELECTED);
		imageSet.add(ICON_RESET_ALL);
		imageSet.add(ICON_RESET_SELECTION);
		imageSet.add(ICON_REDO);
		imageSet.add(ICON_UNDO);
		imageSet.add(ICON_COPY_CLIPBOARD);
		imageSet.add(ICON_CSV);
		imageSet.add(ICON_FIGURE);
		imageSet.add(ICON_BITMAP);
		imageSet.add(ICON_PRINT);
		imageSet.add(ICON_TEX);
		imageSet.add(ICON_R);
		imageSet.add(ICON_TRANSFER);
		imageSet.add(ICON_SAVE);
		imageSet.add(ICON_ADD);
		//
		for(String image : imageSet) {
			imageRegistry.put(image, createImageDescriptor(image));
		}
		//
		return imageRegistry;
	}

	/**
	 * Helps to create an image descriptor.
	 * 
	 * @param fileName
	 * @return ImageDescriptor
	 */
	private static ImageDescriptor createImageDescriptor(String fileName) {

		URL url = ResourceSupport.class.getResource("/resources/icons/16x16/" + fileName);
		return ImageDescriptor.createFromURL(url);
	}
}