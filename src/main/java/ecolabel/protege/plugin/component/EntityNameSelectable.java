/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 20 juin 2017
 * 
 */
package ecolabel.protege.plugin.component;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.SwingConstants;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

import ecolabel.protege.plugin.view.ContextSkeletonView;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 20 juin 2017
 * 
 */
public class EntityNameSelectable extends JPanel {
	public ContextSkeletonView csv;
	public OWLOntology entityOntology;
	public ContextEntityType entityType;
	public String entityIRI;
	public JCheckBox reserveEntityOrNot;
	public JLabel entityId;
	public String entityLabel = "";
	public EntityNameSelectable(ItemListener il) {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		flowLayout.setHgap(10);
		
		reserveEntityOrNot = new JCheckBox("");
		add(reserveEntityOrNot);
		
		entityId = new JLabel("New label");
		add(entityId);
		
//		ItemListener anotherItemListener = new ItemListener() {
//		      public void itemStateChanged(ItemEvent itemEvent) {
//		    	  
//		    	  JCheckBox cb = (JCheckBox) itemEvent.getItem();
//		    	  
//		    	    if (itemEvent.getStateChange() == ItemEvent.SELECTED){
//		    	    	JOptionPane.showMessageDialog(null, "Selected", "title", JOptionPane.ERROR_MESSAGE);
//		    	    }else{
//		    	    	JOptionPane.showMessageDialog(null, "Not selected", "title", JOptionPane.ERROR_MESSAGE);
//		    	    }
//		    	  
//		      }
//		};
		
		reserveEntityOrNot.addItemListener(il);
	}
	

//	public EntityNameSelectable(ContextSkeletonView csv) {
//		this.csv = csv;
//		FlowLayout flowLayout = (FlowLayout) getLayout();
//		flowLayout.setAlignment(FlowLayout.LEFT);
//		flowLayout.setVgap(0);
//		flowLayout.setHgap(10);
//		
//		reserveEntityOrNot = new JCheckBox("");
//		add(reserveEntityOrNot);
//		
//		entityId = new JLabel("New label");
//		add(entityId);
//		
//		reserveEntityOrNot.addItemListener(new ItemListener(){
//		    public void itemStateChanged(ItemEvent itemEvent) {
//		    	JCheckBox cb = (JCheckBox) itemEvent.getItemSelectable();
//			    EntityNameSelectable ens = (EntityNameSelectable) cb.getParent();
//			    System.out.println("\n  Item State Changed! " + entityIRI + "(" + entityId.getText() + ")" );
//			    if(entityIRI != null && entityId.getText() != null && entityIRI.substring(entityIRI.indexOf("#")+1).equals(entityId.getText())){
//				    if(csv.getContextEditorView().getContext().getContextMemberFilterCatalog() != null){
//				    	IRI currentEntityIRI = IRI.create(entityIRI);//xd get this entity's IRI
//				  	    if (itemEvent.getStateChange() == ItemEvent.SELECTED){
//				  	    	//JOptionPane.showMessageDialog(null, "Selected", "title", JOptionPane.ERROR_MESSAGE);
//				  	  	    //System.out.println(currentEntityIRI + " " + ens.entityType.toString() + " " + ens.entityOntology.getOntologyID().getOntologyIRI().get());
//				  	  	    //CheckBoxNodeEditor cbne = (CheckBoxNodeEditor) ens.getParent();
//				  	    	
//				  	    	csv.getContextEditorView().getContext().updateItemInContextMemberFilterCatalog(true, currentEntityIRI, entityType, entityOntology);
//				  	    	
//				  	    }else if(itemEvent.getStateChange() == ItemEvent.DESELECTED){
//				  	    	
//				  	    	csv.getContextEditorView().getContext().updateItemInContextMemberFilterCatalog(false, currentEntityIRI, entityType, entityOntology);
//				  	    }
//				    }else{
//				  	    	
//				  	    	
//				  	}
//			    }else{
//			    	System.out.println("  entityIRI and entityId.getText() don't match, no update");
//			    }
//			    //updateEntityListHandler();
//			    //updateEntityList();
//		    }});
//		
//		//reserveEntityOrNot.addItemListener(il);
//	}
	
	



	/**
	 * @return the entityLabel
	 */
	public String getEntityLabel() {
		return entityLabel;
	}



	/**
	 * @param entityLabel the entityLabel to set
	 */
	public void setEntityLabel(String entityLabel) {
		this.entityLabel = entityLabel;
	}
	

	
}
