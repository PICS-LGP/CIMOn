package edu.stanford.bmir.protege.examples.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.navigation.OWLEntityNavPanel;
import org.protege.editor.owl.ui.renderer.OWLOntologyCellRenderer;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.protege.editor.owl.ui.view.cls.ToldOWLClassHierarchyViewComponent;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ecolabel.protege.plugin.view.SourceOntologyView;

public class ExampleViewComponent extends AbstractOWLViewComponent {

//    private static final Logger log = LoggerFactory.getLogger(ExampleViewComponent.class);
//
//    private Metrics metricsComponent;
//    
//
//    @Override
//    protected void initialiseOWLView() throws Exception {
//        setLayout(new BorderLayout());
//        metricsComponent = new Metrics(getOWLModelManager());
//        add(metricsComponent, BorderLayout.CENTER);
//        log.info("Example View Component initialized");
//    }
//
//	@Override
//	protected void disposeOWLView() {
//		metricsComponent.dispose();
//	}
	
	
	private static final Logger logger = LoggerFactory.getLogger(SourceOntologyView.class);

    private Metrics metricsComponent;
    private final JComboBox<OWLOntology> ontologiesList = new JComboBox<>();
    private final ToldOWLClassHierarchyViewComponent sourceClassHierarchy = new ToldOWLClassHierarchyViewComponent();

    @Override
	protected void initialiseOWLView() throws Exception {
        setLayout(new BorderLayout());
        
        
        //createActiveOntologyPanel();
        logger.info("Tring to add topBarPanel...");
        JPanel topBarPanel = new JPanel(new GridBagLayout());
        topBarPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 3, 10));
        topBarPanel.add(new OWLEntityNavPanel(getOWLEditorKit()),
                new GridBagConstraints(
                        0, 0,
                        1, 1,
                        0, 0,
                        GridBagConstraints.BASELINE, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 2),
                        0, 0));

        final OWLModelManager mngr = getOWLModelManager();

        // Install the active ontology combo box
        ontologiesList.setToolTipText("Active ontology");
        ontologiesList.setRenderer(new OWLOntologyCellRenderer(getOWLEditorKit()));
        rebuildOntologyDropDown();

        topBarPanel.add(ontologiesList, new GridBagConstraints(
                1, 0,
                1, 1,
                100, 0,
                GridBagConstraints.BASELINE,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0),
                0, 0
        ));

        ontologiesList.addActionListener(e -> {
            OWLOntology ont = (OWLOntology) ontologiesList.getSelectedItem();
            if (ont != null) {
                mngr.setActiveOntology(ont);
            }
        });

        add(topBarPanel, BorderLayout.NORTH);
        
        
        
        
        
        
        
        //createSourceOntologyClassHierarchy();
        logger.info("Tring to add sourceClassHierarchy...");
        add(sourceClassHierarchy, BorderLayout.SOUTH);
        
        
//        metricsComponent = new Metrics(getOWLModelManager());
//        add(metricsComponent, BorderLayout.CENTER);
        logger.info("Source Ontology View initialized");
    }

	@Override
	protected void disposeOWLView() {
		//metricsComponent.dispose();
		sourceClassHierarchy.dispose();
		logger.info("Source Ontology View disposed");
	}
	
	private void createSourceOntologyClassHierarchy(){
		add(sourceClassHierarchy, BorderLayout.SOUTH);
	}
	
	private void createActiveOntologyPanel() {

        JPanel topBarPanel = new JPanel(new GridBagLayout());
        topBarPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 3, 10));
        topBarPanel.add(new OWLEntityNavPanel(getOWLEditorKit()),
                new GridBagConstraints(
                        0, 0,
                        1, 1,
                        0, 0,
                        GridBagConstraints.BASELINE, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 2),
                        0, 0));

        final OWLModelManager mngr = getOWLModelManager();

        // Install the active ontology combo box
        ontologiesList.setToolTipText("Active ontology");
        ontologiesList.setRenderer(new OWLOntologyCellRenderer(getOWLEditorKit()));
        rebuildOntologyDropDown();

        topBarPanel.add(ontologiesList, new GridBagConstraints(
                1, 0,
                1, 1,
                100, 0,
                GridBagConstraints.BASELINE,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0),
                0, 0
        ));

        ontologiesList.addActionListener(e -> {
            OWLOntology ont = (OWLOntology) ontologiesList.getSelectedItem();
            if (ont != null) {
                mngr.setActiveOntology(ont);
            }
        });

        //JButton searchButton = new JButton("Search...");
        //searchButton.addActionListener(e -> showSearchDialog());

//        topBarPanel.add(searchButton, new GridBagConstraints(
//                2, 0,
//                1, 1,
//                0, 0,
//                GridBagConstraints.BASELINE,
//                GridBagConstraints.NONE,
//                new Insets(0, 2, 0, 2),
//                0, 0
//        ));

//        topBarPanel.add(backgroundTaskLabel,
//                new GridBagConstraints(
//                        3, 0,
//                        1, 1,
//                        0, 0,
//                        GridBagConstraints.EAST,
//                        GridBagConstraints.NONE,
//                        new Insets(0, 0, 0, 0),
//                        0, 0
//                ));
//        topBarPanel.add(errorNotificationLabel,
//                new GridBagConstraints(
//                        4, 0,
//                        1, 1,
//                        0, 0,
//                        GridBagConstraints.EAST,
//                        GridBagConstraints.NONE,
//                        new Insets(0, 0, 0, 0),
//                        0, 0
//                )
//        );

        add(topBarPanel, BorderLayout.NORTH);

        //updateTitleBar(); //xd I think this function updates the application title bar, our plug-in donesn't need this
    }
	
	
	private void rebuildOntologyDropDown() {
        try {
            TreeSet<OWLOntology> ts = new TreeSet<>(getOWLModelManager().getOWLObjectComparator());
            ts.addAll(getOWLModelManager().getOntologies());
            ontologiesList.setModel(new DefaultComboBoxModel<>(ts.toArray(new OWLOntology[ts.size()])));
            ontologiesList.setSelectedItem(getOWLModelManager().getActiveOntology());
        } catch (Exception e) {
            logger.error("An error occurred whilst building the ontology list: {}", e);
        }
    }
	
	
//	private void updateTitleBar() {
//        Frame f = ProtegeManager.getInstance().getFrame(this);
//        if (f != null) {
//            f.setTitle(getTitle());
//        }
//        ontologiesList.repaint();
//    }
}
