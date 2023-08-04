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
package org.eclipse.swtchart.extensions.clipboard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

public class ImageArrayTransfer extends ByteArrayTransfer {

	private static final String TYPE_NAME_BMP = "image/bmp";
	private static final String TYPE_NAME_PNG = "image/png";
	//
	private static final ImageArrayTransfer WINDOWS = new ImageArrayTransfer(TYPE_NAME_BMP);
	private static final ImageArrayTransfer LINUX = new ImageArrayTransfer(TYPE_NAME_PNG);
	//
	private IImageClipboardSupplier clipboardSupplier = null;
	private String[] typeNames;
	private int[] typeIds;

	public static ImageTransfer getImageTransferWindows() {

		return ImageTransfer.getInstance();
	}

	public static ImageArrayTransfer getInstanceWindows() {

		return WINDOWS;
	}

	public static ImageArrayTransfer getInstanceLinux() {

		return LINUX;
	}

	public static ImageTransfer getImageTransferMacOS() {

		return ImageTransfer.getInstance();
	}

	public static ImageArrayTransfer getInstanceSpecific(IImageClipboardSupplier clipboardSupplier) {

		return new ImageArrayTransfer(clipboardSupplier.getTypeName(), clipboardSupplier);
	}

	private ImageArrayTransfer(String typeName) {

		this(typeName, null);
	}

	private ImageArrayTransfer(String typeName, IImageClipboardSupplier clipboardSupplier) {

		typeNames = new String[]{typeName};
		typeIds = new int[]{registerType(typeName)};
		this.clipboardSupplier = clipboardSupplier;
	}

	@Override
	protected String[] getTypeNames() {

		return typeNames;
	}

	@Override
	protected int[] getTypeIds() {

		return typeIds;
	}

	@Override
	protected void javaToNative(Object object, TransferData transferData) {

		if(isSupportedType(transferData)) {
			if(object instanceof ImageData imageData) {
				/*
				 * Image
				 */
				try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
					ImageLoader imageLoader = new ImageLoader();
					imageLoader.data = new ImageData[]{imageData};
					imageLoader.save(byteArrayOutputStream, getImageCode());
					super.javaToNative(byteArrayOutputStream.toByteArray(), transferData);
				} catch(IOException e) {
					throw new UncheckedIOException(e);
				}
			} else {
				/*
				 * Supplier
				 */
				if(clipboardSupplier != null) {
					super.javaToNative(clipboardSupplier.getData(object), transferData);
				}
			}
		}
	}

	private int getImageCode() {

		int imageCode;
		String typeName = typeNames[0];
		//
		switch(typeName) {
			case TYPE_NAME_BMP:
				imageCode = SWT.IMAGE_BMP;
				break;
			case TYPE_NAME_PNG:
				imageCode = SWT.IMAGE_PNG;
				break;
			default:
				imageCode = SWT.IMAGE_JPEG;
				break;
		}
		//
		return imageCode;
	}
}