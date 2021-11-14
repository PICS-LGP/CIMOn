package ecolabel.protege.plugin.tab;

import java.awt.BorderLayout;

import org.protege.editor.owl.ui.OWLWorkspaceViewsTab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextTab extends OWLWorkspaceViewsTab {

	private static final Logger log = LoggerFactory.getLogger(ContextTab.class);
	
	public ContextTab() {
		setToolTipText("XU Da's context tab");
	}

    @Override
	public void initialise() {
		super.initialise();
		
		log.info("Context tab initialized");
	}

	@Override
	public void dispose() {
		super.dispose();
		log.info("Context tab disposed");
	}
}
