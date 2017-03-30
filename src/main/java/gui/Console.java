/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import java.awt.Color;

import javax.swing.JButton;

import gomoku.Lang;

/**
 *
 * Konsola do wyświetlania komunikatów w oknie gry 
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class Console extends BaseConsole implements ILocalizable {
      
   /** Przycisk Wł / wył gry */
   private final JButton sndButton;
   /** Przycisk do wysyłania wiadomości w grze sieciowej */
   private final JButton msgButton;   
   /** Przycisk do rozłączenia z serwerem */
   private final JButton dscButton;
   /** Menu gry */
   private final MenuGame menuGame;
    
   /**
    * Konstruktor 
    * @param sndButton Przycisk do wł/wył dźwięku
    * @param msgButton Przycisk do wysyłania wiadomości w grze sieciowej
    * @param dscButton Przycisk do rozłączenia z serwerem
    * @param menuGame menu "Gra"
    */
   protected Console(JButton sndButton, JButton msgButton, JButton dscButton, MenuGame menuGame) {
       
      super();
      
      this.sndButton = sndButton;
      this.msgButton = msgButton;
      this.dscButton = dscButton;
      this.menuGame = menuGame;
      
   } 
   
   
      
   /**
    * Komunikat - jak rozpocząć nową grę
    */
   public void newGameMsg() {
	   
	  setMessageLn(Lang.get("UseGameMenuToStart"), Color.GRAY); 
	   
   }   
   
   
  /**
   * Metoda blokuje/odblokowuje przyciski wiadomości i odłączenia
   * @param enabled True jeżeli przyciski mają być odblokowane
   */
  public void networkButtonsEnable(boolean enabled) {
      
    msgButton.setEnabled(enabled); 
    dscButton.setEnabled(enabled); 
    menuGame.enableItems(!enabled);

    menuGame.getDscItem().setVisible(enabled);
    menuGame.getDscItem().setEnabled(enabled);
      
  }
  
  
  @Override
  public void translate() {
	  
	sndButton.setToolTipText(Lang.get("SoundOnOff"));
    msgButton.setToolTipText(Lang.get("SendMessage"));
	dscButton.setToolTipText(Lang.get("DisconnectServer"));
	
  }
  
  
}
