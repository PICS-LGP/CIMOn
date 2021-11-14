/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 2 juil. 2017
 * 
 */
package ecolabel.protege.plugin.component;

import org.semanticweb.owlapi.model.IRI;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 2 juil. 2017
 * 
 */
public class ContextGraphVertex {
	private IRI ontologyIRI;
	
	public ContextGraphVertex(IRI iri){
		this.ontologyIRI = iri;
	}
	
	public String toString(){
		return ontologyIRI.toString();
	}

	/**
	 * @return the ontologyIRI
	 */
	public IRI getOntologyIRI() {
		return ontologyIRI;
	}

	/**
	 * @param ontologyIRI the ontologyIRI to set
	 */
	public void setOntologyIRI(IRI ontologyIRI) {
		this.ontologyIRI = ontologyIRI;
	}
	
	
}
