/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package network;

import java.awt.Color;
import java.io.ObjectOutputStream;
import java.net.Socket;

import gomoku.Lang;
import gui.BaseConsole;


/**
*
* Wątek odrzucający próby połączenia gdy jest już komplet klientów
* 
* @author Maciej Kawecki
* 
*/
public class ServerDenyThread extends Thread {

  /** Referencja do obiektu serwera */
  private final Server server;
  /** Konsola GUI serwera */
  private final BaseConsole console;
	  
  
  
  /**
   * Konstruktor 
   * @param server Ref. do obiektu serwera
   * @param console Konsola GUI serwera
   */
  public ServerDenyThread(Server server, BaseConsole console) {
	      
	this.server = server;
	this.console = console;
	setDaemon(true);
	      
  }	
  
  
  @Override
  public void run() {
	
	 do { 
	  
	   try {  
		       
	     Socket socket = server.getServerSocket().accept();			          
	     ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
	     out.writeObject(new Command(Command.CMD_EXIT));
	     out.flush();
	              
	     console.setMessageLn(Lang.get("ConnectionWithXRejected", socket.getInetAddress()),
	    		 Color.RED);
	     
	   }        
	        
	   catch (Exception e) {	        
	     return;	            
	   }
	   
	   
	   try {
		 Thread.sleep(50);
	   }
	   catch (InterruptedException e) {
		 return;
	   }
	   
	   
	 } while (true);

  }
	
	
}
