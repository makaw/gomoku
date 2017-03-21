/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui.dialogs;

import javax.swing.Icon;

import gui.ImageRes;


/**
*
* Typy okienek informacyjnych (ikony)
* 
* @author Maciej Kawecki
* 
*/
public enum DialogType {
	
  /** Ostrzeżenie / błąd */	
  WARNING, 
  /** Potwierdzenie */
  CONFIRM,
  /** Zapytanie */
  PROMPT, 
  /** Wygrana :-) */
  WIN, 
  /** Przegrana :-( */
  LOOSE, 
  /** Remis :-/ */
  DRAW; 
	   
	  
  /**
   * Zwraca odpowiednią ikonę	
   * @return Ikona
   */
  public Icon getIcon() {
		  
	 switch (this) {
		 
	   default: 
	   case CONFIRM:
	   case PROMPT:		return ImageRes.getIcon("question.png");
	   case WARNING: 	return ImageRes.getIcon("warning.png");
	   case WIN: 		return ImageRes.getIcon("win.png");
	   case LOOSE:		return ImageRes.getIcon("loose.png");
	   case DRAW:		return ImageRes.getIcon("draw.png");
		 
	 }
		  
		  
  }
	   
}
	