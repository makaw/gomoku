/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import game.GameMode;
import gomoku.Settings;


/**
 *
 * Skrócony interfejs klas GUI
 * 
 * @author Maciej Kawecki
 * 
 */
public interface IBaseGUI extends ILocalizable {
   

  /**
   * Metoda pobierająca referencję do ustawień gry
   * @return Referencja do ustawień gry
   */  
  Settings getSettings();
  
  /**
   * Metoda obsługująca uruchomienie nowej gry: zmiana stanu, powiadomienie 
   * wątku kontrolującego przebieg gry o zmianie, wyczyszczenie graficznej planszy i konsoli
   * @param gameMode Wybrany przez użytkownika tryb nowej gry
   * @param serverIP Adres IP serwera   
   */  
  void restartGame(GameMode gameMode, String serverIP);
  
  /**
   * Metoda obsługująca zmianę ustawień przez użytkownika: zatrzymanie bieżącej rozgrywki, 
   * powiadomienie wątku kontrolującego przebieg gry o zmianie stanu, wyczyszczenie konsoli, przygotowanie 
   * nowej graficznej planszy, oraz przesłanie do wątku kontrolującego przebieg gry nowych ustawień i 
   * referencji do nowej graficznej reprezentacji planszy.
   */  
  void restartGameSettings();  
  
  /**
   * Metoda odpowiada czy interfejs jest implementowany przez GUI serwera, czy klienta
   * @return True jeżeli serwer, false jeżeli klient
   */
  boolean isServer();
  
  
  
  
  /** 
   * Wszystkie zawarte komponenty wymagające tłumaczenia
   * @param container Kontener
   * @return Lista komponentów
   */
  static List<Component> getAllLocalised(final Container container) {
	 
	Component[] components = container.getComponents();
	List<Component> compList = new ArrayList<>();
	for (Component c : components) {
	  if (c instanceof ILocalizable) compList.add(c);
	  if (c instanceof Container) compList.addAll(getAllLocalised((Container) c));
	}
	return compList;
	
  }
  
  
  
  /**
   * Kod pierwszego znaku elementu menu
   * @param item Element menu
   * @return Kod znaku
   */
  static int getKeyCode(JMenuItem item) {
	
	return KeyStroke.getKeyStroke(((JMenuItem)item).getText().charAt(0), 0).getKeyCode();
	
  }  
  
   
}
