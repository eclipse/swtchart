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
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtchart.extensions.model;

import java.util.Set;

public interface ICustomSeries {

	String getId();

	String getLabel();

	void setLabel(String label);

	String getDescription();

	void setDescription(String description);

	boolean isDraw();

	void setDraw(boolean draw);

	Set<ITextElement> getTextElements();

	Set<IGraphicElement> getGraphicElements();
}