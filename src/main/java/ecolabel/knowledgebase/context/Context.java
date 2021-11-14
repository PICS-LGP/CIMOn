/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 24 avr. 2017
 * 
 */
package ecolabel.knowledgebase.context;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/*********************
 * xd 30-Mai-2017 got a serious problem of maven or project configuration, I try to add Hermit and jgrapht into the project, then errors happen:
 * org.osgi.framework.BundleException: Unresolved constraint in bundle protege.plugin.examples [20]: Unable to resolve 20.0: missing requirement [20.0] osgi.wiring.package; (osgi.wiring.package=org.semanticweb.HermiT) 
 * xd 24-Juillet-2017 I think the problem was soleved by: https://stackoverflow.com/questions/20016205/unresolved-constraint-in-bundle-missing-requirement-osgi-wiring-package
 */
//import org.jgrapht.DirectedGraph;
//import org.jgrapht.alg.KosarajuStrongConnectivityInspector;
//import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
//import org.jgrapht.graph.DefaultDirectedGraph;
//import org.jgrapht.graph.DefaultEdge;
//import org.jgrapht.graph.DirectedSubgraph;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ecolabel.knowledgebase.context.history.Parser;
import ecolabel.knowledgebase.context.history.ParserFactory;
import ecolabel.knowledgebase.context.mapping.MappingOWLAdapter;
import ecolabel.knowledgebase.module.ContextMemberFilter;
import ecolabel.knowledgebase.module.LocalOWLModule;
import ecolabel.protege.plugin.component.ContextEntityType;
import ecolabel.protege.plugin.component.ContextGraphEdge;
import ecolabel.protege.plugin.component.ContextGraphVertex;
import ecolabel.protege.plugin.test.ClassHierarchyConsolePrinter;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;


/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 24 avr. 2017
 * xd entity includeing all necessary information for contextual ontology. 
 */
public class Context {

	//xd we want to have a directed graph based representation of the context
	//xd refer to http://jgrapht.org/javadoc/org/jgrapht/graph/DefaultDirectedGraph.html for more details about DirectedGraph
	//xd refer to https://github.com/jgrapht/jgrapht/wiki/DirectedGraphDemo for example program
	//xd jgrapht doesn't work in protege 5.0.0 beta 24, we change to another java graph api JUNG.
	//xd refer to http://www.grotto-networking.com/JUNG/JUNG2-Tutorial.pdf
	//xd too bad jung doesn't work with 5.0.0 beta 24 either. The directed graph set aside for now
	//DirectedSparseMultigraph<ContextGraphVertex, ContextGraphEdge> contextGraph;//xd try to use a directed graph to record the hierarchy of the modules in the context
	
	/**********
	 * necessary parts of Context
	 */
	private boolean properlyInitialised = false;//xd a flag indicating if this context is ready or not
	private String contextName;
	private String contextIRI;
	private String contextlocalPath;
	private String contextRoot;//xd an IRI that marks the root of this context
	private Document contextDoc;//xd the DOM document of the xml file
	
	//xd check starting comments of localOWLModel.java
	private Map<String, OWLOntology> contextMembers;//xd a HashMap that keeps all relevant ontology modules, the key (String type) should be the ontology IRI
	private OWLOntologyManager contextOntologyManager;//xd strictly speaking, the OWLOntologyManager belongs to the integration's context (supporting tool) too, we put it as a member variable too 
	//xd OWLDataFactory contextDataFactory;
	//xd not sure if the merger should be also part of this context
	private LocalOWLModule contextualIntegration;//xd as for the first try, each Context has only one contextualIntegration
	private LocalOWLModule contextualIntegrationFiltered;//xd 
	
	/*****************
	 * xd we need a catalog for all the entities included in the context. This catalog concerns the reserveOrNot attribute. In other word, we need a filter-like thing to remember those entities the context needs.
	 * For now, we assume that the IRI is a unique id for each entity. Then, perhaps a Map<IRI, boolean> is enough.
	 * should pay attention that, here we reach a granularity of entity, not axioms yet.
	 */
	private Map<IRI,ContextMemberFilter> contextMemberFilterCatalog;
	
	
	
	/****
	 * xd accessory for reasoning functionality, not obligation to Context, consider abandoning them
	 */
	OWLReasonerFactory contextReasonerFactory;
    ConsoleProgressMonitor contextConsoleProgressMonitor;
    OWLReasonerConfiguration contextReasonerConfig;
    OWLReasoner contextReasoner;
    
