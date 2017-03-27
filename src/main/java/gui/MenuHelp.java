/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import gomoku.Lang;
import gui.dialogs.RulesDialog;


/**
 *
 * Szablon obiektu budującego menu "Pomoc" aplikacji
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class MenuHelp extends JMenu implements ILocalizable {
    
   /** Menu: opcja about */
   private final JMenuItem aboutItem;	
	
   
   /**
    * Konstruktor 
    * @param frame Interfejs GUI
    */    
   public MenuHelp(final IBaseGUI frame) {
       
    super(Lang.get("MenuHelp"));
    setMnemonic(GUI.getKeyCode(this));
    
    
    aboutItem = new JMenuItem(Lang.get("MenuAbout"));
    aboutItem.setMnemonic(GUI.getKeyCode(aboutItem));
    add(aboutItem);
    aboutItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {        
        new RulesDialog(frame);
      }
    });    
    
   } 
   
   
   @Override
   public void translate() {
	   
	  setText(Lang.get("MenuHelp"));
	  setMnemonic(GUI.getKeyCode(this)); 
		   
	  aboutItem.setText(Lang.get("MenuAbout"));     
	  aboutItem.setMnemonic(GUI.getKeyCode(aboutItem));	   
	   
   }
    
    
}
