/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import gomoku.Settings;


/**
 *
 * Skrócony interfejs szablonu obiektu budującego graficzny interfejs użytkownika, 
 * udostępnia metody potrzebne do budowy okienek dialogowych
 * 
 * @author Maciej Kawecki
 * 
 */
public interface IBaseGUI {
   

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
   * @see game.GameStateSpyInterface
   */  
  void restartGame(byte gameMode, String serverIP);
  /**
   * Metoda obsługująca zmianę ustawień przez użytkownika: zatrzymanie bieżącej rozgrywki, 
   * powiadomienie wątku kontrolującego przebieg gry o zmianie stanu, wyczyszczenie konsoli, przygotowanie 
   * nowej graficznej planszy, oraz przesłanie do wątku kontrolującego przebieg gry nowych ustawień i 
   * referencji do nowej graficznej reprezentacji planszy.
   * @see game.SettingsSpyInterface
   * @see game.GameStateSpyInterface
   */  
  void restartGameSettings();  
  /**
   * Metoda odpowiada czy interfejs jest implementowany przez GUI serwera, czy klienta
   * @return True jeżeli serwer, false jeżeli klient
   */
  boolean isServer();
   
}
