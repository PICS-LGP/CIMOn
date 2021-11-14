/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 23 mai 2017
 * 
 */
package ecolabel.protege.plugin.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.protege.editor.owl.ui.renderer.OWLOntologyCellRenderer;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ecolabel.knowledgebase.context.Context;
import ecolabel.knowledgebase.context.ContextManager;
import ecolabel.knowledgebase.context.history.BMBuilder;
import ecolabel.knowledgebase.context.history.BMBuilderFactory;
import ecolabel.knowledgebase.context.mapping.MappingOWLAdapter;
import ecolabel.knowledgebase.module.ContextMemberFilter;
import ecolabel.knowledgebase.module.LocalOWLModule;
import ecolabel.protege.plugin.component.BinaryMappingBundleComponent;
import ecolabel.protege.plugin.component.BinaryMappingComponent;
import ecolabel.protege.plugin.component.ContextChecker;
import ecolabel.protege.plugin.component.ContextHeaderComponent;
import ecolabel.protege.plugin.component.ContextVirtualConsole;



/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 23 mai 2017
 * 
 */
public class ContextEditorView extends /*AbstractOWLViewComponent*/JPanel{

	
	
	
	private Context context;//xd naturally, a ContextEditorView has a context. this variable is more like a cache
	//xd !!!notice that only context.getContextMembers() can be read/writen, the other part of this context is read only, once the context is filled from the file, il bouge pas. 
	//private List<Context> contexts;//xd could support multiple context in the future.
	
	private ContextSkeletonView contextSkeletonView;//xd top level handler
	
	private JMenuBar menuBar;//xd
	private JMenu mnFile;
	private JMenuItem mntmSave;//xd save current context file
	private JMenuItem mntmClose;//xd close current context file
	private JMenuItem mntmOpen;//xd open available context file
	private JMenuItem mntmNew;//xd create new context file
	
	private JMenu mnMapping;
	private JMenuItem mntmValidate;//xd preliminary validation & comleteness check, please refer to ContextChecker class
	private JMenuItem mntmNewMappingBundle;//xd create a new mapping bundle in current context file
	private JMenuItem mntmMergeMappingBundle;//xd if two or more mapping bundles have the same target ontology and source ontologies, merge them all.
	private JMenuItem mntmDeleteCurrentBundle;//xd remove current mapping bundle and its tab
	
	
	private JMenu mnContextFederation;
	private JMenuItem mntmFederationCheck;
	private JMenuItem mntmNarrowFederation;
	private JMenuItem mntmBroadFederation;
	
	/******************
	 * xd the main container for single context, the idea for now is no multiple context files opening support, 
	 * each time, there is only one context running, if there is such a multiple contexts request, 
	 * consider putting different contextEditor in different tabs.
	 */
	private JPanel panelContextEditor;//xd root panel container, it contains one ContextHeaderComponent and one JTabbedPane

	private ContextHeaderComponent panelContextHeader;//xd 
	private JTabbedPane panelMapping;//xd 
	
	private ContextVirtualConsole console;
	
	private ContextChecker checker;//xd validation before save 
	
	
	public ContextEditorView(ContextSkeletonView csv){
		//setPreferredSize(new Dimension(1000,1000));
    	this.contextSkeletonView = csv;
    }
	
	//private DefaultListModel cmListModel;
	//private DefaultComboBoxModel cmComboBoxModel;
	//private Map<String, LocalOWLModule> mapListModelTest;
	
	//ontologiesList.setRenderer(new OWLOntologyCellRenderer(getOWLEditorKit()));
	private OWLOntologyCellRenderer cellRenderer;
	

	/************************
	 * xd fill the components with context's content
	 * 
	 */
	protected void updateOWLView(){
		//cmListModel.removeAllElements();
		//cmComboBoxModel.removeAllElements();
		
	}
	
	
	/* (non-Javadoc)
	 * @see org.protege.editor.owl.ui.view.AbstractOWLViewComponent#initialiseOWLView()
	 * xd show necessary components on the GUI, but without content
	 */
	
	protected void initialiseOWLView() throws Exception {
		
		//xd try to communicate with source ontology view and target ontology view
		
		
		cellRenderer = new OWLOntologyCellRenderer(contextSkeletonView.getOWLEditorKit());//xd initialise cell renderer for JList and JComboBox
		initialiseMenuBar();
		
		initialiseConsole();
		
		initialiseChecker();
		
		//initialiseSingleContextEditor();
	}

	/**
	 * xd initialise context virtual console
	 */
	private void initialiseConsole() {
		console = new ContextVirtualConsole();
		add(console,BorderLayout.SOUTH);
		
	}
	
	/************
	 * xd initialise the context checker, it must be placed after initialiseConsole()
	 */
	private void initialiseChecker(){
		checker = new ContextChecker(console);
	}

