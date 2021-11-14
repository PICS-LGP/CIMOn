/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 21 juil. 2017
 * 
 */
package ecolabel.knowledgebase.context.mapping;

import org.semanticweb.owlapi.model.IRI;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 21 juil. 2017
 * this classs is used to attach semantic labels to OBO RO objectProperties.
 * The context file doesn't save much semantics, which means the detailed definition of entities are defined in corresponding ontologies. 
 * When the context is loaded into the plug-in tool, or the narrow & broad federation is generated, we apply this class to show extra semantics 
 * on the interface or insert extra axioms in to the federation. 
 */
public class SemanticCacheOBORO extends SemanticCache{
	
	//xd judge if this property is an OBORO property, use switch statement
	public boolean isOBOROProperty(IRI iri){
		
		switch (iri.toString()){
		case "http://purl.obolibrary.org/obo/RO_0002001":
			return true;
		case "http://purl.obolibrary.org/obo/RO_0002502":
			return true;
		case "http://purl.obolibrary.org/obo/RO_0002424":
			return true;
		case "http://purl.obolibrary.org/obo/RO_0000085":
			return true;
		case "http://purl.obolibrary.org/obo/RO_0001019":
			return true;
		case "http://purl.obolibrary.org/obo/RO_0001000":
			return true;
		case "http://purl.obolibrary.org/obo/RO_0002202":
			return true;
		case "http://purl.obolibrary.org/obo/RO_0002454":
			return true;
		case "http://purl.obolibrary.org/obo/RO_0002618":
			return true;
		case "http://purl.obolibrary.org/obo/RO_0002352":
			return true;
		case "http://purl.obolibrary.org/obo/RO_0002353":
			return true;
		case "http://purl.obolibrary.org/obo/RO_0002434":
			return true;
		case "http://purl.obolibrary.org/obo/RO_0001025":
			return true;
		case "http://purl.obolibrary.org/obo/BFO_0000051":
			return true;
		case "http://purl.obolibrary.org/obo/BFO_0000063":
			return true;
		
		}
		return false;
		
		
//		if(iri.toString().equals("http://purl.obolibrary.org/obo/RO_0002001")){
//			//mapping.setText("RO_0002001");
//			//mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002001 \"aligned with\"");
//			return true;
//		}else if(mappingTypeOBORO.getSelectedItem().equals("depends on")){
//			mapping.setText("RO_0002502");
//			mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002502 \"depends on\"");
//		}else if(mappingTypeOBORO.getSelectedItem().equals("differs in")){
//			mapping.setText("RO_0002424");
//			mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002424 \"differs in\"");
//		}else if(mappingTypeOBORO.getSelectedItem().equals("has function")){
//			mapping.setText("RO_0000085");
//			mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0000085 \"has function\"");
//		}else if(mappingTypeOBORO.getSelectedItem().equals("contains")){
//			mapping.setText("RO_0001019");
//			mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0001019 \"contains\"");
//		}else if(mappingTypeOBORO.getSelectedItem().equals("derives from")){
//			mapping.setText("RO_0001000");
//			mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0001000 \"derives from\"");
//		}else if(mappingTypeOBORO.getSelectedItem().equals("develops from")){
//			mapping.setText("RO_0002202");
//			mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002202 \"develops from\"");
//		}else if(mappingTypeOBORO.getSelectedItem().equals("has host")){
//			mapping.setText("RO_0002454");
//			mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002454 \"has host\"");
//		}else if(mappingTypeOBORO.getSelectedItem().equals("visits")){
//			mapping.setText("RO_0002618");
//			mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002618 \"visits\"");
//		}else if(mappingTypeOBORO.getSelectedItem().equals("input of")){
//			mapping.setText("RO_0002352");
//			mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002352 \"input of\"");
//		}else if(mappingTypeOBORO.getSelectedItem().equals("output of")){
//			mapping.setText("RO_0002353");
//			mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002353 \"output of\"");
//		}else if(mappingTypeOBORO.getSelectedItem().equals("interacts with")){
//			mapping.setText("RO_0002434");
//			mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0002434 \"interacts with\"");
//		}else if(mappingTypeOBORO.getSelectedItem().equals("located in")){
//			mapping.setText("RO_0001025");
//			mapping.setToolTipText("http://purl.obolibrary.org/obo/RO_0001025 \"located in\"");
//		}else if(mappingTypeOBORO.getSelectedItem().equals("has part")){
//			mapping.setText("BFO_0000051");
//			mapping.setToolTipText("http://purl.obolibrary.org/obo/BFO_0000051 \"has part\"");
//		}else if(mappingTypeOBORO.getSelectedItem().equals("precedes")){
//			mapping.setText("BFO_0000063");
//			mapping.setToolTipText("http://purl.obolibrary.org/obo/BFO_0000063 \"precedes\"");
//		}
	}
	
	/*
	 * xd this function returns object property's semantic in string
	 * idealy this semantic should be retrived from ontology
	 * 
	 * */
	public String getLabelStringForObjectProperty(IRI iri){
		switch (iri.toString()){
		case "http://purl.obolibrary.org/obo/RO_0002001":
			return "aligned with";
		case "http://purl.obolibrary.org/obo/RO_0002502":
			return "depends on";
		case "http://purl.obolibrary.org/obo/RO_0002424":
			return "differs in";
		case "http://purl.obolibrary.org/obo/RO_0000085":
			return "has function";
		case "http://purl.obolibrary.org/obo/RO_0001019":
			return "contains";
		case "http://purl.obolibrary.org/obo/RO_0001000":
			return "derives from";
		case "http://purl.obolibrary.org/obo/RO_0002202":
			return "develops from";
		case "http://purl.obolibrary.org/obo/RO_0002454":
			return "has host";
		case "http://purl.obolibrary.org/obo/RO_0002618":
			return "visits";
		case "http://purl.obolibrary.org/obo/RO_0002352":
			return "input of";
		case "http://purl.obolibrary.org/obo/RO_0002353":
			return "output of";
		case "http://purl.obolibrary.org/obo/RO_0002434":
			return "interacts with";
		case "http://purl.obolibrary.org/obo/RO_0001025":
			return "located in";
		case "http://purl.obolibrary.org/obo/BFO_0000051":
			return "has part";
		case "http://purl.obolibrary.org/obo/BFO_0000063":
			return "precedes";
		
		}
		return "";//xd if no match return null
	}
}
