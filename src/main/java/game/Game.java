/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import gomoku.AppObserver;
import gomoku.Settings;
import gui.BoardGraphics;
import gui.Console;
import gui.GUI;
import gui.Sounds;
import network.Client;
import network.Command;


/**
 *
 * Szablon obiektu kontrolującego przebieg rozgrywki
 * 
 * @author Maciej Kawecki
 * 
 */
public class Game extends Thread implements Observer {

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
   private GameState gameState;   
   /** Podany przez użytkownika adres IP serwera do przekazania do obiektu klienta */
   private String serverIP;
   /** Referencja do obiektu służącego do komunikacji z tym wątkiem, 
    * potrzebna do ustawienia obserwacji stanu gry przez wątek serwera */
   private final AppObserver gameSpy;
   /** Obiekt klienta w grze siociowej */
   private Client client;
   /** Tryb gry */
   private GameMode gameMode;
   /** Ustawienia */
   private Settings settings;
   
   
   /**
    * Konstruktor obiektu odpowiedzialnego za przebieg rozgrywki, przypisuje referencje do obiektów GUI
    * @param gBoard Referencja do graficznej reprezentacji planszy
    * @param console Referencja do obiektu reprezentującego konsolę do wyświetlania komunikatów
    * @param sounds Referencja do obiektu służącego do odtwarzania dźwięków
    * @param gameSpy Referencja do obiektu służącego do komunikacji z tym wątkiem, 
    * potrzebna do ustawienia obserwacji stanu gry przez wątki sieciowe
    */
   public Game(BoardGraphics gBoard, Console console, Sounds sounds, AppObserver gameSpy) {
     
	 super();
	 setDaemon(true);
	   
     this.gBoard = gBoard;
     this.console = console;
     this.sounds = sounds;
     this.gameSpy = gameSpy;     
         
   }
   
   /**
   * Metoda ustawia referencje przekazane przez obserwatora
   * @param o Obserwowany obiekt 
   * @param object Przekazany obiekt
   */
   @Override
   public void update(Observable o, Object object) {
       
     AppObserver obs = (AppObserver)object;
     
     switch (obs.getKey()) {
   
        // zmieniono stan gry
        case "state":  
       
        	this.gameState = (GameState)obs.getObject();
                   
            // wymuszenie końca ruchu graczy
            if (player1!=null) player1.forceEndTurn();
            if (player2!=null) player2.forceEndTurn();
            // zmiana adresu IP serwera
            this.serverIP = gameState.getServerIP();
            console.networkButtonsEnable(false);    
                
            break;
            
        // zmieniono ustawienia, zmiana ref. do graficznej planszy  
        case "board":
            
           setBoard((BoardGraphics)obs.getObject());
           
           break;
           
           
           
        // przesłanie wiadomości
        case "message":
            
           String msg = (String)obs.getObject();
           
           try {
             
             String txt = "[Gracz "+(client.getNumber()+1)+"]: " + msg;
             client.sendCommand(new Command(Command.CMD_MESSAGE, txt));
             console.newLine();
             console.setMessageLn("Wys\u0142ano: "+msg, Color.GRAY);
            
           } catch (IOException | ClassNotFoundException e) {
              System.err.println(e);
           }
        
           break;
           
     }
     
   
     
   }
   
   /**
    * Metoda zwracająca aktualny stan gry
    * @return Aktualny stan gry
    */
   public GameState getGameState() {
       
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
   public void startNewGame(GameMode gameMode, Settings settings) {
   
       
     gameState = GameState.RUN;
     // logika planszy dla przekazanych ustawień
     lBoard = new BoardLogic(settings);

     // przypisanie odpowiednich implementacji gracza w zależności od trybu rozgrywki
     if (gameMode != null)
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
                
              client = new Client(serverIP, gameSpy, console);  
              // jeżeli się udało połączyć, to zmiana ustawień gry
              Settings clientSettings = client.getSettings();
              ((GUI)(SwingUtilities.getWindowAncestor(console)))
                      .restartClientGameSettings(clientSettings.getColsAndRows());
              settings.setSettings(clientSettings.getColsAndRows(), clientSettings.getPiecesInRow(), 
                      clientSettings.getPiecesInRowStrict());   
              gameSpy.sendObject("settings-main", clientSettings);
              
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
              
              
              console.networkButtonsEnable(true);
              
            } catch (IOException e) {
               
               try {
                  Thread.sleep(100);  
               } 
               catch (Exception ex) {} 
               
               console.setMessageLn("Pr\u00f3ba po\u0142\u0105czenia nieudana.", Color.RED); 
               console.networkButtonsEnable(false);
               player1 = null;
               player2 = null;
               
                
            } catch (ClassNotFoundException e) {             
               System.err.println(e);
            }
            

            break;
             
         
     }
     
     
     // zatrzymanie, jeżeli brakuje graczy lub nie ma połąćzenia przy grze sieciowej
     if (player1==null || player2==null) {
           
         gameState=GameState.WAIT;
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
     while (gameState==GameState.RUN) {  
                    
       // sekwencja zdarzeń dla każdego z graczy  
       for(Player p:players) if (gameState==GameState.RUN) {
         
         // komunikat na konsoli
         console.setMessage("Ruch #" + Integer.toString(moveNo) + ": ", Color.BLUE);
         console.setMessage(p.getName(), (p.getColor()==BoardField.WHITE) ? Color.BLACK : Color.WHITE,
                             (p.getColor()==BoardField.WHITE) ? Color.WHITE : Color.BLACK);
         
         // wykonanie ruchu przez gracza
         p.makeMove();   
         
         // żeby uniknąć wypisywanie komunikatów jeżeli przerwano w trakcie ruchu
         if (gameState!=GameState.RUN) break;
         
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
           gameState=GameState.WAIT;
           
           
             
         }
         
         
         // odłączenie od serwera
         if (gameState!=GameState.RUN && gameMode==GameMode.NETWORK_GAME && client!=null) {
             
           try {
             client.sendCommand(new Command(Command.CMD_EXIT));
           } catch (Exception e) {}
         
           client.endGame();
           
         }
         
           
         moveNo++;
         
         
         
       }
        
        
     } 
     
     // przywrócenie domyślnego kursora w razie przerwania gry sieciowej
     if (gameMode == GameMode.NETWORK_GAME && client != null)  {

         gBoard.setDefaultMouseCursor();
         
     }
     
     
     // pętla oczekiwania na rozpoczęcie nowej gry
     do {  
          
       try {         
         Thread.sleep(10);
       } catch (InterruptedException e) { return; }
     
     } while (gameState!=GameState.RESTART);
         
     
       
   }
   
   
   public void run() {	   
	   
	  super.run(); 
	  startNewGame(gameMode, settings);
	   
   }
   
   

   public void setGameMode(GameMode gameMode) {
	 this.gameMode = gameMode;
   }

   public void setSettings(Settings settings) {
	 this.settings = settings;
   }

 
   
   
   
}


