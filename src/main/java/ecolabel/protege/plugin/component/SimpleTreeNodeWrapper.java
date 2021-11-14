/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 27 mai 2017
 * 
 */
package ecolabel.protege.plugin.component;

import org.semanticweb.owlapi.model.OWLOntology;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 27 mai 2017
 * 
 */




public class SimpleTreeNodeWrapper{

	private String iri;
	private OWLOntology ontology;
	private ContextEntityType entityType;
	private String label;//xd nick name of this entity, the princeple of entity name showing is: if the entity has a rdfs:label, then show the label, otherwise show the id.
	private String tooltip;//xd (entity's IRI full name) is also a tooltip that shows the owl entity's full name with IRI prefix
	private String id;//xd the ending part of an entity's IRI
	private boolean reserveOrNot = true;//xd flag indicating reserve this entity or not in the new context, by default we want to reserve it
	/**
	 * @param label
	 * @param tooltip
	 */
//	public SimpleTreeNodeWrapper(String id, String label, String tooltip) {
//		
//		this.id = id;
//		this.label = label;
//		this.tooltip = tooltip;
//	}
	
//	public SimpleTreeNodeWrapper(String id, String label, String tooltip, boolean reserveOrNot){
//		this.id = id;
//		this.label = label;
//		this.tooltip = tooltip;
//		this.reserveOrNot = reserveOrNot;
//	}
	
//	public SimpleTreeNodeWrapper(String id, String label, String tooltip, boolean reserveOrNot, ContextEntityType entityType){
//		this.id = id;
//		this.label = label;
//		this.tooltip = tooltip;
//		this.reserveOrNot = reserveOrNot;
//		this.entityType = entityType;
//	}
	
//	public SimpleTreeNodeWrapper(String id, String label, String tooltip, boolean reserveOrNot, ContextEntityType entityType, OWLOntology ont){
//		this.id = id;
//		this.label = label;
//		this.tooltip = tooltip;
//		this.reserveOrNot = reserveOrNot;
//		this.entityType = entityType;
//		this.ontology = ont;
//	}
	
	public SimpleTreeNodeWrapper(String id, String iri, String label, String tooltip, boolean reserveOrNot, ContextEntityType entityType, OWLOntology ont){
		this.id = id;
		this.iri = iri;
		this.label = label;
		this.tooltip = tooltip;
		this.reserveOrNot = reserveOrNot;
		this.entityType = entityType;
		this.ontology = ont;
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}
	/**
	 * @param tooltip the tooltip to set
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	
	public String toString(){
		return label + "(" + id + ")";
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the reserveOrNot
	 */
	public boolean isReserveOrNot() {
		return reserveOrNot;
	}

	/**
	 * @param reserveOrNot the reserveOrNot to set
	 */
	public void setReserveOrNot(boolean reserveOrNot) {
		this.reserveOrNot = reserveOrNot;
	}

	/**
	 * @return the entityType
	 */
	public ContextEntityType getEntityType() {
		return entityType;
	}

	/**
	 * @param entityType the entityType to set
	 */
	public void setEntityType(ContextEntityType entityType) {
		this.entityType = entityType;
	}
	/**
	 * @return the ontology
	 */
	public OWLOntology getOntology() {
		return ontology;
	}
	/**
	 * @param ontology the ontology to set
	 */
	public void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}

	/**
	 * @return the iri
	 */
	public String getIri() {
		return iri;
	}

	/**
	 * @param iri the iri to set
	 */
	public void setIri(String iri) {
		this.iri = iri;
	}
	
	
}
