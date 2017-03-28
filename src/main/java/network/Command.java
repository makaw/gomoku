/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package network;

import java.io.Serializable;

/**
 *
 * Komenda przesyłana pomiędzy serwerem gry i klientem
 * 
 * @author Maciej Kawecki
 * 
 */
public class Command implements Serializable {
    
   /** Komenda: POTWIERDZENIE  */
   public final static byte CMD_OK = 1;
   /** Komenda: ZAKOŃCZ */
   public final static byte CMD_EXIT = 2;
   /** Komenda: START */
   public final static byte CMD_START = 3;
   /** Komenda: PING */
   public final static byte CMD_PING = 4;
   /** Komenda: ZAKOŃCZ ODBIÓR WIADOMOŚCI */
   public final static byte CMD_STOP_MSG = 5;
   /** Komenda: RUCH */
   public final static byte CMD_MOVE = 11;
   /** Komenda: WIADOMOŚĆ */
   public final static byte CMD_MESSAGE = 12;       
   /** Komenda: USTAWIENIA */
   public final static byte CMD_SETTINGS = 21;
   /** Komenda: DAJ NUMER */
   public final static byte CMD_NUMBER = 22;     
   /** Komenda: KOMPLET GRACZY */
   public final static byte CMD_FULL = 23;
    
   /** Komenda */ 
   private final byte command;
   /** Dane związane z komendą */
   private Object commandData;

   private final static long serialVersionUID = 1L;

   /**
    * Konstruktor 
    * @param command komenda
    * @param commandData obiekt związany z komendą
    */   
   public Command(byte command, Object commandData) {
       
     this.command = command;
     this.commandData = commandData;
     
   }
   
   
   /**
    * Konstruktor 
    * @param command komenda
    */
   public Command(byte command) {
       
     this(command, null);
     
   }

   
   public byte getCommand() {
       
     return command;
    
   }
   

   public Object getCommandData() {
       
     return commandData;
     
   }

    
    
}
