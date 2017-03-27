/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import gomoku.Lang;

/**
*
* Dolny pasek stanu
* 
* @author Maciej Kawecki
* 
*/
@SuppressWarnings("serial")
public class StatusBar extends JToolBar implements ILocalizable {

  /** Wysokość paska stanu */
  protected final static int BAR_HEIGHT = 25;
	
  /** Referencja do GUI */
  private final GUI frame;	  
	
  /** Obiekt paska postepu */
  private final  JProgressBar progressBar;
  /** Etykieta z komunikatem */
  private final JLabel progressLabel;
  /** Przycisk anulowania operacji */
  private final JButton cancelButton;
  
  /** Obiekt zmiany stanu- dla listenera */
  private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
  
  
  /**
   * Konstruktor
   * @param frame Ref. do GUI
   */
  public StatusBar(GUI frame) {
	  
	super();  
	  
	this.frame = frame;
	
	
	setBorder(new EmptyBorder(5, 2, 5, 2));
	setFloatable(false);
    setPreferredSize(new Dimension(GUI.F_WIDTH-4, BAR_HEIGHT));
    setMinimumSize(new Dimension(GUI.F_WIDTH-4, BAR_HEIGHT));
	  
    progressBar = new JProgressBar();
    progressBar.setIndeterminate(false);
    progressBar.setStringPainted(false);
    progressBar.setValue(0);
    
    progressLabel = new JLabel(Lang.get("PleaseWait") + "  ");
    progressLabel.setForeground(getBackground());
    
    add(progressLabel);
    add(progressBar);
    
    add(new JLabel(" "));
    
    cancelButton = new JButton(ImageRes.getIcon("cancel.png"));
    cancelButton.setPreferredSize(new Dimension(15, 10));
    cancelButton.setEnabled(false);
    add(cancelButton);
    
    cancelButton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
		  changes.firePropertyChange("cancelled", false, true);
		}
	});
    
    super.setVisible(false);
	  
  }
  
  
  @Override
  public void setVisible(boolean visible) {	  

	if (visible == isVisible()) return;  
	  
	frame.setSize(GUI.F_WIDTH, GUI.F_HEIGHT + (visible ? BAR_HEIGHT : 0));  	
	super.setVisible(visible);	   
	frame.repaint();

  }
  
  
  @Override
  public void translate() {
	  
	progressLabel.setText(Lang.get("PleaseWait") + "  "); 		   
		   
  }
  
	
  /**
   * (De)aktywowanie paska postępu (indeterminate)
   * @param enabled True jeżeli aktywny
   */
  public void enableProgress(boolean enabled) {
	  
	progressLabel.setForeground(enabled ? new Color(0x808080) : getBackground());
	progressBar.setIndeterminate(enabled); 
	cancelButton.setEnabled(enabled);	  
	
  }
  
  
  @Override
  public void addPropertyChangeListener(PropertyChangeListener l) {
	try {
	  changes.addPropertyChangeListener(l);
	}
	catch (NullPointerException e) {
	  super.addPropertyChangeListener(l);
	}
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener l) {
	  
	changes.removePropertyChangeListener(l);
  }
	
	
}
