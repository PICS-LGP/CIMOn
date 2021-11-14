/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 29 mai 2017
 * 
 */
package ecolabel.protege.plugin.component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.protege.editor.owl.ui.renderer.OWLOntologyCellRenderer;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import java.awt.Point;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.ComponentOrientation;
import javax.swing.ScrollPaneConstants;
import java.awt.GridLayout;
import java.awt.Font;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 29 mai 2017
 * 
 */
public class BinaryMappingBundleComponent extends JPanel {

	public String mappingBundleName;
	public JTextField tfBMBName;
	public JComboBox cbSourceOntology;
	public JComboBox cbTargetOntology;
	public JButton btnNewButton;
	public JPanel binaryMappingContainer;
	JScrollPane scrollPane;
	/**
	 * @return the mappingBundleName
	 */
	public String getMappingBundleName() {
		return mappingBundleName;
	}
	/**
	 * @param mappingBundleName the mappingBundleName to set
	 */
	public void setMappingBundleName(String mappingBundleName) {
		this.mappingBundleName = mappingBundleName;
	}
	/**
	 * Create the panel.
	 */
	public BinaryMappingBundleComponent() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setSize(new Dimension(700,100));
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		tfBMBName = new JTextField();
		tfBMBName.setFocusTraversalKeysEnabled(false);
		tfBMBName.setFont(new Font("Tahoma", Font.BOLD, 17));
		tfBMBName.setPreferredSize(new Dimension(200, 26));
		panel_1.add(tfBMBName);
		
		JLabel lblNewLabel = new JLabel("Source Ontology:");
		lblNewLabel.setPreferredSize(new Dimension(140, 20));
		panel_1.add(lblNewLabel);
		
		cbSourceOntology = new JComboBox();
		cbSourceOntology.setPreferredSize(new Dimension(250, 26));
		panel_1.add(cbSourceOntology);
		
		JButton btnNewButton_1 = new JButton("Reverse");
		btnNewButton_1.addActionListener(new ActionListener() {//xd reverse the source and target ontology
			public void actionPerformed(ActionEvent arg0) {
				int temp = cbSourceOntology.getSelectedIndex();
				cbSourceOntology.setSelectedIndex(cbTargetOntology.getSelectedIndex());
				cbTargetOntology.setSelectedIndex(temp);
			}
		});
		btnNewButton_1.setPreferredSize(new Dimension(90, 29));
		panel_1.add(btnNewButton_1);
		
		JLabel lblNewLabel_1 = new JLabel("Target Ontology:");
		panel_1.add(lblNewLabel_1);
		lblNewLabel_1.setPreferredSize(new Dimension(140, 20));
		
		cbTargetOntology = new JComboBox();
		panel_1.add(cbTargetOntology);
		cbTargetOntology.setPreferredSize(new Dimension(250, 26));
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		btnNewButton = new JButton("New Mapping");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("New Mapping...");
				BinaryMappingComponent BMC = new BinaryMappingComponent();//xd new OBO ro.owl relationships are integrated
				binaryMappingContainer.add(BMC);
				binaryMappingContainer.revalidate();
				binaryMappingContainer.repaint();
				//scrollPane.repaint();
			}
		});
		panel_2.add(btnNewButton);
		//add(table, BorderLayout.SOUTH);
		
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane, BorderLayout.CENTER);
		
		binaryMappingContainer = new JPanel();
		binaryMappingContainer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		binaryMappingContainer.setBackground(SystemColor.text);
		//binaryMappingContainer.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));//xd 
		
		scrollPane.setViewportView(binaryMappingContainer);
		binaryMappingContainer.setLayout(new BoxLayout(binaryMappingContainer, BoxLayout.Y_AXIS));
		
//		for(int i = 0; i < 10; i ++){
//			BinaryMappingComponent bmc = new BinaryMappingComponent();
//			binaryMappingContainer.add(bmc);
//		}
		

	}
	
	
	public void setJComboBoxRenderer(OWLOntologyCellRenderer renderer){
		cbSourceOntology.setRenderer(renderer);
		cbTargetOntology.setRenderer(renderer);
	}

}
