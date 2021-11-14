/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 27 avr. 2017
 * 
 */
package ecolabel.knowledgebase.module;

import org.semanticweb.owlapi.model.OWLOntology;

/**
 * This class is an wrapper for OWLOntolgy the main objective is to attach localPath attribute
 * While, if the OWLOntology is already loaded by OWLOntologyManager, then we might as well get the localPath by:
 * owlOntology.getOWLOntologyManager().getOntologyDocumentIRI(owlOntology).toString(); which means the String localPath is not abandonable 
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 27 avr. 2017
 * 
 */
public class LocalOWLModule {

	private OWLOntology owlOntology;
	private String localPath;
	
	
	
	
	
	/**
	 * @param owlOntology
	 * @param localPath
	 */
	public LocalOWLModule(OWLOntology owlOntology, String localPath) {
		super();
		this.owlOntology = owlOntology;
		this.localPath = localPath;
	}
	
	public LocalOWLModule(){
		super();
	}
	/**
	 * @return the owlOntology
	 */
	public OWLOntology getOwlOntology() {
		return owlOntology;
	}
	/**
	 * @param owlOntology the owlOntology to set
	 */
	public void setOwlOntology(OWLOntology owlOntology) {
		this.owlOntology = owlOntology;
	}
	/**
	 * @return the localPath
	 */
	public String getLocalPath() {
		return localPath;
	}
	/**
	 * @param localPath the localPath to set
	 */
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	
	/*******************
	 * xd As LocalOWLModule is used as an DefaultListModel and DefaultComboBoxModel, then toString function must be realized or overrided
	 */
	public String toString(){
		return owlOntology.getOWLOntologyManager().getOntologyDocumentIRI(owlOntology).toString();
	}
	
}
