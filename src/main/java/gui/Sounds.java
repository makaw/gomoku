/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import java.applet.Applet;
import java.applet.AudioClip;

import gomoku.IConf;
import gomoku.Lang;

/**
 *
 * Odtwarzanie dźwięków
 * 
 * @author Maciej Kawecki
 * 
 */
public class Sounds {
    
   /** Wykonanie ruchu */
   public final static byte SND_MOVE = 0;
   /** Wygrana lub remis */
   public final static byte SND_SUCCESS = 1;
   /** Rozpoczęcie nowej gry */
   public final static byte SND_INFO = 2;    
    
   /** Pliki dźwiękowe */
   private final static AudioClip[] audioClips = new AudioClip[] {
	   loadSoundFile("move.wav"), 
	   loadSoundFile("info.wav"), 
	   loadSoundFile("success.wav")
   };
   
   /** Zezwolenie na odtwarzanie dźwięków */
   private boolean enabled = IConf.DEFAULT_ENABLE_SOUND;
   
   
   /**
    * Metoda przełączająca flagę zezwolenia  
    */
   public void toggleSound() {
       
     enabled = !enabled;  
       
   }
   

   public boolean isEnabled() {
       
     return enabled;  
       
   }
   
   /**
    * Metoda pobierająca klip dźwiękowy ze wskazanego pliku 
    * @param fileName Nazwa pliku z /resources/snd
    * @return Klip dźwiękowy  
    */
   private static AudioClip loadSoundFile(String fileName) {
       
     AudioClip ac = null;
     
     try {    	
       ac = Applet.newAudioClip(Sounds.class.getResource("/snd/"+fileName));     
     }
     catch (NullPointerException e) {
       try {
         ac = Applet.newAudioClip(Sounds.class.getResource("/resources/snd/"+fileName)); 
       }
       catch (NullPointerException ex) {
         System.err.println(Lang.get("FileNotFound", "/resources/img/"+fileName));
       }
     }          
     
     return ac;
     
   }   
   

   /**
    * Metoda odtwarzająca (w nowym wątku) wskazany klip dźwiękowy
    * @param snd Nr klipu
    */
   public synchronized void play(final byte snd) { 
       
      if (!enabled) return;
      
      
      // odtwarzanie wybranego dźwięku w nowym wątku
      new Thread(new Runnable() {  
    	  
         @Override
         public void run() {
       
        	try { 
              audioClips[snd].play();
        	}
        	catch (Exception e) {
        	  System.err.println(e);	
        	}
              
         } 
      
      }).start();
      
   }
   
 
    
}
