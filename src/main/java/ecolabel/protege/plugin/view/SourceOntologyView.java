package ecolabel.protege.plugin.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.protege.editor.owl.ui.navigation.OWLEntityNavPanel;
import org.protege.editor.owl.ui.renderer.OWLOntologyCellRenderer;
import org.protege.editor.owl.ui.selector.OWLClassSelectorPanel;
import org.protege.editor.owl.ui.selector.OWLDataPropertySelectorPanel;
import org.protege.editor.owl.ui.selector.OWLIndividualSelectorPanel;
import org.protege.editor.owl.ui.selector.OWLObjectPropertySelectorPanel;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.protege.editor.owl.ui.view.cls.ToldOWLClassHierarchyViewComponent;
import org.protege.editor.owl.ui.view.individual.OWLIndividualsByTypeViewComponent;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import ecolabel.protege.plugin.component.CheckBoxNodeEditor;
import ecolabel.protege.plugin.component.CheckBoxNodeRenderer;
import ecolabel.protege.plugin.component.ContextEntityType;
import ecolabel.protege.plugin.component.EntityNameSelectable;
import ecolabel.protege.plugin.component.ReserveEntityTreeCellRenderer;
import ecolabel.protege.plugin.component.SelectDeselectAllControl;
import ecolabel.protege.plugin.component.SimpleTreeCellRenderer;
import ecolabel.protege.plugin.component.SimpleTreeNodeWrapper;
import ecolabel.protege.plugin.test.ClassHierarchyConsolePrinter;
import ecolabel.protege.plugin.transferHandler.TreeTransferHandler;
import edu.stanford.bmir.protege.examples.view.Metrics;
import org.protege.editor.core.ProtegeManager;
import org.protege.editor.core.ui.util.Resettable;
import org.protege.editor.core.ui.workspace.TabbedWorkspace;
import org.protege.editor.core.ui.workspace.WorkspaceTab;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;


public class SourceOntologyView extends /*AbstractOWLViewComponent*/JPanel {

    private static final Logger logger = LoggerFactory.getLogger(SourceOntologyView.class);

    OWLModelManager sourceMngr;
    DefaultPrefixManager prefixManager;
    private ContextSkeletonView contextSkeletonView;//xd top level handler
    
    private final JComboBox<OWLOntology> ontologiesList = new JComboBox<>();
    private OWLClassSelectorPanel classHierarchy;
    private OWLDataPropertySelectorPanel dataPropertyHierarchy;
    private OWLObjectPropertySelectorPanel objectPropertyHierarchy;
    private OWLIndividualSelectorPanel individualHierarchy;
    
    private DefaultMutableTreeNode thing;
    private DefaultMutableTreeNode rootObjectProperty;
    private DefaultMutableTreeNode rootDataProperty;
    private DefaultMutableTreeNode rootIndividual;
    private JTree classTree;
    private JTree objectPropertyTree;
    private JTree dataPropertyTree;
    private JTree individualTree;
    private JTabbedPane jtp;  
    private JScrollPane tabClass;
    private JScrollPane tabObjectProperty;
    private JScrollPane tabDataProperty;
    private JScrollPane tabIndividual;
    
