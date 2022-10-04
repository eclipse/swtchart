/*******************************************************************************
 * Copyright (c) 2022 Lablicate GmbH.
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
import java.security.InvalidParameterException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

public class ImageArrayTransfer extends ByteArrayTransfer {

	private static ImageArrayTransfer imageArrayTransferWindows = new ImageArrayTransfer(SWT.IMAGE_BMP);
	private static ImageArrayTransfer imageArrayTransferLinux = new ImageArrayTransfer(SWT.IMAGE_PNG);
	//
	private int imageCode;
	private String typeName;
	private String[] typeNames;
	private int[] typeIds;

	public static ImageArrayTransfer getInstanceWindows() {

		return imageArrayTransferWindows;
	}

	public static ImageArrayTransfer getInstanceLinux() {

		return imageArrayTransferLinux;
	}

	/**
	 * Use either SWT.IMAGE_BMP for Windows or SWT.IMAGE_PNG for Linux.
	 * 
	 * @param imageCode
	 */
	private ImageArrayTransfer(int imageCode) {

		this.imageCode = imageCode;
		if(imageCode == SWT.IMAGE_BMP) {
			typeName = "image/bmp";
		} else if(imageCode == SWT.IMAGE_PNG) {
			typeName = "image/png";
		} else {
			throw new InvalidParameterException("Only SWT.IMAGE_BMP and SWT.IMAGE_PNG are allowed.");
		}
		/*
		 * The above check was valid.
		 */
		typeNames = new String[]{typeName};
		typeIds = new int[]{registerType(typeName)};
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

		if(object instanceof ImageData) {
			if(isSupportedType(transferData)) {
				ImageData imageData = (ImageData)object;
				try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
					ImageLoader imageLoader = new ImageLoader();
					imageLoader.data = new ImageData[]{imageData};
					imageLoader.save(byteArrayOutputStream, imageCode);
					super.javaToNative(byteArrayOutputStream.toByteArray(), transferData);
				} catch(IOException e) {
					throw new UncheckedIOException(e);
				}
			}
		}
	}
}