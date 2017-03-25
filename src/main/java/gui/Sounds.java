/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import java.applet.Applet;
import java.applet.AudioClip;

import gomoku.IConf;

/**
 *
 * Odtwarzanie dźwięków
 * 
 * @author Maciej Kawecki
 * 
 */
public class Sounds {
    

   /** Stała używana do oznaczenia klipu dźwiękowego, odtwarzanego po wykonaniu ruchu */
   public final static byte SND_MOVE = 0;
   /** Stała używana do oznaczenia klipu dźwiękowego, odtwarzanego w razie wygranej lub remisu */
   public final static byte SND_SUCCESS = 1;
   /** Stała używana do oznaczenia klipu dźwiękowego, odtwarzanego po rozpoczęciu nowej gry */
   public final static byte SND_INFO = 2;    
    
   /** Pliki dźwiękowe */
   private final static AudioClip[] audioClips = new AudioClip[] {
	   loadSoundFile("move.wav"), 
	   loadSoundFile("info.wav"), 
	   loadSoundFile("success.wav")
   };
   
   /** Flaga zezwolenia na odtwarzanie dźwięków */
   private boolean enabled = IConf.DEFAULT_ENABLE_SOUND;
   
   
   /**
    * Metoda przełączająca flagę zezwolenia na odtwarzanie dźwięków na wartość przeciwną
    */
   public void toggleSound() {
       
     enabled = !enabled;  
       
   }
   

   public boolean isEnabled() {
       
     return enabled;  
       
   }
   
   /**
    * Metoda pobierająca klip dźwiękowy ze wskazanego pliku z /resources/snd
    * @param fileName Nazwa pliku z /resources/snd
    * @return Klip dźwiękowy ze wskazanego pliku
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
         System.err.println("Brak pliku /resources/img/"+fileName);
       }
     }          
     
     return ac;
     
   }   
   

   /**
    * Metoda odtwarzająca (w nowym wątku) wskazany klip dźwiękowy
    * @param snd Klip dźwiękowy, który ma być odtworzony (ruch, wygrana, informacja)
    */
   public synchronized void play(final byte snd) { 
       
      if (!enabled) return;
      
      
      // odtwarzanie wybranego dźwięku w nowym wątku
      new Thread(new Runnable() {  
    	  
         @Override
         public void run() {
       
            audioClips[snd].play();
          
         } 
      
      }).start();
      
   }
   
 
    
}