    //private SelectDeselectAllControl selectControlClass;
    //private SelectDeselectAllControl selectControlObjectProperty;
    //private SelectDeselectAllControl selectControlDataProperty;
    //private SelectDeselectAllControl selectControlIndividual;
    //private JPanel 
    
    
    /************
     * xd when user operate on the checkBox located before each entity item, 
     * this item listener will communicate with context updating the filter
     */
	ItemListener checkBoxItemListener = new ItemListener(){
    public void itemStateChanged(ItemEvent itemEvent) {
    	
    	JCheckBox cb = (JCheckBox) itemEvent.getItem();
	    EntityNameSelectable ens = (EntityNameSelectable) cb.getParent();
	    //System.out.println("\n  Item State Changed! " + ens.entityId.getText());
	    if(ens.entityIRI != null && ens.entityId.getText() != null && ens.entityIRI.substring(ens.entityIRI.indexOf("#")+1).equals(ens.entityId.getText())){
		    if(contextSkeletonView.getContextEditorView().getContext().getContextMemberFilterCatalog() != null){
		    	IRI currentEntityIRI = IRI.create(ens.entityIRI);//xd get this entity's IRI
		  	    if (itemEvent.getStateChange() == ItemEvent.SELECTED){
		  	    	//JOptionPane.showMessageDialog(null, "Selected", "title", JOptionPane.ERROR_MESSAGE);
		  	  	    //System.out.println(currentEntityIRI + " " + ens.entityType.toString() + " " + ens.entityOntology.getOntologyID().getOntologyIRI().get());
		  	  	    //CheckBoxNodeEditor cbne = (CheckBoxNodeEditor) ens.getParent();
		  	    	
		  	    	contextSkeletonView.getContextEditorView().getContext().updateItemInContextMemberFilterCatalog(true, currentEntityIRI, ens.entityType, ens.entityOntology);
		  	    	
		  	    }else if(itemEvent.getStateChange() == ItemEvent.DESELECTED){
		  	    	
		  	    	contextSkeletonView.getContextEditorView().getContext().updateItemInContextMemberFilterCatalog(false, currentEntityIRI, ens.entityType, ens.entityOntology);
		  	    }
		    }else{
		  	    	//JOptionPane.showMessageDialog(null, "Not selected", "title", JOptionPane.ERROR_MESSAGE);
		  	    	
		  	}
		    updateEntityListHandler();
	    }else{
		  	//System.out.println("  entityIRI and entityId.getText() don't match, no update");
		    
	    }
	    
	  //updateEntityList();
    }};
    
    public SourceOntologyView(ContextSkeletonView csv){
    	setPreferredSize(new Dimension(300,1000));
    	this.contextSkeletonView = csv;
    }
    
