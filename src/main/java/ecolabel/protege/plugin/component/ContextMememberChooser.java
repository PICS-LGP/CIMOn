/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 2 juin 2017
 * 
 */
package ecolabel.protege.plugin.component;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.ScrollPane;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ecolabel.protege.plugin.view.ContextEditorView;

import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 2 juin 2017
 * 
 */
public class ContextMememberChooser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private ContextEditorView parentContextEditorView; 
	public JScrollPane scrollPane;
	public JList list;

	

	/**
	 * Create the dialog.
	 */
	public ContextMememberChooser() {
		setTitle("Select opened ontologies as memembers of your context");
		setBounds(100, 100, 650, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			scrollPane = new JScrollPane();
			contentPanel.add(scrollPane);
		}
		{
			list = new JList();
			scrollPane.setViewportView(list);
			
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {//xd send the newly chosen context memembers back to ContextEditorView
						if(list.getSelectedValuesList().size() > 0){//xd if we really get a not-null selection list
							parentContextEditorView.addContextMemembers(list.getSelectedValuesList());
							
							/*xd after add a new context member, should refresh source and target ontology panel*/
					        //parentContextEditorView.getContextSkeletonView().getSourceOntologyView().refreshOntologiesList();
					        //parentContextEditorView.getContextSkeletonView().getTargetOntologyView().refreshOntologiesList();
					        parentContextEditorView.getContextSkeletonView().updateEntityListHandler();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Close");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
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
