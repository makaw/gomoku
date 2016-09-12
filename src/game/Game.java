/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import gomoku.Settings;
import gomoku.SettingsSpy;
import gui.BoardGraphics;
import gui.Console;
import gui.GUI;
import gui.Sounds;
import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.SwingUtilities;
import network.Client;
import network.Command;


/**
 *
 * Szablon obiektu kontrolującego przebieg rozgrywki
 * 
 * @author Maciej Kawecki
 * 
 */
public class Game  implements Observer {
   
   /** Stała używana do oznaczenia stanu rozgrywki: trwa */
   public final static byte RUN = 0;
   /** Stała używana do oznaczenia stanu rozgrywki: rozpoczęcie nowej gry */
   public final static byte RESTART = 1;
   /** Stała używana do oznaczenia stanu rozgrywki: oczekiwanie na nową grę */
   public final static byte WAIT = 2;    
  
   /** Stała używana do oznaczenia trybu rozgrywki: gracz kontra komputer */
   public final static byte SINGLE_GAME = 1;
   /** Stała używana do oznaczenia trybu rozgrywki: gracz kontra gracz (hot-seat) */
   public final static byte HOTSEAT_GAME = 2;
   /** Stała używana do oznaczenia trybu rozgrywki: gracz kontra gracz (gra sieciowa) */
   public final static byte NETWORK_GAME = 3;        
    
    
   /** Referencja do obiektu reprezentującego pierwszego z graczy */
   private Player player1;
   /** Referencja do obiektu reprezentującego drugiego z graczy */
   private Player player2;
   /** Referencja do obiektu logicznej warstwy planszy */
   private BoardLogic lBoard;
   /** Referencja do obiektu będącego graficzną reprezentacją planszy */
   private BoardGraphics gBoard;
   /** Referencja do obiektu reprezentującego konsolę do wyświetlania komunikatów */
   private final Console console;
   /** Referencja do obiektu służącego do odtwarzania dźwięków */
   private final Sounds sounds;
   /** Aktualny stan gry (trwa, oczekiwanie, restart) */
   private byte gameState;   
   /** Podany przez użytkownika adres IP serwera do przekazania do obiektu klienta */
   private String serverIP;
   /** Referencja do obiektu służącego do komunikacji z tym wątkiem, 
    * potrzebna do ustawienia obserwacji stanu gry przez wątek serwera */
   private final GameStateSpy gameStateSpy;
   /** Obiekt klienta w grze siociowej */
   private Client client;

   
   /**
    * Konstruktor obiektu odpowiedzialnego za przebieg rozgrywki, przypisuje referencje do obiektów GUI
    * @param gBoard Referencja do graficznej reprezentacji planszy
    * @param console Referencja do obiektu reprezentującego konsolę do wyświetlania komunikatów
    * @param sounds Referencja do obiektu służącego do odtwarzania dźwięków
    * @param gameStateSpy Referencja do obiektu służącego do komunikacji z tym wątkiem, 
    * potrzebna do ustawienia obserwacji stanu gry przez wątki sieciowe
    */
   public Game(BoardGraphics gBoard, Console console, Sounds sounds, GameStateSpy gameStateSpy) {
     
     this.gBoard = gBoard;
     this.console = console;
     this.sounds = sounds;
     this.gameStateSpy = gameStateSpy;
         
   }
   
