/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 23 juin 2017
 * 
 */
package ecolabel.protege.plugin.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 23 juin 2017
 * 
 */
public class ContextSkeletonView extends AbstractOWLViewComponent {
	public ContextSkeletonView() {
	}

	private static final Logger logger = LoggerFactory.getLogger(SourceOntologyView.class);
	/**
	 * Create the panel.
	 */
	
	private JSplitPane splitPaneMain = new JSplitPane();
	private JSplitPane splitSourceTarget = new JSplitPane();
	
	private ContextEditorView contextEditorView = new ContextEditorView(this);
	private SourceOntologyView sourceOntologyView = new SourceOntologyView(this);
	private TargetOntologyView targetOntologyView = new TargetOntologyView(this);
	
	


	/* (non-Javadoc)
	 * @see org.protege.editor.owl.ui.view.AbstractOWLViewComponent#initialiseOWLView()
	 */
	@Override
	protected void initialiseOWLView() throws Exception {
		// TODO Auto-generated method stub
		setSize(new Dimension(1500,1000));
		setLayout(new BorderLayout());
		
		
		sourceOntologyView.initialiseOWLView();
		targetOntologyView.initialiseOWLView();
		sourceOntologyView.refreshOntologiesList();//xd refresh chosen ontology, avoid clicking on purpose on ontologiesList combox
		targetOntologyView.refreshOntologiesList();//xd refresh chosen ontology, avoid clicking on purpose on ontologiesList combox
		contextEditorView.initialiseOWLView();
		//setSize(new Dimension(1000,1000));
		
		
		splitPaneMain.setEnabled(true);
		splitSourceTarget.setEnabled(true);
		splitPaneMain.setResizeWeight(0.5);
		splitSourceTarget.setResizeWeight(0.5);
		splitPaneMain.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		//splitPaneMain.setDividerLocation(600);//xd set the location of the divider
		add(splitPaneMain, BorderLayout.CENTER);
		
		splitSourceTarget.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitSourceTarget.setDividerLocation(300);
		splitSourceTarget.setLeftComponent(sourceOntologyView);
		splitSourceTarget.setRightComponent(targetOntologyView);
		
		splitPaneMain.setLeftComponent(splitSourceTarget);
		splitPaneMain.setRightComponent(contextEditorView);
		
		
		
		
	}


	/* (non-Javadoc)
	 * @see org.protege.editor.owl.ui.view.AbstractOWLViewComponent#disposeOWLView()
	 */
	@Override
	protected void disposeOWLView() {
		// TODO Auto-generated method stub
		
		sourceOntologyView.disposeOWLView();
		targetOntologyView.disposeOWLView();
		contextEditorView.disposeOWLView();
		
	}


	/**
	 * @return
	 */
	public OWLModelManager getOWLModelManager() {
		// TODO Auto-generated method stub
		return super.getOWLModelManager();
	}


	/**
	 * @return the contextEditorView
	 */
	public ContextEditorView getContextEditorView() {
		return contextEditorView;
	}


	/**
	 * @param contextEditorView the contextEditorView to set
	 */
	public void setContextEditorView(ContextEditorView contextEditorView) {
		this.contextEditorView = contextEditorView;
	}


	/**
	 * @return the sourceOntologyView
	 */
	public SourceOntologyView getSourceOntologyView() {
		return sourceOntologyView;
	}


	/**
	 * @param sourceOntologyView the sourceOntologyView to set
	 */
	public void setSourceOntologyView(SourceOntologyView sourceOntologyView) {
		this.sourceOntologyView = sourceOntologyView;
	}


	/**
	 * @return the targetOntologyView
	 */
	public TargetOntologyView getTargetOntologyView() {
		return targetOntologyView;
	}


	/**
	 * @param targetOntologyView the targetOntologyView to set
	 */
	public void setTargetOntologyView(TargetOntologyView targetOntologyView) {
		this.targetOntologyView = targetOntologyView;
	}


	public void updateEntityListHandler(){
		getSourceOntologyView().updateEntityList();
		getTargetOntologyView().updateEntityList();
	}

}
