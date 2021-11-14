/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 29 mai 2017
 * 
 */
package ecolabel.protege.plugin.component;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.border.BevelBorder;

import org.protege.editor.owl.ui.renderer.OWLOntologyCellRenderer;
import org.semanticweb.owlapi.model.OWLOntology;

import ecolabel.knowledgebase.module.LocalOWLModule;
import ecolabel.protege.plugin.view.ContextEditorView;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.util.TreeSet;
import java.awt.event.ActionEvent;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 29 mai 2017
 * 
 */
public class ContextHeaderComponent extends JPanel {
	public JTextField tfContextName;//xd context name
	private ContextEditorView parentContextEditorView;

	


	public JList list;
	//public DefaultListModel contextMemembersDlm;//xd abandon local list model, use a global arraylist model instead.
	public JButton btnAdd;
	public JButton btnRemove;
	public JComboBox comboBox;
	public ContextMememberChooser cmc;//xd a JList component used for new context component selection
	public JTextField tfContextIRI;//xd context IRI
	/**
	 * Create the panel.
	 */
	public ContextHeaderComponent() {
		
		setSize(new Dimension(1000, 170));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblContextIri = new JLabel("Context IRI:");
		lblContextIri.setPreferredSize(new Dimension(160, 20));
		panel.add(lblContextIri);
		
		tfContextIRI = new JTextField();
		tfContextIRI.setPreferredSize(new Dimension(450, 26));
		panel.add(tfContextIRI);
		tfContextIRI.setColumns(10);
		JLabel lblNewLabel = new JLabel("Context Name: ");lblNewLabel.setPreferredSize(new Dimension(130, 30));
		panel.add(lblNewLabel);
		
		tfContextName = new JTextField();
		tfContextName.setPreferredSize(new Dimension(200, 26));
		lblNewLabel.setLabelFor(tfContextName);
		panel.add(tfContextName);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(panel_1);
		
		JLabel lblNewLabel_1 = new JLabel("Context Memembers: ");
		lblNewLabel_1.setPreferredSize(new Dimension(160, 30));lblNewLabel_1.setSize(200, getHeight());
		panel_1.add(lblNewLabel_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(450, 104));
		panel_1.add(scrollPane);
		
		list = new JList();
//		list.setModel(new AbstractListModel() {
//			String[] values = new String[] {"fff", "ddddd", "fsfgfg", "fsdfsdf","dfsdf","sdfsdf"};
//			public int getSize() {
//				return values.length;
//			}
//			public Object getElementAt(int index) {
//				return values[index];
//			}
//		});
		//list.setPreferredSize(new Dimension(450, 30));
		//list.setVisibleRowCount(4);
		scrollPane.setViewportView(list);
		//contextMemembersDlm = new DefaultListModel();
		//list.setModel(contextMemembersDlm);
		list.setBackground(Color.WHITE);
		list.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cmc == null){
					cmc = new ContextMememberChooser();
				}
				cmc.setVisible(true);
				ContextEditorView topParent = (ContextEditorView)(getParent().getParent());//xd get the top parent component
				cmc.setParentContextEditorView(topParent);//xd point the top parent component to the ContextEditorView
				TreeSet<OWLOntology> ts = new TreeSet<>(topParent.getContextSkeletonView().getOWLModelManager().getOWLObjectComparator());
		        ts.addAll(topParent.getContextSkeletonView().getOWLModelManager().getOntologies());
		        cmc.list.setCellRenderer(topParent.getCellRenderer());
		        cmc.list.setModel(new DefaultComboBoxModel<>(ts.toArray(new OWLOntology[ts.size()])));
		        cmc.list.setSelectedIndex(-1);//xd every time this dialog opens, the selected item is unknown
				
		        
			}
		});
		panel_1.add(btnAdd);
		
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedValuesList().size() > 0){//xd really want to remove some from the context memembers
					parentContextEditorView.removeContextMemembers(list.getSelectedValuesList());
					
					/*xd after add a new context member, should refresh source and target ontology panel*/
			        //parentContextEditorView.getContextSkeletonView().getSourceOntologyView().refreshOntologiesList();
			        //parentContextEditorView.getContextSkeletonView().getTargetOntologyView().refreshOntologiesList();
			        parentContextEditorView.getContextSkeletonView().updateEntityListHandler();
				}
			}
		});
		panel_1.add(btnRemove);
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		add(panel_2);
		
		JLabel lblNewLabel_2 = new JLabel("Context Root:");
		lblNewLabel_2.setPreferredSize(new Dimension(160, 30));lblNewLabel_2.setSize(200, getHeight());
		panel_2.add(lblNewLabel_2);
		
		comboBox = new JComboBox();
		comboBox.setPreferredSize(new Dimension(450, 26));comboBox.setSize(500,getHeight());
		panel_2.add(comboBox);
		//revalidate();
	}
	
	
	public void setJListAndJComboBoxRenderer(OWLOntologyCellRenderer renderer){
		list.setCellRenderer(renderer);
		comboBox.setRenderer(renderer);
	}
	
	/**
	 * @return the parentContextEditorView
	 */
	public ContextEditorView getParentContextEditorView() {
		return parentContextEditorView;
	}


	/**
	 * @param parentContextEditorView the parentContextEditorView to set
	 */
	public void setParentContextEditorView(ContextEditorView parentContextEditorView) {
		this.parentContextEditorView = parentContextEditorView;
	}
}