	/*****************
	 * xd we do this after the context is loaded
	 */
	private void fillSingleContextEditor(){
		//xd initialise list model and combobox model for the context editor
		//cmListModel = new DefaultListModel();
		//cmComboBoxModel = new DefaultComboBoxModel();
		if(panelContextEditor != null){
			panelContextEditor.removeAll();
		}else{
			panelContextEditor = new JPanel();
		}
		panelContextEditor.setLayout(new BorderLayout());
		
		panelContextHeader = new ContextHeaderComponent();
		panelContextHeader.setParentContextEditorView(this);
		panelContextHeader.setJListAndJComboBoxRenderer(cellRenderer);//xd DIY the item renderer
		panelContextEditor.add(panelContextHeader,BorderLayout.NORTH);

		panelContextHeader.list.setModel(new DefaultComboBoxModel<>(context.getContextMembers().values().toArray()));//xd seems that JList can use JComboBox's model
		panelContextHeader.comboBox.setModel(new DefaultComboBoxModel<>(context.getContextMembers().values().toArray()));
		panelContextHeader.tfContextName.setText(context.getContextName());
		panelContextHeader.tfContextIRI.setText(context.getContextIRI());
		
		//panelContextHeader.list.setModel(cmListModel);
		//panelContextHeader.comboBox.setModel(cmComboBoxModel);
		if(context.getContextMembers().containsKey(context.getContextRoot())){//xd by default, the context root must be in the contextMemember
			panelContextHeader.comboBox.setSelectedItem(context.getContextMembers().get(context.getContextRoot()));
		}
		
		if(context.isProperlyInitialised()){//xd if the context has been initialised
			
			panelMapping = new JTabbedPane();
			
			NodeList bundles = context.getContextDoc().getDocumentElement().getElementsByTagName("binaryMappingBundle");
			for(int j = 0; j < bundles.getLength(); j ++){
				
				BinaryMappingBundleComponent currentBMB = new BinaryMappingBundleComponent();
				String currentBMPName = ((Element)bundles.item(j)).getAttribute("mappingName");
				panelMapping.addTab("Binary Mapping Bundle",currentBMB);
				currentBMB.setMappingBundleName(currentBMPName);
				currentBMB.tfBMBName.setText(currentBMPName);
				currentBMB.setJComboBoxRenderer(cellRenderer);//xd all the JList and JComboBox uses only one renderer
				currentBMB.cbSourceOntology.setModel(new DefaultComboBoxModel<>(context.getContextMembers().values().toArray()));
				currentBMB.cbTargetOntology.setModel(new DefaultComboBoxModel<>(context.getContextMembers().values().toArray()));
				currentBMB.cbSourceOntology.setSelectedItem(context.getContextMembers().get(((Element)bundles.item(j)).getAttribute("sourceOntologyIRI")));
				currentBMB.cbTargetOntology.setSelectedItem(context.getContextMembers().get(((Element)bundles.item(j)).getAttribute("targetOntologyIRI")));
				
				NodeList bundleChildren = bundles.item(j).getChildNodes();
				for(int k = 0; k < bundleChildren.getLength(); k ++){
					if(bundleChildren.item(k).getNodeName() == "binaryMapping" && bundleChildren.item(k).hasChildNodes()){
						NodeList realMapping = bundleChildren.item(k).getChildNodes();//xd #text and '\n' may exist in this realMapping node list
						
						String type = ((Element)bundleChildren.item(k)).getAttribute("type");
						
						BinaryMappingComponent BMC = new BinaryMappingComponent();
						
						try{
							for(int q = 0; q < realMapping.getLength(); q ++){
								if(realMapping.item(q).getNodeType() != Node.COMMENT_NODE && realMapping.item(q).getNodeType() != Node.TEXT_NODE){//xd trim unnecessary comments and text
									//appending += nodeToString(realMapping.item(q));
									String nodeName = realMapping.item(q).getNodeName();
									Node currentNode = (Node) realMapping.item(q); 
									System.out.println("Mapping type: " + nodeName);
									
									MappingOWLAdapter moa = new MappingOWLAdapter(nodeName);
									currentNode = MappingOWLAdapter.trimNode(currentNode);//xd trim off unnecessary '\t', '\n' or #comment
									moa.renderMapping(currentNode,BMC);
								}
							}
						}catch(Exception e){
							e.printStackTrace();
						}
									
						//BM.renderMapping(realMapping);//xd show the content in realMapping to the component(old design)
						currentBMB.binaryMappingContainer.add(BMC);
					}
				}
			}
			

		}else{//xd the context is not yet initialised (it's a new & empty context)
			panelContextHeader.tfContextName.setText("New Context");
			
			panelMapping = new JTabbedPane();
			
			
			BinaryMappingBundleComponent defaultBMB = new BinaryMappingBundleComponent();
			String currentBMPName = "New Mapping Bundle";
			panelMapping.addTab("Binary Mapping Bundle",defaultBMB);
			defaultBMB.setMappingBundleName("New Mapping Bundle");
			defaultBMB.tfBMBName.setText(currentBMPName);
			defaultBMB.setJComboBoxRenderer(cellRenderer);//xd all the JList and JComboBox uses only one renderer
			//defaultBMB.cbSourceOntology.setModel(new DefaultComboBoxModel<>(context.getContextMembers().values().toArray()));
			//defaultBMB.cbTargetOntology.setModel(new DefaultComboBoxModel<>(context.getContextMembers().values().toArray()));
//			defaultBMB.cbSourceOntology.setSelectedItem(-1);
//			defaultBMB.cbTargetOntology.setSelectedItem(-1);
				
			
			//xd it seems that one JTabbedPane is obliged to have at least one tab component...
		}
		
		contextSkeletonView.updateEntityListHandler();
		panelContextEditor.add(panelMapping, BorderLayout.CENTER);
		System.out.println("tabbed pane is added");
		add(panelContextEditor,BorderLayout.CENTER);
		
	}

