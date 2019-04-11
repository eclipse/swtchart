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
package org.eclipse.swtchart.extensions.menu;

public abstract class AbstractChartMenuEntry implements IChartMenuEntry {

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCategory() == null) ? 0 : getCategory().hashCode());
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		AbstractChartMenuEntry other = (AbstractChartMenuEntry)obj;
		if(getCategory() == null) {
			if(other.getCategory() != null)
				return false;
		} else if(!getCategory().equals(other.getCategory()))
			return false;
		if(getName() == null) {
			if(other.getName() != null)
				return false;
		} else if(!getName().equals(other.getName()))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "ChartMenuEntry [category=" + getCategory() + ", name=" + getName() + "]";
	}
}
