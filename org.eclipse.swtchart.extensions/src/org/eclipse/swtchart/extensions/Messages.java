package org.eclipse.swtchart.extensions;

import org.eclipse.osgi.util.NLS;


public final class Messages extends NLS {
	
	private static final String BUNDLE_NAME = "org.eclipse.swtchart.extensions.Messages";//$NON-NLS-1$
	
	public static String RESET_CHART_LABEL;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
	
	// Don't instantiate
	private Messages() {

	}
	
	
}
