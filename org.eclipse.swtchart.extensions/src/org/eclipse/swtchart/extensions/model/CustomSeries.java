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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class CustomSeries implements ICustomSeries {

	private String id = UUID.randomUUID().toString();
	private String label = "";
	private String description = "";
	private boolean draw = true;
	//
	private Set<ITextElement> textElements = new HashSet<>();
	private Set<IGraphicElement> graphicElements = new HashSet<>();

	@Override
	public String getId() {

		return id;
	}

	@Override
	public String getLabel() {

		return label;
	}

	@Override
	public void setLabel(String label) {

		this.label = label;
	}

	@Override
	public String getDescription() {

		return description;
	}

	@Override
	public void setDescription(String description) {

		this.description = description;
	}

	@Override
	public boolean isDraw() {

		return draw;
	}

	@Override
	public void setDraw(boolean draw) {

		this.draw = draw;
	}

	@Override
	public void clear() {

		textElements.clear();
		graphicElements.clear();
	}

	@Override
	public Set<ITextElement> getTextElements() {

		return textElements;
	}

	@Override
	public Set<IGraphicElement> getGraphicElements() {

		return graphicElements;
	}

	@Override
	public int hashCode() {

		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		CustomSeries other = (CustomSeries)obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {

		return "CustomSeries [id=" + id + ", label=" + label + ", description=" + description + ", draw=" + draw + "]";
	}
}