    /********************
     * xd try to understand this kind of 
     */
    private OWLModelManagerListener modelListener = event -> {
        if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED 
        		|| event.getType() == EventType.ONTOLOGY_LOADED 
        		|| event.getType() == EventType.ONTOLOGY_RELOADED) {
            //recalculate();
        	rebuildOntologyDropDownWithoutSelection();
        }
    };
    
    protected void initialisePluginListener(){
    	contextSkeletonView.getOWLModelManager().addListener(modelListener);
    }
    
    protected void disposePluginListener(){
    	contextSkeletonView.getOWLModelManager().removeListener(modelListener);
    }
    
    
    
	protected void initialiseOWLView() throws Exception {
    	
    	
    	
    	sourceMngr = contextSkeletonView.getOWLModelManager();
    	initialisePluginListener();
        setLayout(new BorderLayout());
        
        createActiveOntologyPanel();
        
        jtp = new JTabbedPane();
        tabClass = new JScrollPane();
        //tabClass.setLayout(new BorderLayout());
        tabObjectProperty = new JScrollPane();
        //tabObjectProperty.setLayout(new BorderLayout());
        tabDataProperty = new JScrollPane();
        //tabDataProperty.setLayout(new BorderLayout());
        tabIndividual = new JScrollPane();
        //tabIndividual.setLayout(new BorderLayout());
        jtp.add("Classes", tabClass);
        jtp.add("O-Properties", tabObjectProperty);
        jtp.add("D-Properties", tabDataProperty);
        jtp.add("Individuals", tabIndividual);
        
        
        thing = new DefaultMutableTreeNode("owl:thing");
        rootObjectProperty = new DefaultMutableTreeNode("ObjectProperty");
        rootDataProperty = new DefaultMutableTreeNode("DataProperty");
        rootIndividual = new DefaultMutableTreeNode("Individual");
        
        classTree = new JTree(thing);
        objectPropertyTree = new JTree(rootObjectProperty);
        dataPropertyTree = new JTree(rootDataProperty);
        individualTree = new JTree(rootIndividual);
        
        createEntityList(classTree, thing, tabClass,ContextEntityType.OWLClass);
        createEntityList(objectPropertyTree, rootObjectProperty, tabObjectProperty, ContextEntityType.OWLObjectProperty);
        createEntityList(dataPropertyTree, rootDataProperty, tabDataProperty, ContextEntityType.OWLDataProperty);
        createEntityList(individualTree, rootIndividual, tabIndividual, ContextEntityType.OWLNamedIndividual);
        
        add(jtp,BorderLayout.CENTER);
        logger.info("Source Ontology View initialized");
    }

	

	protected void disposeOWLView() {
		disposePluginListener();//xd remember to remove action listener
		
		classHierarchy.dispose();
		dataPropertyHierarchy.dispose();
		objectPropertyHierarchy.dispose();
		individualHierarchy.dispose();
		logger.info("Source Ontology View disposed");
	}
	
	
	
	/************
	 * xd function createClassList() createObjectPropertyList() createIndividualList() createDataPropertyList() have similar behaviors, it's easier to merge them into one.
	 * @param tree
	 */
	private void createEntityList(JTree tree, DefaultMutableTreeNode treeRoot, JScrollPane scrollContainer, ContextEntityType entityType) throws Exception{
		
		switch (entityType){
		case OWLClass:
			logger.info("Tring to add class List tab...");
			break;
		case OWLObjectProperty:
			logger.info("Tring to add objectProperty List tab...");
			//treeRoot = new DefaultMutableTreeNode("ObjectProperty");
			break;
		case OWLDataProperty:
			logger.info("Tring to add dataProperty List tab...");
			//treeRoot = new DefaultMutableTreeNode("DataProperty");
			break;
		case OWLNamedIndividual:
			logger.info("Tring to add individual List tab...");
			//treeRoot = new DefaultMutableTreeNode("Individual");
			break;
		}
		
		//tree = new JTree(treeRoot);
		tree.setRootVisible(true);
		tree.setDragEnabled(true);//xd enable drag
		
		SelectDeselectAllControl selectControl = new SelectDeselectAllControl(tree, contextSkeletonView);//xd create a selectDeselectControl
		
		//SimpleTreeCellRenderer classListRenderer = new SimpleTreeCellRenderer();
		CheckBoxNodeRenderer entityListRenderer = new CheckBoxNodeRenderer(checkBoxItemListener);
		//CheckBoxNodeRenderer entityListRenderer = new CheckBoxNodeRenderer(contextSkeletonView);
		//ReserveEntityTreeCellRenderer classListRenderer = new ReserveEntityTreeCellRenderer();
		ToolTipManager.sharedInstance().registerComponent(tree);
		tree.setCellRenderer(entityListRenderer);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		tree.setCellEditor(new CheckBoxNodeEditor(tree,entityListRenderer));
		tree.setEditable(true);
		tree.setTransferHandler(new TreeTransferHandler());//xd drag & drop support
		//tabClass.add(classTree, BorderLayout.CENTER);
		scrollContainer.setViewportView(tree);
		scrollContainer.setColumnHeaderView(selectControl);//xd add the select/deselect control to the header
	}
	
	
	/**********************************
	 * xd add ontology selection bar
	 * 
	 * *******/
	private void createActiveOntologyPanel() {
        logger.info("Tring to add topBarPanel...");
        
        //JPanel topBarPanel = new JPanel(new GridBagLayout());
        //topBarPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 3, 10));

        JPanel topBarPanel = new JPanel(new BorderLayout());

        // Install the active ontology combo box
        ontologiesList.setToolTipText("Active Source Ontology");
        ontologiesList.setRenderer(new OWLOntologyCellRenderer(contextSkeletonView.getOWLEditorKit()));
        ontologiesList.setMinimumSize(new Dimension(100,30));
        rebuildOntologyDropDownWithoutSelection();

        topBarPanel.add(ontologiesList, BorderLayout.NORTH/*new GridBagConstraints(
                1, 0,
                1, 1,
                100, 0,
                GridBagConstraints.BASELINE,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0),
                0, 0
        )*/);

        ontologiesList.addActionListener(e -> {
        	System.out.println("Combox action:");
            OWLOntology ont = (OWLOntology) ontologiesList.getSelectedItem();
            if (ont != null) {
            	//xd sourceMngr.setActiveOntology(ont);//xd originally, this commend will reset the active ontology, here we want them only affect local class hierarchy
            	selectLocalActiveOntology(ont);
            	
            }
        });

        
        add(topBarPanel, BorderLayout.NORTH);
    }
	
	
