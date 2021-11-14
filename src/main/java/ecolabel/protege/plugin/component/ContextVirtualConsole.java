/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 14 juin 2017
 * 
 */
package ecolabel.protege.plugin.component;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 14 juin 2017
 * 
 */
public class ContextVirtualConsole extends JScrollPane{

	//public JScrollPane panelConsole;
	public JTextPane textPaneConsole;
	
	//public enum MessageType {ERROR, INFO, WARNING}
	
	public ContextVirtualConsole(){
		super();
		
		setPreferredSize(new Dimension(1000,100));
		textPaneConsole = new JTextPane();
		textPaneConsole.setEditable(false);
		textPaneConsole.setText("...");
		setViewportView(textPaneConsole);
	}
	
	
	/************
	 * xd normal printing
	 * @param msg
	 */
	public void printInfo(String msg){
		SimpleAttributeSet infoStyle = new SimpleAttributeSet();//xd info style (plain text in bold)
		StyleConstants.setForeground(infoStyle, Color.black);
		StyleConstants.setBackground(infoStyle, Color.white);
		StyleConstants.setBold(infoStyle, true);
		print(msg, infoStyle);
		
	}

	
	public void print(String msg, SimpleAttributeSet s){
		StyledDocument document = (StyledDocument) textPaneConsole.getStyledDocument();
	    try {
			document.insertString(document.getLength(), msg+"\n", s);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * xd clear the virtual console content
	 */
	public void clear() {
		
		StyledDocument document = (StyledDocument) textPaneConsole.getStyledDocument();
	    try {
			document.insertString(document.getLength(), "", null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*********
	 * 
	 * @param msg
	 */
	public void printWarning(String msg){
		SimpleAttributeSet warningStyle = new SimpleAttributeSet();//xd info style (plain text in bold)
		StyleConstants.setForeground(warningStyle, Color.ORANGE);
		StyleConstants.setBackground(warningStyle, Color.white);
		StyleConstants.setBold(warningStyle, true);
		print(msg, warningStyle);
	}
	
	
	/*************
	 * 
	 * @param msg
	 */
	public void printError(String msg){
		SimpleAttributeSet errorStyle = new SimpleAttributeSet();//xd info style (plain text in bold)
		StyleConstants.setForeground(errorStyle, Color.red);
		StyleConstants.setBackground(errorStyle, Color.white);
		StyleConstants.setBold(errorStyle, true);
		print(msg, errorStyle);
	}
	
}