   /**
    * Metoda aktualizująca zmienną określającą stan gry przy wykorzystaniu
    * mechanizmu "Obserwatora" (Observer); służy do komunikacji z wątkiem GUI
    * @param obs Obserwowany obiekt
    * @param change Przekazany nowy stan gry 
    * @see GameStateSpy 
    * @see gui.GUI#restartGame(byte, java.lang.String) 
    */
   @Override
   public void update(Observable obs, Object change) {
      
     // zmieniono stan gry (restart)
     if (change instanceof GameStateSpy) {
               
       // zmiana stanu  
       this.gameState = ((GameStateSpy)change).state;
       
       // wymuszenie końca ruchu graczy
       if (player1!=null) player1.forceEndTurn();
       if (player2!=null) player2.forceEndTurn();
       // zmiana adresu IP serwera
       this.serverIP = ((GameStateSpy)change).serverIP;
       console.msgButtonEnable(false);                            
       
     }
     // zmiana ustawień już po wyborze trybu rozgrywki, dla gry sieciowej
     else if (change instanceof SettingsSpy) {
       
       SettingsSpy changedSettings = (SettingsSpy)change;  
       // czy zmiana ma być wykonana z tego miejsca ?
       if (!changedSettings.isRestart()) {
          
          // zmiana referencji do graficznej planszy
          setBoard(changedSettings.getBoard());
        
       }
       
     }
     
     // prześlij wiadomość
     else if (change instanceof String && !String.valueOf(change).startsWith("server_")) {
         
         try {
             
            String msg = "[Gracz "+(client.getNumber()+1)+"]: " + (String)change;
            client.sendCommand(new Command(Command.CMD_MESSAGE, msg));
            console.newLine();
            console.setMessageLn("Wys\u0142ano: "+(String)change, Color.GRAY);
            
         } catch (IOException e) {
             System.err.println(e);
         } catch (ClassNotFoundException e) {
             System.err.println(e);
         }
     
     }
   
     
   }
   
