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
import gomoku.Lang;
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
public class MenuGame extends JMenu implements ILocalizable {
    
   /** Menu: opcja nowej gry */ 
   private final JMenuItem newGameItem;
   /** Menu: opcja serwer */
   private final JMenuItem serverItem;
   /** Menu: opcja rozłącz */
   private final JMenuItem dscItem;
   /** Menu: opcja ustawienia */
   private final JMenuItem settingsItem;
   /** Menu: opcja koniec */
   private final JMenuItem quitItem;
    
   /**
    * Konstruktor  
    * @param frame Interfejs GUI
    */
   public MenuGame(final IBaseGUI frame) {
       
     super(Lang.get("MenuGame"));
     setMnemonic(IBaseGUI.getKeyCode(this));
    
     newGameItem = new JMenuItem(Lang.get("MenuNewGame"));     
     newGameItem.setPreferredSize(new Dimension(160, 20));
     newGameItem.setMnemonic(IBaseGUI.getKeyCode(newGameItem));
     newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
     add(newGameItem);
     newGameItem.addActionListener(new ActionListener() {
       @Override  
       public void actionPerformed(final ActionEvent e) {
           
          new NewGameDialog(frame);
         
       }
     });   
     
     
    serverItem = new JMenuItem(Lang.get("MenuRunServer"));
    serverItem.setPreferredSize(new Dimension(160, 20));
    serverItem.setMnemonic(IBaseGUI.getKeyCode(serverItem));
    add(serverItem);
    serverItem.addActionListener(new ActionListener() {
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
     
    
     dscItem = new JMenuItem(Lang.get("MenuDisconnect"));
     dscItem.setMnemonic(IBaseGUI.getKeyCode(dscItem));
     dscItem.setEnabled(false);
     dscItem.setVisible(false);
     add(dscItem);
     
    
     settingsItem = new JMenuItem(Lang.get("MenuSettings"));
     settingsItem.setMnemonic(IBaseGUI.getKeyCode(settingsItem));
     add(settingsItem);
     settingsItem.addActionListener(new ActionListener() {
       @Override  
       public void actionPerformed(final ActionEvent e) {
           
          new SettingsDialog(frame);
         
       }
     });       
     

     addSeparator(); 

     quitItem = new JMenuItem(Lang.get("MenuQuit"));
     quitItem.setMnemonic(IBaseGUI.getKeyCode(quitItem));
     quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
     add(quitItem);       
     quitItem.addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(final ActionEvent e) {
           
        boolean res = new ConfirmDialog(frame, Lang.get("ExitConfirm")).isConfirmed();
        if (res) System.exit(0);
          
       }
     });          
       
   }
   
   
   /**
    * Wł / wył opcji nowej gry i ustawień
    * @param enabled Czy włączone
    */
   public void enableItems(boolean enabled) {
	   
	 newGameItem.setEnabled(enabled);
	 settingsItem.setEnabled(enabled);
	   
   }
   
   
   public JMenuItem getDscItem() {
	 return dscItem;
   }
   
   

   @Override
   public void translate() {
	   
	  setText(Lang.get("MenuGame"));
	  setMnemonic(IBaseGUI.getKeyCode(this)); 
	   
	  newGameItem.setText(Lang.get("MenuNewGame"));     
	  newGameItem.setMnemonic(IBaseGUI.getKeyCode(newGameItem));
	  serverItem.setText(Lang.get("MenuRunServer"));     
	  serverItem.setMnemonic(IBaseGUI.getKeyCode(serverItem));
	  dscItem.setText(Lang.get("MenuDisconnect"));
	  dscItem.setMnemonic(IBaseGUI.getKeyCode(dscItem));
	  settingsItem.setText(Lang.get("MenuSettings"));     
	  settingsItem.setMnemonic(IBaseGUI.getKeyCode(settingsItem));
	  quitItem.setText(Lang.get("MenuQuit"));     
	  quitItem.setMnemonic(IBaseGUI.getKeyCode(quitItem));
	   
   }
   
    
}
