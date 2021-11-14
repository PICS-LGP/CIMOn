/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 13 juin 2017
 * 
 */
package ecolabel.protege.plugin.component;

import java.awt.Component;

import javax.swing.JTabbedPane;

import org.semanticweb.owlapi.model.OWLOntology;

import ecolabel.protege.plugin.view.ContextEditorView;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 13 juin 2017
 * Before the context is closed or saved, validation is needed. Each check function returns true or false as the the validation result.
 * Use different textual output in different checking functions, e.g. tooltip, virtual console, pop-up windows.
 */
public class ContextChecker {
	
	public ContextVirtualConsole console;
	
	
	public ContextChecker(ContextVirtualConsole cvc){
		this.console = cvc;
	}
	
	
	/*************
	 * xd basic mapping check
	 * @param bmc
	 * @return
	 */
	public boolean checkBinaryMapping(BinaryMappingComponent bmc){
		boolean result = true;
		bmc.ErrorSwitchOff();//xd by default we assume that there is no error in this mapping component
		String mappingType = bmc.mappingType.getSelectedItem().toString();
		//xd bmc.literalDatatype has default selected item 'string', so here we don't need to do another check.
		if(mappingType.equals("DataPropertyRange")){//xd tfHead needed only
			if(bmc.tfHead.getText().equals("")){
				result = false;
				console.printError("ERROR: Incomplete mapping, dataProperty's domain shouldn't be empty.");
				bmc.ErrorSwitchOn("ERROR: Incomplete mapping, dataProperty's domain shouldn't be empty.");
			}
		}else if(mappingType.equals("SubClassOf")//xd tfHead and tfTail needed 
				|| mappingType.equals("ClassAssertion")
				|| mappingType.equals("EquivalentClasses")
				|| mappingType.equals("DisjointClasses")
				|| mappingType.equals("SubObjectPropertyOf")
				|| mappingType.equals("ObjectPropertyDomain")
				|| mappingType.equals("ObjectPropertyRange")
				|| mappingType.equals("DifferentIndividuals")
				|| mappingType.equals("SameIndividual")
				|| mappingType.equals("DataPropertyDomain")){
			if(bmc.tfHead.getText().equals("") || bmc.tfTail.getText().equals("")){
				result = false;
				console.printError("ERROR: Incomplete mapping. 2 fields are needed");
				bmc.ErrorSwitchOn("ERROR: Incomplete mapping. 2 fields are needed");
			}
		}else if(mappingType.equals("ObjectPropertyAssertion")//xd tfHead, mapping and tfTail needed
				|| mappingType.equals("NegativeObjectPropertyAssertion")
				|| mappingType.equals("DataPropertyAssertion")
				|| mappingType.equals("NegativeDataPropertyAssertion")){
			if(bmc.tfHead.getText().equals("") || bmc.tfTail.getText().equals("") || bmc.mapping.getText().equals("")){
				result = false;
				console.printError("ERROR: Incomplete mapping. 3 fields are needed");
				bmc.ErrorSwitchOn("ERROR: Incomplete mapping. 3 fields are needed");
			}
		}
		
		
		
		return result;
	}
	
	
	
	/*************
	 * xd bundle check (structural)
	 * @param bmbc
	 * @return
	 */
	public boolean checkBinaryMappingBundle(BinaryMappingBundleComponent bmbc){
		boolean result = true;
		
		String sourceIRI = "";
		String targetIRI = "";
		
		if(bmbc.cbSourceOntology.getSelectedItem() == null || bmbc.cbTargetOntology.getSelectedItem() == null
				|| bmbc.cbSourceOntology.getSelectedIndex() == -1 || bmbc.cbTargetOntology.getSelectedIndex() == -1){
			result = false;
			console.printError("ERROR: Either source ontology IRI or target ontology IRI is empty.");
		}else{
			sourceIRI = ((OWLOntology)bmbc.cbSourceOntology.getSelectedItem()).getOntologyID().getOntologyIRI().get().toString();
			targetIRI = ((OWLOntology)bmbc.cbTargetOntology.getSelectedItem()).getOntologyID().getOntologyIRI().get().toString();
			if(sourceIRI.equals(targetIRI)){
				result = false;
				console.printError("ERROR: The source ontology IRI and target ontology IRI are same.");
			}
		}
		
		
		
		if(bmbc.getMappingBundleName().equals("")){//xd the bundle name should not be empty
			result = false;
			console.printWarning("WARNING: Mapping bundle name is empty.");
			
		}
		
		
		
		//xd other complex validation shall be left for the reasoner, here in this function we do only some structural validation 
		
		for(Component c: bmbc.binaryMappingContainer.getComponents()){
			BinaryMappingComponent currentBMC = (BinaryMappingComponent) c;
			if(!checkBinaryMapping(currentBMC)){
				result = false;
				//break;//xd 
			}
		}
		
		
		return result;
	}
	
	
	
	/***********************
	 * xd global context check
	 * @param cev
	 * @return
	 */
	public boolean checkContext(ContextEditorView cev){
		
		console.printInfo("-----------------Mappings validation------------------------");
		boolean result = true;
		String info = "";
		
		//xd if the context member list in ContextHeaderComponent doesn't match the context's members' size, then return false
		if(cev.getPanelContextHeader().list.getModel().getSize() != cev.getContext().getContextMembers().size()){
			
			result = false;
			console.printError("ERROR: Context members unsynchronized.");
		}
		
		if(cev.getContext().getContextMembers().size() < 2){//xd each context must have at least two ontologies
			result = false;
			console.printError("ERROR: At least two ontologies are needed in one context.");
		}
		
		if(cev.getPanelContextHeader().tfContextIRI.getText().equals("")){
			result = false;
			console.printError("ERROR: Context IRI is empty.");
		}
		
		if(cev.getPanelContextHeader().tfContextName.getText().equals("")){
			result = false;
			console.printError("ERROR: Context name is empty.");
		}
		
		if(cev.getPanelContextHeader().comboBox.getSelectedItem() == null || cev.getPanelContextHeader().comboBox.getSelectedIndex() == -1){
			result = false;
			console.printError("ERROR: Context root is empty.");
		}
		
		JTabbedPane tabbedPane = (JTabbedPane)cev.getPanelContextEditor().getComponent(1);//xd #0 panelContextHeader(ContextHeaderComponent) #1 panelMapping(JTabbedPane)
		for(Component c: tabbedPane.getComponents()){
			BinaryMappingBundleComponent bmbc = (BinaryMappingBundleComponent) c;
			if(!checkBinaryMappingBundle(bmbc)){
				result = false;
			}
		}
		
		if(result){
			console.printInfo("Context validated!");
		}
		
		return result;
	}


	/**
	 * @return the console
	 */
	public ContextVirtualConsole getConsole() {
		return console;
	}


	/**
	 * @param console the console to set
	 */
	public void setConsole(ContextVirtualConsole console) {
		this.console = console;
	}
	
	
	
	
	
}
