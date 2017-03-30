/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package network;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import gomoku.AppObserver;
import gomoku.IConf;
import gomoku.Lang;
import gomoku.Settings;
import gui.BaseConsole;
import gui.GUI;
import gui.ServerGUI;
import gui.dialogs.DialogType;
import gui.dialogs.InfoDialog;


/**
 *
 * Serwer gry.
 * Klasa główna zawierająca metodę main() - wywołanie interfejsu graficznego, 
 * uruchomianie serwera. 
 * 
 * @author Maciej Kawecki
 * 
 */
public class Server  implements Observer {
    
  /** Gniazdko serwera */
  private ServerSocket serverSocket;  
  /** Tablica strumieni wyjściowych */
  private final ObjectOutputStream[] output;
  /** Tablica strumieni wejściowych */
  private final ObjectInputStream[] input;
  /** Lista wątków klientów */
  private final List<ServerThread> serverThreadList;
  /** Lista gniazdek klienckich */
  private final List<Socket> serverSocketList;
  /** Lista timerów */
  private final List<Ping> serverPingList;
  /** True jeżeli konieczny jest restart, false jeżeli nie */
  private boolean restart;
  /** Obserwator do komunikacji z innymi wątkami */
  private AppObserver serverSpy;
  /** Referencja do GUI serwera */
  private ServerGUI gui;
  /** Ustawienia gry po stronie serwera */
  private final Settings settings;
  
  
  /**
   * Konstruktor
   * @throws InterruptedException Problem z uruchomieniem wątku GUI
   * @throws InvocationTargetException Problem z uruchomieniem wątku GUI
   */
  private Server() throws InterruptedException, InvocationTargetException {
      
    this.output = new ObjectOutputStream[2];
    this.input = new ObjectInputStream[2];
    
    serverSpy = new AppObserver();
    serverSpy.addObserver(this);
    serverSocketList = new ArrayList<>();
    serverThreadList = new ArrayList<>();
    serverPingList = new ArrayList<>();
    
    // ustawia wartości domyślne
    settings = new Settings();
    settings.load();
  	
    // operacje przy zakończeniu
  	Runtime.getRuntime().addShutdownHook(new Thread() {  	
  	  @Override
      public void run() {
  	    try {  	       
  	      Server.this.free();  
  	    }
  	    catch (Exception e) {}		  
  	  }  		
  	});  	
    
    GUI.setLookAndFeel();
    
    // bezpieczne wywołanie interfejsu graficznego  
    SwingUtilities.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        gui = new ServerGUI(serverSpy, settings); 
      }
    });        
        
  }
    
  
  /**
   * Metoda ustawia referencję do strumienia wyjściowego dla danego klienta
   * @param clientNumber Numer klienta
   * @param output Strumień wyjściowy
   */
  public void setOutputStream(int clientNumber, ObjectOutputStream output) {
      
    this.output[clientNumber] = output;
      
  }
  
  /**
   * Metoda ustawia referencję do strumienia wejściowego dla danego klienta
   * @param clientNumber Numer klienta
   * @param input Strumień wejściowy
   */
  public void setInputStream(int clientNumber, ObjectInputStream input) {
      
    this.input[clientNumber] = input;
      
  }  
  
  /**
   * Metoda zwraca referencję do strumienia wyjściowego dla danego klienta
   * @param clientNumber Numer klienta
   * @return Referencja do strumienia wyjściowego
   */
  public ObjectOutputStream getOutputStream(int clientNumber) {
      
    
    try {
        
      return output[clientNumber];
      
    }
    catch (ArrayIndexOutOfBoundsException e) {
       
       serverRestart();
       return null;
        
    }
        
  }
  
  
  /**
   * Metoda zwraca referencję do strumienia wejściowego dla danego klienta
   * @param clientNumber Numer klienta
   * @return Referencja do strumienia wejściowego
   */  
  public ObjectInputStream getInputStream(int clientNumber) {
    
    try {
        
      return input[clientNumber];   
        
    }
      
    catch (ArrayIndexOutOfBoundsException e) {
       
       serverRestart();
       return null;
        
    }
      
  }  
  
  

  protected ServerSocket getServerSocket() {
      
    return serverSocket;  
      
  }
  
  
  /**
   * Metoda zapamiętuje gniazdko klienta do późniejszego uruchomienia dedykowanego wątku serwera
   * @param socket Gniazdko klienta
   */
  private void addNewSocket(Socket socket) {
      
     serverSocketList.add(socket);        
      
  }
  
  
  
  /**
   * Metoda uruchamia wątki serwera dla każdego z podłączonych wcześniej klientów
   */
  private void startServerThreads() {
      
    int n = 0;  
    
    try {
        
      for (Socket s: serverSocketList) {
        
        // przesłanie powitania do klienta 
        gui.getConsole().setMessageLn(Lang.get("WelcomeMessageToClient", n+1), new Color(0xa5, 0x2a, 0x2a));
        ObjectOutputStream out = getOutputStream(n); 
        try {
           out.writeObject(new Command(Command.CMD_START));
           out.flush();
        } catch (IOException ex) {
           // tylko wyjście, restart będzie obsłużony przez Ping
           return;
        }
      
        ServerThread sThread = new ServerThread(s, this, n);
        serverThreadList.add(sThread);
        sThread.start();
        n++;
     
      }
    }
    // wyjątek rzucany, jeżeli w trakcie podłączania drugiego klienta, pierwszy klient 
    // się rozłączył i poszedł restart serwera; nic nie trzeba robić, wystarczy nie blokować
    catch (ConcurrentModificationException e) {}
           
      
  }
  

  protected AppObserver getServerSpy() {
      
    return serverSpy;  
      
  }
  
  

  protected Settings getSettings() {
      
    return settings;
      
  }
  
  
  /**
   * Metoda ustawia referencje przekazane przez obserwatora
   * @param o Obserwowany obiekt 
   * @param object Przekazany obiekt
   */
  @Override
  public void update(Observable o, Object object) {
       
    AppObserver obs = (AppObserver)object;
    
    SwingUtilities.invokeLater(new Runnable() { 
      @Override
      public void run() {
    	  
    	 switch (obs.getKey()) {           
     
           case "state":
            
             String val = (String)obs.getObject();
             if (val.equals("restart")) {
               gui.trayMessage(Lang.get("ServerRestarted")); 
               serverRestart();
             }
            
             break;
           
           
           case "ping-out":
        	
             try {	
               String msg = Lang.get("ConnectionWithXLost", 
              		  serverSocketList.get((int)obs.getObject()).getInetAddress());
               gui.getConsole().setMessageLn(msg, Color.RED);
               gui.trayMessage(msg + ". " + Lang.get("ServerRestarted"));
               serverRestart();
             }
             // już zrestartowane - FIX (2 klientów, kolejna gra)
             catch (IndexOutOfBoundsException e) {
               try {
                 free();
               } catch (Exception ex) {}
               restart = true;
             }

             break;
           
           
           case "settings":
            
             Settings s = (Settings)obs.getObject();
             settings.setGameSettings(s.getColsAndRows(), s.getPiecesInRow());                       
             serverRestart();
           
             break;
             
    	 }
           
       };
    });           
     
  }    
  
   
  /** 
   * Operacje przy restarcie/zamknięciu
   */
  private void free() {	  
		       
	for (Ping p: serverPingList) p.stopPinging();
	serverPingList.clear();	
	
	for (ServerThread t: serverThreadList) t.interrupt();
	for (Socket s: serverSocketList) {
	   try {
	     s.close();
	   } catch (Exception ex) {}
	   do {          
	     try {
	       Thread.sleep(50);
	     } catch (InterruptedException e) { }           
	   } while (!s.isClosed());
	}		       
	    
	serverThreadList.clear();
	serverSocketList.clear();
	  	  
  }
  
  
  /**
   * Liczba podłączonych klientów   
   * @return Liczba klientów
   */
  public int clientsNum() {
	 return serverSocketList.size();
  }
  
  
  /**
   * Restart serwera
   */  
  protected void serverRestart() {
      
	 gui.getConsole().setMessageLn(Lang.get("ServerRestarted"), Color.RED);
     gui.getConsole().newLine();
	      
     try {
       Thread.sleep(100);	 
       free();
     }
     // właśnie zwalnianie w innym wątku
     catch (ConcurrentModificationException e) {}
     catch (InterruptedException e) {}
     catch (NullPointerException e) {}
     
     try {
       serverSocket.close();
     } catch (Exception e) {}
       
     do {          
       try {
         Thread.sleep(50);
       } catch (InterruptedException e) { }           
     } while (!serverSocket.isClosed());
                
     setServerSocket();     

     restart = true;     

  }
  
  
  
  /**
   * Ustawienie gniazda
   */
  public void setServerSocket() {
      
    try { 
    	  
      serverSocket = new ServerSocket(IConf.SERVER_PORT);
        
    } catch (IOException e) {
    	  
      gui.getConsole().setMessageLn(e.getMessage(), Color.RED);
      new InfoDialog(gui, e.getMessage(), DialogType.WARNING);
      System.exit(0);
          
    }
      
  }
  
  
      
 /** 
   * Metoda main: uruchamia interfejs graficzny, oczekuje na podłączenie klientów, 
   * i uruchamia dedykowane dla nich wątki serwera.
   * Metoda jest wołana przez głowną klasę projektu Gomoku, w razie wywołania 
   * z parametrem -s.
   * @param args Parametry wejściowe (nie obsługiwane)
   * @see gomoku.Gomoku
   */
  public static void main(final String[] args) {
     
    Server server = null; 
      
    try {  
      server = new Server();    
    } catch (InterruptedException | InvocationTargetException e) {
      System.err.println(Lang.get("StartGraphicsProblem", e));
      System.exit(0);
    }
    
    ServerGUI frame = server.gui;
    BaseConsole console = frame.getConsole();
    console.setMessageLn("Gomoku Server v."+IConf.VERSION_SERVER, Color.GRAY);
    console.setMessageLn("--------------------------------", Color.GRAY);  
   
    server.setServerSocket();        
    
    SwingUtilities.invokeLater(new Runnable() {		
	  @Override
	  public void run() {
		frame.setTray();
	  }
	});
    
    do {
      
      try {
        Thread.sleep(50);
      } catch (Exception e) {}
      
      console.setMessageLn(Lang.get("WaitForConnectionsOnPort",
    		  String.valueOf(IConf.SERVER_PORT)), Color.DARK_GRAY);
      
      server.restart = false;
             
      while (!server.restart)  {    	  
          
        try {  
                 
           Socket socket = server.getServerSocket().accept();
           
           try {
          
            server.setInputStream(server.clientsNum(), new ObjectInputStream(socket.getInputStream()));
            server.setOutputStream(server.clientsNum(), new ObjectOutputStream(socket.getOutputStream()));                    

            String msg = Lang.get("ConnectionWithXAccepted", socket.getInetAddress());
            console.setMessageLn(msg, Color.BLUE);     
            frame.trayMessage(msg);
            
            Ping ping = new Ping(server, server.clientsNum());
            server.serverPingList.add(ping);
            ping.startPinging();
          
            server.addNewSocket(socket);
          
          
            if (server.clientsNum() == 2) {
              
               server.startServerThreads();
                    
               console.setMessageLn(Lang.get("TwoClientsAlready"), Color.BLACK);
               console.newLine();              
               server.gui.trayMessage(Lang.get("TwoClientsAlready"));
              
            }
          
          }      
           
           // jest już 2 klientów
           catch (ArrayIndexOutOfBoundsException e) {
           	          
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
          	 out.writeObject(new Command(Command.CMD_FULL));
          	 out.flush();     	    
           	
           }
       
        }
      
        
        catch (IOException e) {       
          server.restart = true;
      	  break;      	  
        }

        try {
          Thread.sleep(50);
        }
        catch (InterruptedException e) { return; }    

      }      
      
      
    }  while(true);
    
       
  }
  
  
  
 
}
  
  
  
  

  