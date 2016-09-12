/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import gomoku.IConf;
import java.applet.Applet;
import java.applet.AudioClip;

/**
 *
 * Szablon obiektu służącego do odtwarzania dźwięków
 * 
 * @author Maciej Kawecki
 * 
 */
public class Sounds {
    

   /** Stała używana do oznaczenia klipu dźwiękowego, odtwarzanego po wykonaniu ruchu */
   public final static byte SND_MOVE = 1;
   /** Stała używana do oznaczenia klipu dźwiękowego, odtwarzanego w razie wygranej lub remisu */
   public final static byte SND_SUCCESS = 2;
   /** Stała używana do oznaczenia klipu dźwiękowego, odtwarzanego po rozpoczęciu nowej gry */
   public final static byte SND_INFO = 3;    
    
   /** Klip dźwiękowy odtwarzany po wykonaniu ruchu - postawieniu kamienia na planszy */
   private final AudioClip sndMove;
   /** Klip dźwiękowy odtwarzany po zakończeniu rozgrywki przez wygraną lub remis */
   private final AudioClip sndSuccess;
   /** Klip dźwiękowy odtwarzany po rozpoczęciu nowej gry */
   private final AudioClip sndInfo;
   /** Flaga zezwolenia na odtwarzanie dźwięków */
   private boolean enabled;
   
   /**
    * Konstruktor wczytujący pliki dźwiękowe i ustawiający flagę zezwolenia na odtwarzanie na wartość domyślną
    * @see game.Settings#DEFAULT_ENABLE_SOUND
    */
   public Sounds() {
       
     sndMove = loadSoundFile("move.wav");
     sndInfo = loadSoundFile("info.wav");
     sndSuccess = loadSoundFile("success.wav");
     enabled = IConf.DEFAULT_ENABLE_SOUND;
       
   }
   
   /**
    * Metoda przełączająca flagę zezwolenia na odtwarzanie dźwięków na wartość przeciwną
    */
   public void toggleSound() {
       
     enabled = !enabled;  
       
   }
   
   /**
    * Metoda zwracająca wartość flagi zezwolenia na odtwarzanie dźwięków
    * @return true jeżeli odtwarzanie dźwięków jest włączone, false jeżeli nie
    */
   public boolean getEnabled() {
       
     return enabled;  
       
   }
   
   /**
    * Metoda pobierająca klip dźwiękowy ze wskazanego pliku z /resources/snd
    * @param fileName Nazwa pliku z /resources/snd
    * @return Klip dźwiękowy ze wskazanego pliku
    */
   private AudioClip loadSoundFile(String fileName) {
       
     AudioClip ac = null;
     try {
       ac = Applet.newAudioClip(getClass().getResource("/resources/snd/"+fileName));
     }
     catch (NullPointerException e) {
       System.err.println("Brak pliku /resources/snd/"+fileName);      
     }
     
     return ac;
     
   }   

   /**
    * Metoda odtwarzająca (w nowym wątku) wskazany klip dźwiękowy
    * @param snd Klip dźwiękowy, który ma być odtworzony (ruch, wygrana, informacja)
    */
   public synchronized void play(final byte snd) { 
       
      final AudioClip clip;
      
      if (!enabled) return;
      
      switch(snd) {
          
          default: return;
          case SND_MOVE:
            clip = sndMove;
            break;
          case SND_SUCCESS:
            clip = sndSuccess;
            break;
          case SND_INFO:
            clip = sndInfo;
            break;              
      }
      
      if (clip==null) return;
      
      // odtwarzanie wybranego dźwięku w nowym wątku
      new Thread(new Runnable() {  
         @Override
         public void run() {
       
            clip.play();
          
         } 
      
      }).start();
      
   }
   
 
    
}