//	private void rebuildOntologyDropDown() {
//        try {
//            TreeSet<OWLOntology> ts = new TreeSet<>(getOWLModelManager().getOWLObjectComparator());
//            ts.addAll(getOWLModelManager().getOntologies());
//            ontologiesList.setModel(new DefaultComboBoxModel<>(ts.toArray(new OWLOntology[ts.size()])));
//            ontologiesList.setSelectedItem(getOWLModelManager().getActiveOntology());
//        } catch (Exception e) {
//            logger.error("An error occurred whilst building the ontology list: {}", e);
//        }
//    }
	
	/*******************
	 * xd when both the SourceOntologyView and TargetOntologyView show the same ontology, when we operate on one of them, the other view should respond too.
	 * 
	 * 
	 */
	public void updateEntityListHandler(){
		//contextSkeletonView.getSourceOntologyView().updateEntityList();
		if(contextSkeletonView.getTargetOntologyView().getOntologiesList().getSelectedItem().equals(ontologiesList.getSelectedItem())){
			contextSkeletonView.getTargetOntologyView().updateEntityList();
		}
		
	}
	
	public void updateEntityList(){
		OWLOntology ont = (OWLOntology) ontologiesList.getSelectedItem();
        if (ont != null) {
        	selectLocalActiveOntology(ont);
        }
	}
	/*************************
	 * xd set local view to be the selected ontology (All the 4 tabs need updating)
	 * 
	 */
	private void selectLocalActiveOntology(OWLOntology ont) {

		
		IRI prefix = ont.getOntologyID().getOntologyIRI().get();
	    System.out.println("Current ontology prefix: " + prefix);
	    prefixManager = new DefaultPrefixManager();
	    prefixManager.setDefaultPrefix(prefix.toString() + "#");
	    
//	    TreeSet<OWLClass> classedSorted = new TreeSet<OWLClass>(ont.getClassesInSignature());
//	    TreeSet<OWLObjectProperty> objpropertiesSorted = new TreeSet<OWLObjectProperty>(ont.getObjectPropertiesInSignature());
//	    TreeSet<OWLDataProperty> sortedDataProperties = new TreeSet<OWLDataProperty>(ont.getDataPropertiesInSignature());
//	    TreeSet<OWLNamedIndividual> sortedIndividuals = new TreeSet<OWLNamedIndividual>(ont.getIndividualsInSignature());
	    
		updateClassList(ont);
		updateObjectPropertyList(ont);
		updateDataPropertyList(ont);
		updateIndividualList(ont);
		
		
		
		
	}

	private void rebuildOntologyDropDownWithoutSelection() {
        try {
            TreeSet<OWLOntology> ts = new TreeSet<>(contextSkeletonView.getOWLModelManager().getOWLObjectComparator());
            ts.addAll(contextSkeletonView.getOWLModelManager().getOntologies());
            ontologiesList.setModel(new DefaultComboBoxModel<>(ts.toArray(new OWLOntology[ts.size()])));
            //xd ontologiesList.setSelectedItem(getOWLModelManager().getActiveOntology());//xd don't need rebuild forcely, let the plug-in user to choose.
        } catch (Exception e) {
            logger.error("An error occurred whilst building the ontology list: {}", e);
        }
    }
	
	
	/**************
	 * 
	 */
	private void OWLModelManagerChanged(){
		WorkspaceTab tab = ((TabbedWorkspace) contextSkeletonView.getWorkspace()).getSelectedTab();
        ((Resettable) tab).reset();
        tab.validate();
	}
	
	
	private String getEntityLabel(IRI subject, OWLOntology ont){
		String currentEntityLabel = "";
		
		for(OWLAnnotationAssertionAxiom a : ont.getAnnotationAssertionAxioms(subject)) {
		    if(a.getProperty().isLabel()) {
		        if(a.getValue() instanceof OWLLiteral) {
		            OWLLiteral val = (OWLLiteral) a.getValue();
		            //System.out.println(c + " labelled " + val.getLiteral());
		            currentEntityLabel = " " + "\"" + val.getLiteral() + "\"";
		        }
		    }
		}
		return currentEntityLabel;
	}
	
	
	
	
	
	
	private void updateClassList(OWLOntology ont){
		//classTree.clearSelection();
		thing.removeAllChildren();
		classTree.updateUI();
		//Set<OWLClass> classes = ont.getClassesInSignature();
		
		TreeSet<OWLClass> classesSorted = new TreeSet<OWLClass>(ont.getClassesInSignature());//xd sort the set 
		
		/************
		 * xd Since we've introduced filter to ontology's entities, here we should check if the current ontology is in the context's member list.
		 * if current ontology is indeed in the member list, we should apply its filter, make the JTree component editable, 
		 * otherwise, we deselect all the entities, then make the JTree uneditable
		 */
		if(ontologyInContextMembers(ont)){//xd the opened ontology is in the context member list, its filter has to be applied
			Iterator<OWLClass> it = classesSorted.iterator();
			while (it.hasNext()) {
				OWLClass currentClass = it.next();
				
				DefaultMutableTreeNode dmt = new DefaultMutableTreeNode(
						new SimpleTreeNodeWrapper(prefixManager.getShortForm(currentClass).substring(1), 
								currentClass.getIRI().toString(),
								getEntityLabel(currentClass.getIRI(),ont),
								currentClass.getIRI().toString() + getEntityLabel(currentClass.getIRI(),ont),
								getContextFilterResult(ont, currentClass.getIRI(), currentClass),
								ContextEntityType.OWLClass,
								ont)
						);//xd the substring(1) is for deleting the unnecessary ':'
				
				thing.add(dmt);
				
			}
			classTree.setEditable(true);
			classTree.setDragEnabled(true);
		}else{//xd the opened ontology is not in the context member, we don't care about its filter, further more, we can not drag its entities
			Iterator<OWLClass> it = classesSorted.iterator();
			while (it.hasNext()) {
				OWLClass currentClass = it.next();
				
				DefaultMutableTreeNode dmt = new DefaultMutableTreeNode(
						new SimpleTreeNodeWrapper(prefixManager.getShortForm(currentClass).substring(1), 
								currentClass.getIRI().toString(),
								getEntityLabel(currentClass.getIRI(),ont),
								currentClass.getIRI().toString() + getEntityLabel(currentClass.getIRI(),ont),
								false,
								ContextEntityType.OWLClass,
								ont));//xd the substring(1) is for deleting the unnecessary ':'
				
				thing.add(dmt);
				
			}
			classTree.setEditable(false);
			classTree.setDragEnabled(false);
		}
		
		classTree.updateUI();
		
		
	}
	
	

	

	private void updateObjectPropertyList(OWLOntology ont){
		rootObjectProperty.removeAllChildren();
		objectPropertyTree.updateUI();
		//Set<OWLObjectProperty> objproperties = ont.getObjectPropertiesInSignature();
		TreeSet<OWLObjectProperty> objpropertiesSorted = new TreeSet<OWLObjectProperty>(ont.getObjectPropertiesInSignature());//xd sort the set 
		
		
		if(ontologyInContextMembers(ont)){
			Iterator<OWLObjectProperty> it = objpropertiesSorted.iterator();
			while (it.hasNext()) {
				OWLObjectProperty currentObjectProperty = it.next();
				DefaultMutableTreeNode dmt = new DefaultMutableTreeNode(
						new SimpleTreeNodeWrapper(prefixManager.getShortForm(currentObjectProperty).substring(1),
								currentObjectProperty.getIRI().toString(),
								getEntityLabel(currentObjectProperty.getIRI(),ont),
								currentObjectProperty.getIRI().toString() + getEntityLabel(currentObjectProperty.getIRI(),ont),
								getContextFilterResult(ont, currentObjectProperty.getIRI(), currentObjectProperty),
								ContextEntityType.OWLObjectProperty,
								ont));//xd the substring(1) is for deleting the unnecessary ':'
				
				rootObjectProperty.add(dmt);
			}
			objectPropertyTree.setEditable(true);
			objectPropertyTree.setDragEnabled(true);
		}else{
			Iterator<OWLObjectProperty> it = objpropertiesSorted.iterator();
			while (it.hasNext()) {
				OWLObjectProperty currentObjectProperty = it.next();
				DefaultMutableTreeNode dmt = new DefaultMutableTreeNode(
						new SimpleTreeNodeWrapper(prefixManager.getShortForm(currentObjectProperty).substring(1),
								currentObjectProperty.getIRI().toString(),
								getEntityLabel(currentObjectProperty.getIRI(),ont),
								currentObjectProperty.getIRI().toString() + getEntityLabel(currentObjectProperty.getIRI(),ont),
								false,
								ContextEntityType.OWLObjectProperty,
								ont));//xd the substring(1) is for deleting the unnecessary ':'
				
				rootObjectProperty.add(dmt);
			}
			objectPropertyTree.setEditable(false);
			objectPropertyTree.setDragEnabled(false);
		}
		
		
		objectPropertyTree.updateUI();
		
	}
	
	private void updateDataPropertyList(OWLOntology ont){
		rootDataProperty.removeAllChildren();
		dataPropertyTree.updateUI();
		//Set<OWLDataProperty> dataProperties = ont.getDataPropertiesInSignature();
		TreeSet<OWLDataProperty> sortedDataProperties = new TreeSet<OWLDataProperty>(ont.getDataPropertiesInSignature());//xd sort the set
		
		if(ontologyInContextMembers(ont)){
			Iterator<OWLDataProperty> it = sortedDataProperties.iterator();
			while (it.hasNext()) {
				OWLDataProperty currentDataProperty = it.next();
				DefaultMutableTreeNode dmt = new DefaultMutableTreeNode(
						new SimpleTreeNodeWrapper(prefixManager.getShortForm(currentDataProperty).substring(1), 
								currentDataProperty.getIRI().toString(),
								getEntityLabel(currentDataProperty.getIRI(),ont),
								currentDataProperty.getIRI().toString() + getEntityLabel(currentDataProperty.getIRI(),ont),
								getContextFilterResult(ont, currentDataProperty.getIRI(), currentDataProperty),
								ContextEntityType.OWLDataProperty,
								ont));//xd the substring(1) is for deleting the unnecessary ':'
				rootDataProperty.add(dmt);
				
			}
			dataPropertyTree.setEditable(true);
			dataPropertyTree.setDragEnabled(true);
		}else{
			Iterator<OWLDataProperty> it = sortedDataProperties.iterator();
			while (it.hasNext()) {
				OWLDataProperty currentDataProperty = it.next();
				DefaultMutableTreeNode dmt = new DefaultMutableTreeNode(
						new SimpleTreeNodeWrapper(prefixManager.getShortForm(currentDataProperty).substring(1), 
								currentDataProperty.getIRI().toString(),
								getEntityLabel(currentDataProperty.getIRI(),ont),
								currentDataProperty.getIRI().toString() + getEntityLabel(currentDataProperty.getIRI(),ont),
								false,
								ContextEntityType.OWLDataProperty,
								ont));//xd the substring(1) is for deleting the unnecessary ':'
				rootDataProperty.add(dmt);
				
			}
			dataPropertyTree.setEditable(false);
			dataPropertyTree.setDragEnabled(false);
		}
		
		
		
		dataPropertyTree.updateUI();
		
	}
	
	private void updateIndividualList(OWLOntology ont){
		rootIndividual.removeAllChildren();
		individualTree.updateUI();
		TreeSet<OWLNamedIndividual> sortedIndividuals = new TreeSet<OWLNamedIndividual>(ont.getIndividualsInSignature());
		
		if(ontologyInContextMembers(ont)){
			Iterator<OWLNamedIndividual> it = sortedIndividuals.iterator();
			while (it.hasNext()) {
				OWLNamedIndividual currentIndividual = it.next();
				DefaultMutableTreeNode dmt = new DefaultMutableTreeNode(
						new SimpleTreeNodeWrapper(prefixManager.getShortForm(currentIndividual).substring(1), 
								currentIndividual.getIRI().toString(),
								getEntityLabel(currentIndividual.getIRI(),ont),
								currentIndividual.getIRI().toString() + getEntityLabel(currentIndividual.getIRI(),ont),
								getContextFilterResult(ont, currentIndividual.getIRI(), currentIndividual),
								ContextEntityType.OWLNamedIndividual,
								ont));//xd the substring(1) is for deleting the unnecessary ':'
				rootIndividual.add(dmt);
				
			}
			individualTree.setEditable(true);
			individualTree.setDragEnabled(true);
		}else{
			Iterator<OWLNamedIndividual> it = sortedIndividuals.iterator();
			while (it.hasNext()) {
				OWLNamedIndividual currentIndividual = it.next();
				DefaultMutableTreeNode dmt = new DefaultMutableTreeNode(
						new SimpleTreeNodeWrapper(prefixManager.getShortForm(currentIndividual).substring(1), 
								currentIndividual.getIRI().toString(),
								getEntityLabel(currentIndividual.getIRI(),ont),
								currentIndividual.getIRI().toString() + getEntityLabel(currentIndividual.getIRI(),ont),
								false,
								ContextEntityType.OWLNamedIndividual,
								ont));//xd the substring(1) is for deleting the unnecessary ':'
				rootIndividual.add(dmt);
				
			}
			individualTree.setEditable(false);
			individualTree.setDragEnabled(false);
		}
		individualTree.updateUI();
		
	}
	
	
	/***************************
	 * xd here in this function, we assume that ontology's IRI is the unique id for each OWLOntology
	 * that's why we use containsKey() instead of containsValue();
	 * @return
	 */
	private boolean ontologyInContextMembers(OWLOntology ont) {
		if(contextSkeletonView.getContextEditorView().getContext() != null){
			return contextSkeletonView.getContextEditorView().getContext().getContextMembers().containsKey(ont.getOntologyID().getOntologyIRI().get().toString());
		}else{
			return false;
		}
		
	}
	
	/*************************
	 * xd go to context's filter to check if this entity should be reserved or not
	 * @return
	 */
	private boolean getContextFilterResult(OWLOntology ont, IRI entity, Object o) {
		return contextSkeletonView.getContextEditorView().getContext().getContextFilterResult(ont, entity, o);

		
	}

	/**
	 * @return
	 */
	public JComboBox<OWLOntology> getOntologiesList() {
		// TODO Auto-generated method stub
		return ontologiesList;
	}
	
	
	public void refreshOntologiesList(){
		ontologiesList.setSelectedItem(ontologiesList.getSelectedItem());
	}
}
