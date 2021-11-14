/**
 * @author XU Da ENIT-LGP xudaddd@gmail.com 
 * @time 13 juin 2017
 * 
 */
package ecolabel.protege.plugin.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * @author: XU Da ENIT-LGP xudaddd@gmail.com 
 * @time: 13 juin 2017
 * 
 */
public class SeparatorComboBoxRenderer extends BasicComboBoxRenderer implements ListCellRenderer<Object>
{
	   public SeparatorComboBoxRenderer() {
	      super();
	   }
	    
	   public Component getListCellRendererComponent( JList list,
	           Object value, int index, boolean isSelected, boolean cellHasFocus) {
	      if (isSelected) {
	          setBackground(list.getSelectionBackground());
	          setForeground(list.getSelectionForeground());
	      }
	      else {
	          setBackground(list.getBackground());
	          setForeground(list.getForeground());
	      }
	  
	      setFont(list.getFont());
	      if (value instanceof Icon) {
	         setIcon((Icon)value);
	      }
	      if (value instanceof JSeparator) {
	         return (Component) value;
	      }
	      else {
	         setText((value == null) ? "" : value.toString());
	      }
	  
	      return this;
	  } 
	}

class SeparatorComboBoxListener implements ActionListener {
	   JComboBox combobox;
	   Object oldItem;
	     
	   SeparatorComboBoxListener(JComboBox combobox) {
	      this.combobox = combobox;
	      combobox.setSelectedIndex(0);
	      oldItem = combobox.getSelectedItem();
	   }
	      
	   @Override
	   public void actionPerformed(ActionEvent e) {
	      Object selectedItem = combobox.getSelectedItem();
	      if (selectedItem instanceof JSeparator) {
	         combobox.setSelectedItem(oldItem);
	      } else {
	         oldItem = selectedItem;
	      }
	   }

	
	}