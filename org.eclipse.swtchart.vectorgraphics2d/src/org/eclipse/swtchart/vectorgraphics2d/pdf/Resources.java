/*******************************************************************************
 * Copyright (c) 2010, 2019 VectorGraphics2D project.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Erich Seifert - initial API and implementation
 * Michael Seifert - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.vectorgraphics2d.pdf;

import java.awt.Font;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swtchart.vectorgraphics2d.util.DataUtils;
import org.eclipse.swtchart.vectorgraphics2d.util.GraphicsUtils;

class Resources extends DefaultPDFObject {

	private static final String KEY_TRANSPARENCY = "ExtGState";
	private static final String KEY_IMAGE = "XObject";
	private static final String[] VALUE_PROC_SET = {"PDF", "Text", "ImageB", "ImageC", "ImageI"};
	private static final String PREFIX_FONT = "Fnt";
	private static final String PREFIX_IMAGE = "Img";
	private static final String PREFIX_TRANSPARENCY = "Trp";
	private final List<String> procSet;
	private final Map<String, TrueTypeFont> fontsByFontId;
	private final Map<Font, String> fontIDsByFont;
	private final Map<PDFObject, String> images;
	private final Map<Double, String> transparencies;
	private final AtomicInteger currentFontId = new AtomicInteger();
	private final AtomicInteger currentImageId = new AtomicInteger();
	private final AtomicInteger currentTransparencyId = new AtomicInteger();

	public Resources() {

		super(null, null, false);
		procSet = new LinkedList<>();
		fontsByFontId = new HashMap<>();
		fontIDsByFont = new HashMap<>();
		images = new HashMap<>();
		transparencies = new HashMap<>();
		setProcSet(VALUE_PROC_SET);
	}

	private <T> String getResourceId(Map<T, String> resources, T resource, String idPrefix, AtomicInteger idCounter) {

		String id = resources.get(resource);
		if(id == null) {
			id = String.format("%s%d", idPrefix, idCounter.getAndIncrement());
			resources.put(resource, id);
		}
		return id;
	}

	public String getId(Font font) {

		font = GraphicsUtils.getPhysicalFont(font);
		String resourceId = getResourceId(fontIDsByFont, font, PREFIX_FONT, currentFontId);
		String baseFontName = font.getPSName();
		// TODO: Determine font encoding (e.g. MacRomanEncoding, MacExpertEncoding, WinAnsiEncoding)
		String fontEncoding = "WinAnsiEncoding";
		TrueTypeFont pdfFont = new TrueTypeFont(fontEncoding, baseFontName);
		fontsByFontId.put(resourceId, pdfFont);
		return resourceId;
	}

	@SuppressWarnings("unchecked")
	public String getId(PDFObject image) {

		// Make sure a dictionary entry for images exists
		Map<String, PDFObject> dictEntry = (Map<String, PDFObject>)dict.get(KEY_IMAGE);
		if(dictEntry == null) {
			dictEntry = new LinkedHashMap<>();
			dict.put(KEY_IMAGE, dictEntry);
		}
		String resourceId = getResourceId(images, image, PREFIX_IMAGE, currentImageId);
		dictEntry.put(resourceId, image);
		return resourceId;
	}

	@SuppressWarnings("unchecked")
	public String getId(Double transparency) {

		// Make sure a dictionary entry for transparency levels exists
		Map<String, Map<String, Object>> dictEntry = (Map<String, Map<String, Object>>)dict.get(KEY_TRANSPARENCY);
		if(dictEntry == null) {
			dictEntry = new LinkedHashMap<>();
			dict.put(KEY_TRANSPARENCY, dictEntry);
		}
		String resourceId = getResourceId(transparencies, transparency, PREFIX_TRANSPARENCY, currentTransparencyId);
		dictEntry.put(resourceId, DataUtils.map(new String[]{"Type", "ca", "CA"}, new Object[]{"ExtGState", transparency, transparency}));
		return resourceId;
	}

	public void setProcSet(String... procedureNames) {

		procSet.clear();
		procSet.addAll(Arrays.asList(procedureNames));
	}

	public List<String> getProcSet() {

		return Collections.unmodifiableList(procSet);
	}

	public Map<String, TrueTypeFont> getFont() {

		return Collections.unmodifiableMap(fontsByFontId);
	}
}
