/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import gui.dialogs.RulesDialog;


/**
 *
 * Szablon obiektu budującego menu "Pomoc" aplikacji
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class MenuHelp extends JMenu {
    
   /**
    * Konstruktor 
    * @param frame Interfejs GUI
    */    
   protected MenuHelp(final IBaseGUI frame) {
       
    super("Pomoc");
    setMnemonic(KeyEvent.VK_P);
    
    
    JMenuItem menuItem = new JMenuItem("O programie");
    menuItem.setMnemonic(KeyEvent.VK_O);
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {        
        new RulesDialog(frame);
      }
    });    
    
   } 
    
    
}
