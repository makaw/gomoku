/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import gomoku.Gomoku;
import gui.dialogs.ConfirmDialog;
import gui.dialogs.NewGameDialog;
import gui.dialogs.SettingsDialog;

/**
 *
 * Szablon obiektu budującego menu "Gra" aplikacji
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class MenuGame extends JMenu {
    
   /** Menu: opcja nowej gry */ 
   private final JMenuItem newGameItem;
    
    
   /**
    * Konstruktor budujący menu "Gra" aplikacji
    * @param frame Referencja do interfejsu GUI
    */
   protected MenuGame(final IBaseGUI frame) {
       
     super("Gra");
     setMnemonic(KeyEvent.VK_G);
    
     newGameItem = new JMenuItem("Nowa gra");     
     newGameItem.setPreferredSize(new Dimension(160, 20));
     newGameItem.setMnemonic(KeyEvent.VK_N);
     newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
     add(newGameItem);
     newGameItem.addActionListener(new ActionListener() {
       @Override  
       public void actionPerformed(final ActionEvent e) {
           
          // wywołanie okna z wyborem trybu nowej gry 
          new NewGameDialog(frame);
         
       }
     });   
     
     
    JMenuItem menuItem = new JMenuItem("Serwer");
    menuItem.setPreferredSize(new Dimension(160, 20));
    menuItem.setMnemonic(KeyEvent.VK_S);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
       @Override  
       public void actionPerformed(final ActionEvent e) {
           
         // -------------------------------------------------------  
         // kod ze stackoverflow.com (4159802)
         StringBuilder cmd = new StringBuilder();
         cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
         for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            cmd.append(jvmArg + " ");
         }
         cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
         cmd.append(Gomoku.class.getName()).append(" -s");
         try {
           Runtime.getRuntime().exec(cmd.toString());
         }
         catch (IOException ex) { System.err.println(ex); }
         // -------------------------------------------------------
           
       }
     });   
     
     
    
     menuItem = new JMenuItem("Ustawienia");
     menuItem.setMnemonic(KeyEvent.VK_U);
     menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
     add(menuItem);
     menuItem.addActionListener(new ActionListener() {
       @Override  
       public void actionPerformed(final ActionEvent e) {
           
          // wywołanie okna z wyborem ustawień 
          new SettingsDialog(frame);
         
       }
     });       
     
     //menuItem.setEnabled(false);

     addSeparator(); 

     menuItem = new JMenuItem("Koniec");
     menuItem.setMnemonic(KeyEvent.VK_K);
     menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));
     add(menuItem);       
     menuItem.addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(final ActionEvent e) {
           
        boolean res = new ConfirmDialog(frame, "Czy na pewno zako\u0144czy\u0107 ?").isConfirmed();
        // po potwierdzeniu przez użytkownika, zakończenie pracy aplikacji
        if (res) Gomoku.quitGomoku();
          
       }
     });          
       
   }

   
   public JMenuItem getNewGameItem() {
       return newGameItem;
   }
   
   
   
   
    
    
}
