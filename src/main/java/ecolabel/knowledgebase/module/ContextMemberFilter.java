/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 26 juin 2017
 * 
 */
package ecolabel.knowledgebase.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 26 juin 2017
 * 
 */
public class ContextMemberFilter {
	private IRI iri;//xd ontology member's iri
	private Map<IRI, Boolean> classFilter = new HashMap<IRI, Boolean>();
	private Map<IRI, Boolean> objectPropertyFilter = new HashMap<IRI, Boolean>();
	private Map<IRI, Boolean> dataPropertyFilter = new HashMap<IRI, Boolean>();
	private Map<IRI, Boolean> individualFilter = new HashMap<IRI, Boolean>();
	
	public ContextMemberFilter(OWLOntology ont){
		System.out.println("Initialising ContextMemberFilter...");
		classFilter = new HashMap<IRI, Boolean>();
		objectPropertyFilter = new HashMap<IRI, Boolean>();
		dataPropertyFilter = new HashMap<IRI, Boolean>();
		individualFilter = new HashMap<IRI, Boolean>();
		this.iri = ont.getOntologyID().getOntologyIRI().get();
	    ArrayList<OWLClass> memberClasses = new ArrayList<OWLClass>(ont.getClassesInSignature());
	    ArrayList<OWLObjectProperty> memberObjectProperties = new ArrayList<OWLObjectProperty>(ont.getObjectPropertiesInSignature());
	    ArrayList<OWLDataProperty> memberDataProperties = new ArrayList<OWLDataProperty>(ont.getDataPropertiesInSignature());
	    ArrayList<OWLNamedIndividual> memberIndividuals = new ArrayList<OWLNamedIndividual>(ont.getIndividualsInSignature());
	    
	    Iterator<OWLClass> itClass = memberClasses.iterator();
		while (itClass.hasNext()) {
			OWLClass c = itClass.next();
			classFilter.put(c.getIRI(), true);/*xd or new Boolean(true)*/
		}
		
		Iterator<OWLObjectProperty> itObjectProperty = memberObjectProperties.iterator();
		while (itObjectProperty.hasNext()) {
			OWLObjectProperty o = itObjectProperty.next();
			objectPropertyFilter.put(o.getIRI(), true);
		}
		
		Iterator<OWLDataProperty> itDataProperty = memberDataProperties.iterator();
		while (itDataProperty.hasNext()) {
			OWLDataProperty d = itDataProperty.next();
			dataPropertyFilter.put(d.getIRI(), true);
		}
		
		Iterator<OWLNamedIndividual> itIndividual = memberIndividuals.iterator();
		while (itIndividual.hasNext()) {
			OWLNamedIndividual indi = itIndividual.next();
			individualFilter.put(indi.getIRI(), true);
		}
	}
	
	
	public void parseContextMemberFilter(Node c){
		System.out.println("pasing ContextMemberFilter...");
		NodeList entityFilters = c.getChildNodes();//xd normally, we have only 4 sub filters inside
		for(int i = 0; i < entityFilters.getLength(); i ++){
			Node currentEntityFilter = entityFilters.item(i);
			if(currentEntityFilter.getNodeName().equals("classFilter")){
				parseFilter(currentEntityFilter, classFilter);
			}else if(currentEntityFilter.getNodeName().equals("objectPropertyFilter")){
				parseFilter(currentEntityFilter, objectPropertyFilter);
			}else if(currentEntityFilter.getNodeName().equals("dataPropertyFilter")){
				parseFilter(currentEntityFilter, dataPropertyFilter);
			}else if(currentEntityFilter.getNodeName().equals("individualFilter")){
				parseFilter(currentEntityFilter, individualFilter);
			}
		}
	}
	
	private void parseFilter(Node n, Map<IRI, Boolean> mf) {
		System.out.println("parsing Filter...");
		String mode = ((Element) n).getAttribute("mode");
		if(mode.equals("selectAll")){
			for(Map.Entry<IRI, Boolean> entry : mf.entrySet()){
			    mf.replace(entry.getKey(),true);
			}
		}else if(mode.equals("deselectAll")){
			for(Map.Entry<IRI, Boolean> entry : mf.entrySet()){
			    mf.replace(entry.getKey(),false);
			}
		}else if(mode.equals("part")){
			
			NodeList filterChildren = n.getChildNodes();
			for(int j = 0; j < filterChildren.getLength(); j ++){
				if(filterChildren.item(j).getNodeName().equals("filterItem")){
					Element e = (Element) filterChildren.item(j);
					mf.replace(IRI.create(e.getAttribute("entityIRI")),Boolean.parseBoolean(e.getAttribute("reserveOrNot")));
				}
			}
			
		}
		
	}

	
	public String getFilterMode(Map<IRI, Boolean> map){
		
		if(map.containsValue(new Boolean(true)) && !map.containsValue(new Boolean(false))){
			return "selectAll";
		}else if(map.containsValue(new Boolean(false)) && !map.containsValue(new Boolean(true))){
			return "deselectAll";
		}else if(map.containsValue(new Boolean(true)) && map.containsValue(new Boolean(false))){
			return "part";
		}else{
			return "selectAll";//xd by default, we select all the entities
		}
	}
	
	/**
	 * @return the classFilter
	 */
	public Map<IRI, Boolean> getClassFilter() {
		return classFilter;
	}

	/**
	 * @param classFilter the classFilter to set
	 */
	public void setClassFilter(Map<IRI, Boolean> classFilter) {
		this.classFilter = classFilter;
	}

	/**
	 * @return the objectPropertyFilter
	 */
	public Map<IRI, Boolean> getObjectPropertyFilter() {
		return objectPropertyFilter;
	}

	/**
	 * @param objectPropertyFilter the objectPropertyFilter to set
	 */
	public void setObjectPropertyFilter(Map<IRI, Boolean> objectPropertyFilter) {
		this.objectPropertyFilter = objectPropertyFilter;
	}

	/**
	 * @return the dataPropertyFilter
	 */
	public Map<IRI, Boolean> getDataPropertyFilter() {
		return dataPropertyFilter;
	}

	/**
	 * @param dataPropertyFilter the dataPropertyFilter to set
	 */
	public void setDataPropertyFilter(Map<IRI, Boolean> dataPropertyFilter) {
		this.dataPropertyFilter = dataPropertyFilter;
	}

	/**
	 * @return the individualFilter
	 */
	public Map<IRI, Boolean> getIndividualFilter() {
		return individualFilter;
	}

	/**
	 * @param individualFilter the individualFilter to set
	 */
	public void setIndividualFilter(Map<IRI, Boolean> individualFilter) {
		this.individualFilter = individualFilter;
	}
	
	
}
