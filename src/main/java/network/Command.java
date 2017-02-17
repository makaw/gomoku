/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package network;

import java.io.Serializable;

/**
 *
 * Szablon obiektu komendy przesyłanej pomiędzy serwerem gry i klientem
 * 
 * @author Maciej Kawecki
 * 
 */
public class Command implements Serializable {
    
   /** Stała używana do oznaczenia komendy: POTWIERDZENIE  */
   public final static byte CMD_OK = 1;
   /** Stała używana do oznaczenia komendy: ZAKOŃCZ */
   public final static byte CMD_EXIT = 2;
   /** Stała używana do oznaczenia komendy: START */
   public final static byte CMD_START = 3;
   /** Stała używana do oznaczenia komendy: PING */
   public final static byte CMD_PING = 4;
   /** Stała używana do oznaczenia komendy: ZAKOŃCZ ODBIÓR WIADOMOŚCI */
   public final static byte CMD_STOP_MSG = 5;
   /** Stała używana do oznaczenia komendy: RUCH */
   public final static byte CMD_MOVE = 11;
   /** Stała używana do oznaczenia komendy: WIADOMOŚĆ */
   public final static byte CMD_MESSAGE = 12;       
   /** Stała używana do oznaczenia komendy: USTAWIENIA */
   public final static byte CMD_SETTINGS = 21;
   /** Stała używana do oznaczenia komendy: DAJ NUMER */
   public final static byte CMD_NUMBER = 22;     
        
    
   /** Komenda */ 
   private final byte command;
   /** Dane związane z komendą */
   private Object commandData;

   private final static long serialVersionUID = 1L;

   /**
    * Konstruktor inicjalizujący wewnętrzne pola komendy i związanego z nią obiektu
    * @param command komenda
    * @param commandData obiekt związany z komendą
    */   
   public Command(byte command, Object commandData) {
       
     this.command = command;
     this.commandData = commandData;
     
   }
   
   
   /**
    * Konstruktor inicjalizujący wewnętrzne pole komendy
    * @param command komenda
    */
   public Command(byte command) {
       
     this(command, null);
     
   }

   

   /**
    * Metoda zwracająca komendę
    * @return komenda
    */
   public byte getCommand() {
       
     return command;
    
   }
   

   /**
    * Metoda zwracająca związany z komendą obiekt
    * @return związany z komendą obiekt
    */
   public Object getCommandData() {
       
     return commandData;
     
   }

    
    
}