   /**
    * Metoda zwracająca aktualny stan gry
    * @return Aktualny stan gry
    */
   public Byte getState() {
       
      return gameState;  
       
   }

   
   /**
    * Metoda ustanawiająca nową referencję do graficznej reprezentacji planszy
    * @param board Nowa referencja do graficznej reprezentacji planszy
    */
   public void setBoard(BoardGraphics board) {
       
      gBoard = board; 
       
   }
   
   
   /**
    * Rozpoczęcie nowej rozgrywki.
    * @param gameMode Tryb nowej rozgrywki (gra z komputerem, hot-seat, serwer lub klient)
    * @param settings Referencja do aktualnych ustawień gry
    */
   public void startNewGame(Byte gameMode, Settings settings) {
   
       
     gameState = Game.RUN;
     // logika planszy dla przekazanych ustawień
     lBoard = new BoardLogic(settings);
     

     // przypisanie odpowiednich implementacji gracza w zależności od trybu rozgrywki
     switch (gameMode) {

         // komputer vs gracz
         case SINGLE_GAME:
             
            player1 = new PlayerComputer(BoardField.BLACK, gBoard, lBoard, "Komputer");
            player2 = new PlayerHuman(BoardField.WHITE, gBoard, lBoard, "Gracz [Ty]");
            
            break;         
             
         // gracz vs gracz
         case HOTSEAT_GAME:
             
            player1 = new PlayerHuman(BoardField.BLACK, gBoard, lBoard, "Gracz 1");
            player2 = new PlayerHuman(BoardField.WHITE, gBoard, lBoard, "Gracz 2");
            
            break;  
             
        
         //  gracz (klient lokalny) vs gracz (klient zdalny)
         case NETWORK_GAME:
       
             
            try {
                
              client = new Client(serverIP, gameStateSpy, console);  
              // jeżeli się udało połączyć, to zmiana ustawień gry
              Settings clientSettings = client.getSettings();
              ((GUI)(SwingUtilities.getWindowAncestor(console)))
                      .restartClientGameSettings(clientSettings.getColsAndRows());
              settings.setSettings(clientSettings.getColsAndRows(), clientSettings.getPiecesInRow(), 
                      clientSettings.getPiecesInRowStrict());   
              
              // zmiana logiki planszy, bo zmiana ustawień
              lBoard = new BoardLogic(clientSettings);
              
              // kto pierwszy ten zaczyna
              if (client.getNumber()==0) {
                player1 = new PlayerLocal(client, BoardField.BLACK, gBoard, lBoard, "Gracz 1 [Ty] ");
                player2 = new PlayerRemote(client, BoardField.WHITE, gBoard, lBoard, "Gracz 2");
              }
              else {
                player1 = new PlayerRemote(client, BoardField.BLACK, gBoard, lBoard, "Gracz 1");    
                player2 = new PlayerLocal(client, BoardField.WHITE, gBoard, lBoard, "Gracz2 [Ty]");   
              }
              
              
              console.msgButtonEnable(true);
              
            } catch (IOException e) {
               
               try {
                  Thread.sleep(100);  
               } 
               catch (Exception ex) {} 
               
               console.setMessageLn("Pr\u00f3ba po\u0142\u0105czenia nieudana.", Color.RED); 
               player1 = null;
               player2 = null;
               
                
            } catch (ClassNotFoundException e) {             
               System.err.println(e);
            }
            

            break;
             
         
     }
     
     
     // zatrzymanie, jeżeli brakuje graczy lub nie ma połąćzenia przy grze sieciowej
     if (player1==null || player2==null) {
           
         gameState=Game.WAIT;
         console.setMessageLn("Wybierz \"Gra\"  \u279C \"Nowa gra\" aby " +
                              "rozpocz\u0105\u0107.", Color.GRAY);

         
     }
     
     
     // sygnał rozpoczęcia, jeżeli wszystko w porządku
     else {
       
       sounds.play(Sounds.SND_INFO); 
       
       try {
         Thread.sleep(100);  
       }
       catch(InterruptedException e) {
         System.err.println(e);
       }       
     
       console.setMessageLn("START!", new Color(0x22, 0x8b, 0x22));  
       console.newLine();
         
     }

     int moveNo = 1;            // nr ruchu
     List<BoardField> winRow;   // lista kamieni w ewentualnym wygrywającym rzędzie
     
     List<Player> players = Arrays.asList(player1, player2); 
     
     // petla rozgrywki
     while (gameState==Game.RUN) {  
                    
       // sekwencja zdarzeń dla każdego z graczy  
       for(Player p:players) if (gameState==Game.RUN) {
         
         // komunikat na konsoli
         console.setMessage("Ruch #" + Integer.toString(moveNo) + ": ", Color.BLUE);
         console.setMessage(p.getName(), (p.getColor()==BoardField.WHITE) ? Color.BLACK : Color.WHITE,
                             (p.getColor()==BoardField.WHITE) ? Color.WHITE : Color.BLACK);
         
         // wykonanie ruchu przez gracza
         p.makeMove();   
         
         // żeby uniknąć wypisywanie komunikatów jeżeli przerwano w trakcie ruchu
         if (gameState!=Game.RUN) break;
         
         // dokończenie komunikatu na konsoli - wykonany ruch 
         console.setMessageLn("  \u279C  " + BoardGraphics.getFieldName(lBoard.lastField.getA(),
                              lBoard.lastField.getB(), settings.getColsAndRows()), Color.RED);
         // dźwięk położenia kamienia
         sounds.play(Sounds.SND_MOVE);
         
         // sprawdzenie warunków końca gry (wygrana lub remis)
         winRow = lBoard.getPieceRows();
         if ((winRow != null || lBoard.freeFieldsAmount==0)) {
             
           // komunikat o wygranej 
           if (winRow != null) {
               
             console.newLine();  
             console.setMessageLn("WYGRYWA " + p.getName().toUpperCase() + " !!!", Color.RED);
             gBoard.setPiecesRow(winRow, p.getColor());
             
           }
           
           // komunikat o remisie
           else {
               
             console.newLine();  
             console.setMessageLn("REMIS!", Color.RED);  
             
           }

           // na wypadek wymuszonego zakończenia ruchu - przywrócenie domyślnego kursora myszy
           gBoard.setDefaultMouseCursor();
           // kończący dzwonek :-)
           sounds.play(Sounds.SND_SUCCESS);
           // zatrzymanie rozgrywki
           gameState=Game.WAIT;
           
           
             
         }
         
         
         // odłączenie od serwera
         if (gameState!=Game.RUN && gameMode==NETWORK_GAME && client!=null) {
             
           try {
             client.sendCommand(new Command(Command.CMD_EXIT));
           } catch (Exception e) {}
         
           client.endGame();
           
         }
         
           
         moveNo++;
         
         
         
       }
        
        
     } 
     
     // przywrócenie domyślnego kursora w razie przerwania gry sieciowej
     if (gameMode == NETWORK_GAME && client != null)  {

         gBoard.setDefaultMouseCursor();
         
     }
     
     
     // pętla oczekiwania na rozpoczęcie nowej gry
     do {  
          
       try {
         // sleep jest po to, żeby podtrzymywać bieżący wątek
         Thread.sleep(10);
       } catch (InterruptedException e) {}
     
     } while (gameState!=Game.RESTART);
         
     
       
   }

 
   
   
   
}