    /*xd try again the jung api*/
	private DirectedSparseMultigraph<ContextGraphVertex, ContextGraphEdge> contextGraph;
	
	/***************
	 * xd the real method that realize the initialization. (No mapping parsing here)
	 * @param contextPath the local file path of the context path
	 */
	public void initializeContext(String contextPath){
		
		
		System.out.println("Context initializing...");
		try{
			contextOntologyManager = OWLManager.createOWLOntologyManager();
			File contextFile = new File(contextPath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(contextFile);
			
			this.setContextDoc(doc);//xd set the xml's DOM
			this.setContextlocalPath(contextPath);//xd set the context's local path
			this.getContextDoc().getDocumentElement().normalize();
			
			Node contextMembersNode = doc.getDocumentElement().getElementsByTagName("contextMembers").item(0);//xd the first and the only
			Node contextNameNode = doc.getDocumentElement().getElementsByTagName("contextName").item(0);//xd the first and only context name
			Node contextRootNode = doc.getDocumentElement().getElementsByTagName("contextRoot").item(0);//xd the first and only context root
			Node contextFilter = doc.getDocumentElement().getElementsByTagName("contextFilter").item(0);//xd the first and only context filter element
			
			if(contextNameNode.getNodeType() == Node.ELEMENT_NODE && contextNameNode.hasAttributes()){
				this.setContextName(contextNameNode.getTextContent());
				this.setContextIRI(((Element) contextNameNode).getAttribute("IRI"));
			}
			
			contextMembers = new HashMap<String, OWLOntology>();
			contextMemberFilterCatalog = new HashMap<IRI,ContextMemberFilter>();
			
			contextGraph = new DirectedSparseMultigraph<ContextGraphVertex, ContextGraphEdge>();//xd initialize the directed graph of this context
			
			
			
			NodeList owlOntologies = contextMembersNode.getChildNodes();//xd only one contextMembers tag is allowed
			for(int i = 0; i < owlOntologies.getLength(); i ++){
				Node currentOwlOntology = owlOntologies.item(i);
				if(currentOwlOntology.getNodeType() == Node.ELEMENT_NODE && currentOwlOntology.hasAttributes()){
					Element ele = (Element) currentOwlOntology;
					System.out.println("A ontology module is found: IRI=" + ele.getAttribute("IRI") + " localPath=" + ele.getAttribute("localPath"));
					
					File currentModuleFile = new File(ele.getAttribute("localPath"));  
				    OWLOntology currentModule = contextOntologyManager.loadOntologyFromOntologyDocument(currentModuleFile);    
				    //IRI currentModuleIRI = mgr.getOntologyDocumentIRI(currentModule);
				    
				    //LocalOWLModule owlModule = new LocalOWLModule();
				    //owlModule.setLocalPath(ele.getAttribute("localPath"));
				    //owlModule.setOwlOntology(currentModule);
				    
				    contextMembers.put(ele.getAttribute("IRI"), currentModule);
				    
				    /***********************
				     * The same time identifying the context members, should prepare the context filter.
				     * In this stage, by default(context member and context member filter should be synchronized), the reserveOrNot attribute value for all filters is true
				     * (in other words, if an ontology becomes an context member, by default, all it's entities should be included)
				     */
				    ContextMemberFilter cmb = new ContextMemberFilter(currentModule);
				    contextMemberFilterCatalog.put(IRI.create(ele.getAttribute("IRI")), cmb);
				    
				    contextGraph.addVertex(new ContextGraphVertex(IRI.create(ele.getAttribute("IRI"))));//xd in this context graph, each ontology module should be a vertex, and the name of the vertex shall be the IRI
				}
			}
			
			
			
			//xd 2.when the reserveOrNot checkBox is changed in Source/TargetOntologyView, should update the 4 filters too;
			NodeList contextMemberFilterList = contextFilter.getChildNodes();
			for(int i = 0; i < contextMemberFilterList.getLength(); i ++){
				Node currentContextMemberFilter = contextMemberFilterList.item(i);
				if(currentContextMemberFilter.getNodeName().equals("contextMemberFilter")){
					Element e = (Element) currentContextMemberFilter;
					contextMemberFilterCatalog.get(IRI.create(e.getAttribute("IRI"))).parseContextMemberFilter(currentContextMemberFilter);
				}
			}
			
			
			
			
			
			if(contextRootNode.getNodeType() == Node.ELEMENT_NODE && contextRootNode.hasAttributes()){
				this.setContextRoot(((Element) contextRootNode).getAttribute("IRI"));
			}
			
			NodeList bundles = doc.getDocumentElement().getElementsByTagName("binaryMappingBundle");
			for(int j = 0; j < bundles.getLength(); j ++){
				Node tempBundle = bundles.item(j);
				if(tempBundle.getNodeType() == Node.ELEMENT_NODE && tempBundle.hasAttributes()){
					Element tempEle = (Element) tempBundle;
					
					
					
					//xd find the source and target vertex, then add en edge in the graph
					Collection<ContextGraphVertex> vertices = contextGraph.getVertices();
					Iterator<ContextGraphVertex> it = vertices.iterator();
					ContextGraphVertex sourceVertex = null, targetVertex = null;
					while (it.hasNext()){
					    ContextGraphVertex v = it.next();
					    if(v.getOntologyIRI().equals(IRI.create(tempEle.getAttribute("sourceOntologyIRI")))){
					    	sourceVertex = v;
					    }
					    if(v.getOntologyIRI().equals(IRI.create(tempEle.getAttribute("targetOntologyIRI")))){
					    	targetVertex = v;
					    }
					}
					contextGraph.addEdge(new ContextGraphEdge(), sourceVertex, targetVertex, EdgeType.DIRECTED);
					
					
					
				}
			}
			
			
			//xd not sure if this contextGraph is well constructed or not, we'll see in future
			System.out.println("Context graph: ");
			System.out.println(contextGraph.toString());

			
			/***********************
			 * xd construct filtered context integration
			 */
			contextualIntegrationFiltered = new LocalOWLModule();
			contextualIntegrationFiltered.setLocalPath(contextPath);//xd normally the contextual integration will remember the config file's path
			OWLOntologyMerger mergerFiltered = new OWLOntologyMerger(contextOntologyManager);
			OWLOntology tempOwlOntologyFiltered = mergerFiltered.createMergedOntology(contextOntologyManager, IRI.create(this.getContextIRI()+"_filtered"));//xd small difference between narrow federation and broad federation
			contextualIntegrationFiltered.setOwlOntology(tempOwlOntologyFiltered);//xd now, inside the integration, there should be all relevant ontology's axioms, but without the mappings
			
			
			
			/***********************
			 * xd construct normal context integration
			 */
			contextualIntegration = new LocalOWLModule();
			contextualIntegration.setLocalPath(contextPath);//xd normally the contextual integration will remember the config file's path
			OWLOntologyMerger merger = new OWLOntologyMerger(contextOntologyManager);
			OWLOntology tempOwlOntology = merger.createMergedOntology(contextOntologyManager, IRI.create(this.getContextIRI()));
			contextualIntegration.setOwlOntology(tempOwlOntology);//xd now, inside the integration, there should be all relevant ontology's axioms, but without the mappings
			
			properlyInitialised = true;//xd in the end of initialisation, set this flag to be true;
			
			//xd reasoning test
			doContextReasoning(contextualIntegration);//xd try reasoning 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	/**
	 * @param n
	 * @param mf
	 */
	





	/**********************
	 * xd constructor with file path param (load Context from local file)
	 * xd What if config file is in another format instead of xml? The design should reserve flexibility for this
	 * @param contextPath
	 */
	public Context(String contextPath){
		this.initializeContext(contextPath);
	}
	
	/*************
	 * xd constructor without any param (new Context)
	 */
	public Context(){
		
		//xd do nothing, return a empty Context
		contextMembers = new HashMap<String, OWLOntology>();
		contextMemberFilterCatalog = new HashMap<IRI,ContextMemberFilter>();
//		contextName;
//		contextIRI;
//		contextlocalPath;
//		contextRoot;
//		contextDoc;
//		
//		
//		contextMembers;
//		contextOntologyManager;
//		
//		contextualIntegration;
		
	}
	
	/*********
	 * xd think about if the OWLReasonerFactory and OWLReasonerConfiguration or OWLReasoner parameters should be put in the Context class
	 * 30-Mai-2017 tried to put jgrapht and hermit into the plugin, didn't work, so this reasoning method is abandonned for now. 
	 */
	public boolean doContextReasoning(LocalOWLModule ont){
		System.out.println("R E A S O N ing...");
		contextReasonerFactory = new Reasoner.ReasonerFactory();
        
		contextConsoleProgressMonitor = new ConsoleProgressMonitor();
		contextReasonerConfig = new SimpleConfiguration(contextConsoleProgressMonitor);
		contextReasoner = contextReasonerFactory.createReasoner(
				ont.getOwlOntology(),
        		contextReasonerConfig);
        
		contextReasoner.precomputeInferences();
        if(contextReasoner.isConsistent()){
        	System.out.println("Integration is logical consistent.");
        	return true;
        }else{
        	System.out.println("Integration is NOT logical consistent.");
        	return false;
        }
        
	}
	
	
	
	public void printContextualIntegration(){
		
		System.out.println("Trying to print all entities in the contextual integration: ");
		org.semanticweb.owlapi.reasoner.Node<OWLClass> topNode = contextReasoner.getTopClassNode();
        ClassHierarchyConsolePrinter chcp = new ClassHierarchyConsolePrinter();
        System.out.println("----All classes:");
        chcp.printClassHierarchy(topNode, contextReasoner, 0);
	}
	
	
	/*************
	 * xd materialize and save included ontologies in the context, the "Virtual Ontology" should be saved
	 * @param contextPath
	 */
	public void materializeContext(/*String contextOutputPath*/File f, LocalOWLModule ont){
		
		System.out.println("Saving contextual integration...");
		try {
			//File newOwlFile = new File(contextOutputPath);
			contextOntologyManager.saveOntology(ont.getOwlOntology(), new OWLXMLOntologyFormat(), IRI.create(f));
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*********************
	 * xd generate narrow federation according to the filter.
	 * The spirit of this function is to iterate the contextMemberFilterCatalog, find all the entities with reserveOrNot=false then delete them all from the contextualIntegrationFiltered
	 */
	public void applyFilter(LocalOWLModule ont){
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		for(Entry<IRI, ContextMemberFilter> memberEntry : contextMemberFilterCatalog.entrySet()){
			for(Entry<IRI, Boolean> filterItemEntry : memberEntry.getValue().getClassFilter().entrySet()){
				if(!filterItemEntry.getValue().booleanValue()){//xd a reserveOrNot value found
					
			        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ont.getOwlOntology()));
			        
			        OWLClass target = df.getOWLClass(filterItemEntry.getKey());
			        target.accept(remover);
			        
			        ont.getOwlOntology().getOWLOntologyManager().applyChanges(remover.getChanges());
			        remover.reset();
				}
			}
			
			for(Entry<IRI, Boolean> filterItemEntry : memberEntry.getValue().getObjectPropertyFilter().entrySet()){
				if(!filterItemEntry.getValue().booleanValue()){//xd a reserveOrNot value found
					
			        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ont.getOwlOntology()));
			        
			        OWLObjectProperty target = df.getOWLObjectProperty(filterItemEntry.getKey());
			        target.accept(remover);
			        
			        ont.getOwlOntology().getOWLOntologyManager().applyChanges(remover.getChanges());
			        remover.reset();
				}
			}
			
			for(Entry<IRI, Boolean> filterItemEntry : memberEntry.getValue().getDataPropertyFilter().entrySet()){
				if(!filterItemEntry.getValue().booleanValue()){//xd a reserveOrNot value found
					
			        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ont.getOwlOntology()));
			        
			        OWLDataProperty target = df.getOWLDataProperty(filterItemEntry.getKey());
			        target.accept(remover);
			        
			        ont.getOwlOntology().getOWLOntologyManager().applyChanges(remover.getChanges());
			        remover.reset();
				}
			}
			
			for(Entry<IRI, Boolean> filterItemEntry : memberEntry.getValue().getIndividualFilter().entrySet()){
				if(!filterItemEntry.getValue().booleanValue()){//xd a reserveOrNot value found
					
			        OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(ont.getOwlOntology()));
			        
			        OWLNamedIndividual target = df.getOWLNamedIndividual(filterItemEntry.getKey());
			        target.accept(remover);
			        
			        ont.getOwlOntology().getOWLOntologyManager().applyChanges(remover.getChanges());
			        remover.reset();
				}
			}
		}
		
	}
	
	
	/**************
	 * xd after the ContextManager load (create) the context, parse.
	 * xd let's assume that, for now, the mainframe design of the xml file is stable
	 */
	public void parseContext(LocalOWLModule ont){
		
		ParserFactory pf = new ParserFactory();
		Parser parser;//xd don't forget to initialize this
		NodeList bundles = this.getContextDoc().getDocumentElement().getElementsByTagName("binaryMappingBundle");
		for(int j = 0; j < bundles.getLength(); j ++){
			NodeList bundleChildren = bundles.item(j).getChildNodes();
			for(int k = 0; k < bundleChildren.getLength(); k ++){
				if(bundleChildren.item(k).getNodeName() == "binaryMapping" && bundleChildren.item(k).hasChildNodes()){
					NodeList realMapping = bundleChildren.item(k).getChildNodes();//xd #text and '\n' may exist in this realMapping node list
					
					
					String type = ((Element)bundleChildren.item(k)).getAttribute("type");
					
					//parser = pf.getParser(type);//xd different type mapping syntax need different parser (old design)
					
					//System.out.println(parser.parse(realMapping, contextOntologyManager, contextualIntegration));//xd old design
					
					try{
						for(int q = 0; q < realMapping.getLength(); q ++){
							if(realMapping.item(q).getNodeType() != Node.COMMENT_NODE && realMapping.item(q).getNodeType() != Node.TEXT_NODE){//xd trim unnecessary comments and text
								//appending += nodeToString(realMapping.item(q));
								String nodeName = realMapping.item(q).getNodeName();
								Node currentNode = (Node) realMapping.item(q); 
								System.out.println("Mapping type: " + nodeName);
								
								MappingOWLAdapter moa = new MappingOWLAdapter(nodeName);
								currentNode = MappingOWLAdapter.trimNode(currentNode);//xd trim off unnecessary '\t', '\n' or #comment
								moa.parse(currentNode, contextOntologyManager, ont);
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					
				}
			}
		}
	}
	
	

	/**
	 * @return the contextRoot
	 */
	public String getContextRoot() {
		return contextRoot;
	}



	/**
	 * @param contextRoot the contextRoot to set
	 */
	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}



	/**
	 * @return the contextOntologyManager
	 */
	public OWLOntologyManager getContextOntologyManager() {
		return contextOntologyManager;
	}



	/**
	 * @param contextOntologyManager the contextOntologyManager to set
	 */
	public void setContextOntologyManager(OWLOntologyManager contextOntologyManager) {
		this.contextOntologyManager = contextOntologyManager;
	}



	/**
	 * @return the contextName
	 */
	public String getContextName() {
		return contextName;
	}



	/**
	 * @param contextName the contextName to set
	 */
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}



	/**
	 * @return the contextIRI
	 */
	public String getContextIRI() {
		return contextIRI;
	}



	/**
	 * @param contextIRI the contextIRI to set
	 */
	public void setContextIRI(String contextIRI) {
		this.contextIRI = contextIRI;
	}



	/**
	 * @return the contextlocalPath
	 */
	public String getContextlocalPath() {
		return contextlocalPath;
	}



	/**
	 * @param contextlocalPath the contextlocalPath to set
	 */
	public void setContextlocalPath(String contextlocalPath) {
		this.contextlocalPath = contextlocalPath;
	}



	/**
	 * @return the contextDoc
	 */
	public Document getContextDoc() {
		return contextDoc;
	}



	/**
	 * @param contextDoc the contextDoc to set
	 */
	public void setContextDoc(Document contextDoc) {
		this.contextDoc = contextDoc;
	}



	/**
	 * @return the contextMembers
	 */
	public Map<String, OWLOntology> getContextMembers() {
		return contextMembers;
	}



	/**
	 * @param contextMembers the contextMembers to set
	 */
	public void setContextMembers(Map<String, OWLOntology> contextMembers) {
		this.contextMembers = contextMembers;
	}



	/**
	 * @return the contextualIntegration
	 */
	public LocalOWLModule getContextualIntegration() {
		return contextualIntegration;
	}



	/**
	 * @param contextualIntegration the contextualIntegration to set
	 */
	public void setContextualIntegration(LocalOWLModule contextualIntegration) {
		this.contextualIntegration = contextualIntegration;
	}
	
	/**
	 * @return the properlyInitialised
	 */
	public boolean isProperlyInitialised() {
		return properlyInitialised;
	}



	/**
	 * @param properlyInitialised the properlyInitialised to set
	 */
	public void setProperlyInitialised(boolean properlyInitialised) {
		this.properlyInitialised = properlyInitialised;
	}





	/**
	 * @return the contextMemberFilterCatalog
	 */
	public Map<IRI, ContextMemberFilter> getContextMemberFilterCatalog() {
		return contextMemberFilterCatalog;
	}



	


	/**
	 * @return the contextualIntegrationFiltered
	 */
	public LocalOWLModule getContextualIntegrationFiltered() {
		return contextualIntegrationFiltered;
	}





	/**
	 * @param contextualIntegrationFiltered the contextualIntegrationFiltered to set
	 */
	public void setContextualIntegrationFiltered(LocalOWLModule contextualIntegrationFiltered) {
		this.contextualIntegrationFiltered = contextualIntegrationFiltered;
	}





	/**
	 * @param contextMemberFilterCatalog the contextMemberFilterCatalog to set
	 */
	public void setContextMemberFilterCatalog(Map<IRI, ContextMemberFilter> contextMemberFilterCatalog) {
		this.contextMemberFilterCatalog = contextMemberFilterCatalog;
	}
	
	
	public boolean getContextFilterResult(OWLOntology ont, IRI entity, Object o){
		//return getContextMemberFilterCatalog().get(ont.getOntologyID().getOntologyIRI().get()).getClassFilter().get(entity).booleanValue();
		if(o instanceof OWLClass){
			return getContextMemberFilterCatalog().get(ont.getOntologyID().getOntologyIRI().get()).getClassFilter().get(entity).booleanValue();
		}else if(o instanceof OWLObjectProperty){
			return getContextMemberFilterCatalog().get(ont.getOntologyID().getOntologyIRI().get()).getObjectPropertyFilter().get(entity).booleanValue();
		}else if(o instanceof OWLDataProperty){
			return getContextMemberFilterCatalog().get(ont.getOntologyID().getOntologyIRI().get()).getDataPropertyFilter().get(entity).booleanValue();
		}else if(o instanceof OWLNamedIndividual){
			return getContextMemberFilterCatalog().get(ont.getOntologyID().getOntologyIRI().get()).getIndividualFilter().get(entity).booleanValue();
		}else{
			System.out.println("No filter item found in the context! By default, return true");
			return true;
		}
	}
	
	/****************
	 * xd in this function, an entity item in the contextMemberFilterCatalog is updated
	 * @param iri
	 * @param type
	 * @param ont
	 */
	public void updateItemInContextMemberFilterCatalog(boolean newValue, IRI iri, ContextEntityType type, OWLOntology ont){
		System.out.println("Updating " + iri.toString() + " " + newValue);
		ContextMemberFilter targetFilter = contextMemberFilterCatalog.get(ont.getOntologyID().getOntologyIRI().get());
		if(targetFilter != null){//xd notice that sometimes an ontology could be removed from context already
			if(type == ContextEntityType.OWLClass){
				//System.out.println("targetFilter.getClassFilter().replace(iri, newValue);" + newValue);
				targetFilter.getClassFilter().replace(iri, newValue);
			}else if(type == ContextEntityType.OWLObjectProperty){
				//System.out.println("targetFilter.getObjectPropertyFilter().replace(iri, newValue);" + newValue);
				targetFilter.getObjectPropertyFilter().replace(iri, newValue);
			}else if(type == ContextEntityType.OWLDataProperty){
				//System.out.println("targetFilter.getDataPropertyFilter().replace(iri, newValue);" + newValue);
				targetFilter.getDataPropertyFilter().replace(iri, newValue);
			}else if(type == ContextEntityType.OWLNamedIndividual){
				//System.out.println("targetFilter.getIndividualFilter().replace(iri, newValue);" + newValue);
				targetFilter.getIndividualFilter().replace(iri, newValue);
				
				//xd check individualFilter 
	//			System.out.println("After individual filter is updated...");
	//			for(Entry<IRI, Boolean> e : targetFilter.getIndividualFilter().entrySet()){
	//				
	//				System.out.println(e.getKey() + "  " + e.getValue());
	//			}
	//			System.out.println();
				
			}
		}else{
			System.out.println("Can't find this ontology module...");
		}
	}
	
}