	/* (non-Javadoc)
	 * @see org.protege.editor.owl.ui.view.AbstractOWLViewComponent#disposeOWLView()
	 */
	
	protected void disposeOWLView() {
		// TODO Auto-generated method stub
		
	}
	
	private void initialiseMenuBar(){
		setLayout(new BorderLayout());
		menuBar = new JMenuBar();
		
		mnFile = new JMenu("File");
		mnMapping = new JMenu("Mapping");
		mnContextFederation = new JMenu("Federation");
		mnMapping.setEnabled(false);//xd by default, the mapping functions under this menu is disabled
		mnContextFederation.setEnabled(false);
		
		menuBar.add(mnFile);
		menuBar.add(mnMapping);
		menuBar.add(mnContextFederation);
		
		mntmOpen = new JMenuItem("Open");
		
		/*********
		 * Open 
		 */
		mntmOpen.addActionListener(new ActionListener(){//xd http://blog.csdn.net/crystaldestiny/article/details/13833381 several method for adding actionListener
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog((Component) e.getSource());//xd open a file chooser
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            //This is where a real application would open the file.
		            System.out.println("Opening: " + file.getAbsolutePath());
		            int result = loadContextFile(file);//xd load the selected context file, seems that we can use main class's function
		            mntmClose.setEnabled(true);
		            mntmSave.setEnabled(true);
		            mnMapping.setEnabled(true);
		            mnContextFederation.setEnabled(true);
				} else {
		            //xd to do
		        }
				
			}
		});
		mnFile.add(mntmOpen);
		mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);
		mntmSave = new JMenuItem("Save");mntmSave.setEnabled(false);//xd by default the save menu item is disabled
		mnFile.add(mntmSave);
		mntmClose = new JMenuItem("Close");mntmClose.setEnabled(false);//xd by default the close menu item is disabled
		
		/*********
		 * New
		 */
		mntmNew.addActionListener(new ActionListener(){//xd set up a new context
			public void actionPerformed(ActionEvent e) {
				
				int result = newContextFile();
				mntmClose.setEnabled(true);
	            mntmSave.setEnabled(true);
			}
			
		});
		
		/********
		 * Close
		 */
		mntmClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				remove(panelContextEditor);//xd root panel container, it contains one ContextHeaderComponent and one JTabbedPane
				console.clear();//xd clear the console
				mnMapping.setEnabled(false);
				mnContextFederation.setEnabled(false);
				mntmClose.setEnabled(false);
	            mntmSave.setEnabled(false);
	            updateUI();
	            context = null;//xd
	            System.gc();//xd garbage collection
			}
			
		});
		
		/************
		 * Save 
		 * xd notice that a Context instance will be initialized as long as a new context created or loaded in the GUI. 
		 * However, this instance works only as a cache for the GUI initialization. When the GUI changes, this caching instance doesn't change correspondingly.
		 * (Only part of this Context instance, the contextMembers, which is used as the list and comboBox model, changes through GUI's operation)
		 * In fact, alternative solution or better solution is to have this caching context as well as the GUI content change together. When it comes to 'Save' the context, 
		 * since the caching instance and the GUI are already synchronized, all we need to do is to save the caching instance.
		 * 
		 * In this version of 'Save' function, we do the simplified version which means save on the basis of GUI instead of the caching instance. (Copy or snapshot of the content of the GUI)
		 *  
		 */
		mntmSave.addActionListener(new ActionListener(){//xd save the current context, core function of this plug-in
			public void actionPerformed(ActionEvent e) {
				if(validateContextGUI() == 0){//xd everything seems ok for the content of the context, no contradiction exists 
					saveContextGUI();
				}else{
					//xd to do: show contradiction or error message about the context, guide user how to modify.
				}
				
				
			}
		});
		
		
		
		mnFile.add(mntmClose);
		
		mntmNewMappingBundle = new JMenuItem("New mapping bundle");
		mnMapping.add(mntmNewMappingBundle);
		mntmMergeMappingBundle = new JMenuItem("Merge overlapped mapping bundles");
		mnMapping.add(mntmMergeMappingBundle);
		mntmDeleteCurrentBundle = new JMenuItem("Delete current mapping bundle");
		mnMapping.add(mntmDeleteCurrentBundle);
		mntmValidate = new JMenuItem("Validate all mappings");
		mnMapping.addSeparator();
		mnMapping.add(mntmValidate);
		
		mntmNewMappingBundle.addActionListener(new ActionListener(){//xd add a new & empty mapping bundle 
			public void actionPerformed(ActionEvent e) {
				
				BinaryMappingBundleComponent newBMB = new BinaryMappingBundleComponent();
				
				newBMB.setMappingBundleName("New Mapping Bundle");//xd by default the new bundle name
				newBMB.tfBMBName.setText("New Mapping Bundle");//xd by default the bundle name is "New Mapping Bundle"
				newBMB.setJComboBoxRenderer(cellRenderer);//xd all the JList and JComboBox uses only one renderer
				newBMB.cbSourceOntology.setModel(new DefaultComboBoxModel<>(context.getContextMembers().values().toArray()));
				newBMB.cbTargetOntology.setModel(new DefaultComboBoxModel<>(context.getContextMembers().values().toArray()));
				newBMB.cbSourceOntology.setSelectedIndex(-1);//xd 
				newBMB.cbTargetOntology.setSelectedIndex(-1);//xd by default, null item is chosen
				
				panelMapping.add("Binary Mapping Bundle",newBMB);
			}
		});
		
		mntmDeleteCurrentBundle.addActionListener(new ActionListener(){//xd delete current mapping bundle
			public void actionPerformed(ActionEvent e) {
				if(panelMapping.getTabCount() > 1){//xd if there is some tab is selected
					panelMapping.remove(panelMapping.getSelectedIndex());//xd remove current selected tab
				}else if(panelMapping.getTabCount() == 1){//xd there is only one bundle tab in the container, we must keep and reset it 
					((BinaryMappingBundleComponent) panelMapping.getSelectedComponent()).cbSourceOntology.setSelectedIndex(-1);
					((BinaryMappingBundleComponent) panelMapping.getSelectedComponent()).cbTargetOntology.setSelectedIndex(-1);
					((BinaryMappingBundleComponent) panelMapping.getSelectedComponent()).mappingBundleName = "New Mapping Bundle";
					((BinaryMappingBundleComponent) panelMapping.getSelectedComponent()).tfBMBName.setText("New Mapping Bundle");
				}
				
			}
		});
		
		
		
		mntmFederationCheck = new JMenuItem("FederationCheck");
		mnContextFederation.add(mntmFederationCheck);
		mntmNarrowFederation = new JMenuItem("Generate narrow federation");
		mnContextFederation.add(mntmNarrowFederation);
		mntmBroadFederation = new JMenuItem("Generate broad federation");
		mnContextFederation.add(mntmBroadFederation);
		
		
		/***************
		 * xd start the reasoner to check the big integration. Don't forget to consider the mapping and unwanted entities
		 */
		mntmFederationCheck.addActionListener(new ActionListener(){//xd start reasoner to check big integration
			public void actionPerformed(ActionEvent e) {
				context.doContextReasoning(context.getContextualIntegration());
				context.doContextReasoning(context.getContextualIntegrationFiltered());
			}
		});
		
		/************
		 * xd the small integration is the hard one, because we need to trim unwanted entities and the axioms. At the same time, we have to make sure that the mappings are correct.
		 */
		mntmNarrowFederation.addActionListener(new ActionListener(){//xd generate small integration
			public void actionPerformed(ActionEvent e) {
				context.parseContext(context.getContextualIntegrationFiltered());
				context.applyFilter(context.getContextualIntegrationFiltered());//xd main difference between narrow federation and broad federation. In this federation, we'll trim off unwanted entities
				if(context.doContextReasoning(context.getContextualIntegrationFiltered())){
					JFileChooser chooser = new JFileChooser();
				    int retrival = chooser.showSaveDialog(null);
				    if (retrival == JFileChooser.APPROVE_OPTION) {
				    	context.materializeContext(chooser.getSelectedFile(), context.getContextualIntegrationFiltered());
			    	}
			    }else{
			    	console.printError("Narrow federation (small integration) is not logical correct, please re-check your ontology and mapping editing.");
			    }
			}
		});
		
		/************
		 * xd the big integration with mapping is relative easy, we've implemented this
		 */
		mntmBroadFederation.addActionListener(new ActionListener(){//xd generate big integration
			public void actionPerformed(ActionEvent e) {
				context.parseContext(context.getContextualIntegration());
				if(context.doContextReasoning(context.getContextualIntegration())){
					JFileChooser chooser = new JFileChooser();
				    int retrival = chooser.showSaveDialog(null);
				    if (retrival == JFileChooser.APPROVE_OPTION) {
				    	context.materializeContext(chooser.getSelectedFile(), context.getContextualIntegration());
				    }
				}else{
					console.printError("Broad federation (big integration) is not logical correct, please re-check your ontology and mapping editing.");
				}
			}
		});
		
		
		
		add(menuBar, BorderLayout.NORTH);
		
		mntmValidate.addActionListener(new ActionListener(){//xd start the ContextChecker to validate the mapping content
			public void actionPerformed(ActionEvent e) {
				checker.checkContext((ContextEditorView)menuBar.getParent());
			}
		});
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	private int loadContextFile(File file) {
		context = new ContextManager().loadContext(file.getAbsolutePath());
		fillSingleContextEditor();
		System.out.println("Context loaded in the editor view");
		//xd to do: should check if the context file is normally loaded
		
		
		return 0;
	}
	
	
	/***********
	 * xd 
	 * @return
	 */
	private int newContextFile(){
		context = new ContextManager().getNewContext();
		fillSingleContextEditor();
		System.out.println("A new & empty context in set up in editor view");
		
		mnMapping.setEnabled(true);
		return 0;
	}
	
	/**
	 * @return the cellRenderer
	 */
	public OWLOntologyCellRenderer getCellRenderer() {
		return cellRenderer;
	}


	/**
	 * @param cellRenderer the cellRenderer to set
	 */
	public void setCellRenderer(OWLOntologyCellRenderer cellRenderer) {
		this.cellRenderer = cellRenderer;
	}


	/****************************
	 * 
	 * @param cmlist
	 */
	public void addContextMemembers(List cmlist) {
		ArrayList<OWLOntology> selected = new ArrayList(cmlist);//xd cast the Object to OWLOntology
		for(OWLOntology i: selected){
			if(context.getContextMembers().containsValue(i)){
				//xd this selected ontology is already in the context memembers, do nothing
			}else{
				context.getContextMembers().put(i.getOntologyID().getOntologyIRI().get().toString(), i);
				
				ContextMemberFilter cmb = new ContextMemberFilter(i);//xd the same time refreshing the context members, should refresh contextFilter
				context.getContextMemberFilterCatalog().put(i.getOntologyID().getOntologyIRI().get(), cmb);
			}
			
		}
		refreshContextMemembers();
		
	}
	
	/****************************
	 * notice that here is a possibility that we could remove a context memember that already exist in the bundle, 
	 * alert message should be shown to the user, then proceed the update 
	 * @param cmlist
	 */
	public void removeContextMemembers(List cmlist){
		ArrayList<OWLOntology> selected = new ArrayList(cmlist);//xd cast the Object to OWLOntology
		for(OWLOntology i: selected){
			if(context.getContextMembers().containsValue(i)){
				context.getContextMembers().remove(i.getOntologyID().getOntologyIRI().get().toString());
				
				context.getContextMemberFilterCatalog().remove(i.getOntologyID().getOntologyIRI().get());//xd the same time refreshing context members, should refresh contextFilter
				
			}			
		}
		refreshContextMemembers();
		
	}
	
	
	/************
	 * every time the context members are changed, the context member related component must be updated
	 * if any source or target member of any binary mapping bundle is removed, all the mappings under this bundle should be removed too
	 * if there is any change to the DefaultComboBoxModel, before update the model, should remember the old selection
	 */
	public void refreshContextMemembers(){
		OWLOntology oldRoot = (OWLOntology) panelContextHeader.comboBox.getSelectedItem();
		panelContextHeader.comboBox.setModel(new DefaultComboBoxModel<>(context.getContextMembers().values().toArray()));
		if(context.getContextMembers().containsValue(oldRoot)){//xd the old root element is still in the context
			panelContextHeader.comboBox.setSelectedItem(oldRoot);//xd remember the old root choice
		}else{
			panelContextHeader.comboBox.setSelectedIndex(-1);//xd if the old root element is missing, set nobody
		}
		panelContextHeader.list.setModel(new DefaultComboBoxModel<>(context.getContextMembers().values().toArray()));
		
		
		for(Component bmbc: panelMapping.getComponents()){
			OWLOntology sourceOnto = (OWLOntology)((BinaryMappingBundleComponent) bmbc).cbSourceOntology.getSelectedItem();
			OWLOntology targetOnto = (OWLOntology)((BinaryMappingBundleComponent) bmbc).cbTargetOntology.getSelectedItem();
			((BinaryMappingBundleComponent) bmbc).cbSourceOntology.setModel(new DefaultComboBoxModel<>(context.getContextMembers().values().toArray()));
			((BinaryMappingBundleComponent) bmbc).cbTargetOntology.setModel(new DefaultComboBoxModel<>(context.getContextMembers().values().toArray()));
			if(context.getContextMembers().containsValue(sourceOnto) && context.getContextMembers().containsValue(targetOnto)){
				//xd if the mapping bundle component's source and target ontology are still in the context memembers, this context is safe, update the model
				
				((BinaryMappingBundleComponent) bmbc).cbSourceOntology.setSelectedItem(sourceOnto);
				
				((BinaryMappingBundleComponent) bmbc).cbTargetOntology.setSelectedItem(targetOnto);
			}else{
				if(panelMapping.getTabCount() == 1){//xd this is the last bundle tab in the context, we must keep it and reset it
					((BinaryMappingBundleComponent) bmbc).cbSourceOntology.setSelectedIndex(-1);
					((BinaryMappingBundleComponent) bmbc).cbTargetOntology.setSelectedIndex(-1);
					((BinaryMappingBundleComponent) bmbc).mappingBundleName = "New Mapping Bundle";
					((BinaryMappingBundleComponent) bmbc).tfBMBName.setText("New Mapping Bundle");
				}else{
					panelMapping.remove(bmbc);//xd either source or target ontology is removed, this bundle doesn't exist any more
				}
				
				
				/********************
				 * xd an interesting thing was found:
				 * JTabbedPane should have at least one tab component left, otherwise we'll have exception like this:
				 * java.lang.ArrayIndexOutOfBoundsException: -1
					at javax.swing.plaf.basic.BasicTabbedPaneUI.getTabBounds(Unknown Source) ~[na:1.8.0_121]
					at javax.swing.plaf.basic.BasicTabbedPaneUI.getTabBounds(Unknown Source) ~[na:1.8.0_121]
					at org.protege.editor.core.ui.tabbedpane.CloseableTabbedPaneUI.getTabBounds(CloseableTabbedPaneUI.java:266) ~[na:na]
					at org.protege.editor.core.ui.tabbedpane.CloseableTabbedPaneUI.paintContentBorderTopEdge(CloseableTabbedPaneUI.java:397) ~[na:na]...
					So, we should keep at least one tab left
				 */
				
			}
		}
	}
	
	
	//xd each time after ontology member is added or removed, should refresh all the filters in the context
	public void refreshContextFilters(){
		
	}
	
	/**********
	 * xd validate and check the consistency & completeness of the context
	 * @return
	 */
	public int validateContextGUI(){
		//xd to do:
		return 0;
	}
	
	
	public void fillEntityFilter(Document doc, Element filter, Map<IRI, Boolean> map){
		
		for(Entry<IRI, Boolean> entry : map.entrySet()){
			Element filterItem = doc.createElement("filterItem");
			filterItem.setAttribute("entityIRI", entry.getKey().toString());
			filterItem.setAttribute("reserveOrNot", entry.getValue().toString());
			filter.appendChild(filterItem);
		}
	}
	
	/************
	 * xd save the current GUI editing context to file system 
	 */
	public void saveContextGUI(){
		//xd we assume that the GUI content is complete and valid
		//panelContextHeader;//xd ContextHeaderComponent
		//panelMapping;//xd JTabbedPane
		
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			/********
			 *xd elements unique 
			 */
			Element eleRoot = doc.createElement("context");
			Element eleContextName = doc.createElement("contextName");
			Element eleContextMembers = doc.createElement("contextMembers");
			Element eleContextRoot = doc.createElement("contextRoot");
			Element eleContextFilter = doc.createElement("contextFilter");
			
			doc.appendChild(eleRoot);
			eleRoot.appendChild(eleContextName);
			eleRoot.appendChild(eleContextMembers);
			eleRoot.appendChild(eleContextRoot);
			eleRoot.appendChild(eleContextFilter);
			
			eleContextName.setAttribute("IRI", panelContextHeader.tfContextIRI.getText());
			eleContextName.appendChild(doc.createTextNode(panelContextHeader.tfContextName.getText()));
			
			for(Map.Entry<String, OWLOntology> entry : context.getContextMembers().entrySet()){
				Element eleOwlOntology = doc.createElement("owlOntology");
				eleOwlOntology.setAttribute("IRI", entry.getValue().getOntologyID().getOntologyIRI().get().toString());
				eleOwlOntology.setAttribute("localPath", entry.getValue().getOWLOntologyManager().getOntologyDocumentIRI(entry.getValue()).toString().substring(6));//xd delete the sub string 'file:/'
				eleContextMembers.appendChild(eleOwlOntology);
			}
			OWLOntology currentRoot = (OWLOntology) panelContextHeader.comboBox.getSelectedItem();
			eleContextRoot.setAttribute("IRI", currentRoot.getOntologyID().getOntologyIRI().get().toString());
			
			for(Entry<IRI, ContextMemberFilter> entry : getContext().getContextMemberFilterCatalog().entrySet())
			{
			    Element eleContextMemberFilter = doc.createElement("contextMemberFilter");
			    eleContextMemberFilter.setAttribute("IRI", entry.getKey().toString());
			    
			    Element eleClassFilter = doc.createElement("classFilter");
			    Element eleObjectPropertyFilter = doc.createElement("objectPropertyFilter");
			    Element eleDataPropertyFilter = doc.createElement("individualFilter");
			    Element eleIndividualFilter = doc.createElement("individualFilter");
			    
			    eleClassFilter.setAttribute("mode", entry.getValue().getFilterMode(entry.getValue().getClassFilter()));
			    eleObjectPropertyFilter.setAttribute("mode", entry.getValue().getFilterMode(entry.getValue().getObjectPropertyFilter()));
			    eleDataPropertyFilter.setAttribute("mode", entry.getValue().getFilterMode(entry.getValue().getDataPropertyFilter()));
			    eleIndividualFilter.setAttribute("mode", entry.getValue().getFilterMode(entry.getValue().getIndividualFilter()));
			    
			    //xd only part mode call function fillEntityFilter
			    if(eleClassFilter.getAttribute("mode").equals("part")){
			    	fillEntityFilter(doc, eleClassFilter, entry.getValue().getClassFilter());
			    }
			    if(eleObjectPropertyFilter.getAttribute("mode").equals("part")){
			    	fillEntityFilter(doc,eleObjectPropertyFilter,entry.getValue().getObjectPropertyFilter());
			    }
			    if(eleDataPropertyFilter.getAttribute("mode").equals("part")){
			    	fillEntityFilter(doc,eleDataPropertyFilter,entry.getValue().getDataPropertyFilter());
			    }
			    if(eleIndividualFilter.getAttribute("mode").equals("part")){
			    	fillEntityFilter(doc,eleIndividualFilter,entry.getValue().getIndividualFilter());
			    }
			    
			    
			    eleContextMemberFilter.appendChild(eleClassFilter);
			    eleContextMemberFilter.appendChild(eleObjectPropertyFilter);
			    eleContextMemberFilter.appendChild(eleDataPropertyFilter);
			    eleContextMemberFilter.appendChild(eleIndividualFilter);
			    
			    eleContextFilter.appendChild(eleContextMemberFilter);
			}
			
			
			for(Component c: panelMapping.getComponents()){
				BinaryMappingBundleComponent bmbc = (BinaryMappingBundleComponent) c;
				
				Element eleBMB = doc.createElement("binaryMappingBundle");
				eleBMB.setAttribute("sourceOntologyIRI", ((OWLOntology)bmbc.cbSourceOntology.getSelectedItem()).getOntologyID().getOntologyIRI().get().toString());
				eleBMB.setAttribute("targetOntologyIRI", ((OWLOntology)bmbc.cbTargetOntology.getSelectedItem()).getOntologyID().getOntologyIRI().get().toString());
				eleBMB.setAttribute("mappingName",bmbc.tfBMBName.getText());
				
				for(Component mapping: bmbc.binaryMappingContainer.getComponents()){
					BinaryMappingComponent bmc = (BinaryMappingComponent) mapping;
					Element eleBM = doc.createElement("binaryMapping");
					eleBM.setAttribute("type", "OWL/XML");//xd by default the element type is OWL/XML
					
					//BMBuilder mappingBuider = mappingBuiderFactory.getBuilder("OWL/XML");//xd by default the element type is OWL/XML (old design)
					//Element realMapping = mappingBuider.buildRealBinaryMapping(bmc,doc);//xd normally lbMappingType and mappingType are synchronized (old design)
					
					MappingOWLAdapter moa = new MappingOWLAdapter(bmc.mappingType.getSelectedItem().toString());
					Element realMapping = moa.buildRealBinaryMapping(bmc, doc);
					
					eleBM.appendChild(realMapping);
					eleBMB.appendChild(eleBM);
				}
				
				eleRoot.appendChild(eleBMB);
			}
			
			JFileChooser chooser = new JFileChooser();
		    //chooser.setCurrentDirectory(new File("/home/me/Documents"));
		    int retrival = chooser.showSaveDialog(null);
		    if (retrival == JFileChooser.APPROVE_OPTION) {
		    	TransformerFactory transformerFactory =
			    TransformerFactory.newInstance();
			    Transformer transformer = transformerFactory.newTransformer();
	        	DOMSource source = new DOMSource(doc);
	        	StreamResult result = new StreamResult(chooser.getSelectedFile());
	        	transformer.transform(source, result);
		    }
		    
			
		}catch(Exception e) {
	         e.printStackTrace();
		}
		
	    
	    
	    
		
		
		
	}


	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}


	/**
	 * @param context the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}


	/**
	 * @return the menuBar
	 */
	public JMenuBar getMenuBar() {
		return menuBar;
	}


	/**
	 * @param menuBar the menuBar to set
	 */
	public void setMenuBar(JMenuBar menuBar) {
		this.menuBar = menuBar;
	}


	/**
	 * @return the mnFile
	 */
	public JMenu getMnFile() {
		return mnFile;
	}


	/**
	 * @param mnFile the mnFile to set
	 */
	public void setMnFile(JMenu mnFile) {
		this.mnFile = mnFile;
	}


	/**
	 * @return the mntmSave
	 */
	public JMenuItem getMntmSave() {
		return mntmSave;
	}


	/**
	 * @param mntmSave the mntmSave to set
	 */
	public void setMntmSave(JMenuItem mntmSave) {
		this.mntmSave = mntmSave;
	}


	/**
	 * @return the mntmClose
	 */
	public JMenuItem getMntmClose() {
		return mntmClose;
	}


	/**
	 * @param mntmClose the mntmClose to set
	 */
	public void setMntmClose(JMenuItem mntmClose) {
		this.mntmClose = mntmClose;
	}


	/**
	 * @return the mntmOpen
	 */
	public JMenuItem getMntmOpen() {
		return mntmOpen;
	}


	/**
	 * @param mntmOpen the mntmOpen to set
	 */
	public void setMntmOpen(JMenuItem mntmOpen) {
		this.mntmOpen = mntmOpen;
	}


	/**
	 * @return the mntmNew
	 */
	public JMenuItem getMntmNew() {
		return mntmNew;
	}


	/**
	 * @param mntmNew the mntmNew to set
	 */
	public void setMntmNew(JMenuItem mntmNew) {
		this.mntmNew = mntmNew;
	}


	/**
	 * @return the mnMapping
	 */
	public JMenu getMnMapping() {
		return mnMapping;
	}


	/**
	 * @param mnMapping the mnMapping to set
	 */
	public void setMnMapping(JMenu mnMapping) {
		this.mnMapping = mnMapping;
	}


	/**
	 * @return the mntmNewMappingBundle
	 */
	public JMenuItem getMntmNewMappingBundle() {
		return mntmNewMappingBundle;
	}


	/**
	 * @param mntmNewMappingBundle the mntmNewMappingBundle to set
	 */
	public void setMntmNewMappingBundle(JMenuItem mntmNewMappingBundle) {
		this.mntmNewMappingBundle = mntmNewMappingBundle;
	}


	/**
	 * @return the mntmMergeMappingBundle
	 */
	public JMenuItem getMntmMergeMappingBundle() {
		return mntmMergeMappingBundle;
	}


	/**
	 * @param mntmMergeMappingBundle the mntmMergeMappingBundle to set
	 */
	public void setMntmMergeMappingBundle(JMenuItem mntmMergeMappingBundle) {
		this.mntmMergeMappingBundle = mntmMergeMappingBundle;
	}


	/**
	 * @return the mntmDeleteCurrentBundle
	 */
	public JMenuItem getMntmDeleteCurrentBundle() {
		return mntmDeleteCurrentBundle;
	}


	/**
	 * @param mntmDeleteCurrentBundle the mntmDeleteCurrentBundle to set
	 */
	public void setMntmDeleteCurrentBundle(JMenuItem mntmDeleteCurrentBundle) {
		this.mntmDeleteCurrentBundle = mntmDeleteCurrentBundle;
	}


	/**
	 * @return the panelContextEditor
	 */
	public JPanel getPanelContextEditor() {
		return panelContextEditor;
	}


	/**
	 * @param panelContextEditor the panelContextEditor to set
	 */
	public void setPanelContextEditor(JPanel panelContextEditor) {
		this.panelContextEditor = panelContextEditor;
	}


	/**
	 * @return the panelContextHeader
	 */
	public ContextHeaderComponent getPanelContextHeader() {
		return panelContextHeader;
	}


	/**
	 * @param panelContextHeader the panelContextHeader to set
	 */
	public void setPanelContextHeader(ContextHeaderComponent panelContextHeader) {
		this.panelContextHeader = panelContextHeader;
	}


	/**
	 * @return the panelMapping
	 */
	public JTabbedPane getPanelMapping() {
		return panelMapping;
	}


	/**
	 * @param panelMapping the panelMapping to set
	 */
	public void setPanelMapping(JTabbedPane panelMapping) {
		this.panelMapping = panelMapping;
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


	/**
	 * @return the contextSkeletonView
	 */
	public ContextSkeletonView getContextSkeletonView() {
		return contextSkeletonView;
	}


	/**
	 * @param contextSkeletonView the contextSkeletonView to set
	 */
	public void setContextSkeletonView(ContextSkeletonView contextSkeletonView) {
		this.contextSkeletonView = contextSkeletonView;
	}


	

	
}
