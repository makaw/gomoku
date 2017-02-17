/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import game.BoardField;
import java.awt.Image;

import javax.swing.ImageIcon;

/**
 *
 * Szablon obiektu udostępniającego obrazki - elementy graficzne do 
 * umieszczania na planszy
 * 
 * @author Maciej Kawecki
 * 
 */
public class ImageRes {
    
  /** Obrazek z czarnym kamieniem */
  private final static Image BLACK_PIECE = getImage("black.png");
  /** Obrazek z wyróżnionym czarnym kamieniem */
  private final static Image BLACK_PIECE_CHECKED = getImage("black_checked.png");
  /** Obrazek z białym kamieniem */
  private final static Image WHITE_PIECE = getImage("white.png");   
  /** Obrazek z wyróżnionym białym kamieniem */
  private final static Image WHITE_PIECE_CHECKED = getImage("white_checked.png");
  /** Obrazek z białym kursorem planszy */
  private final static Image CURSOR_WHITE_PIECE = getImage("cursor_white.png");
  /** Obrazek z czarnym kursorem planszy */
  private final static Image CURSOR_BLACK_PIECE = getImage("cursor_black.png");
  
  /** Szerokość obrazka z kamieniem w pikselach */
  protected final static int IMG_PIECES_WIDTH = 20;
  /** Wysokość obrazka z kamieniem w pikselach */
  protected final static int IMG_PIECES_HEIGHT = 20;

  private ImageRes() {}
  

  /**
   * Metoda pobierająca obrazek z odpowiednim kamieniem do umieszczenia na planszy
   * @param pColor Kolor kamienia
   * @param checked Jeżeli true kamień wyróżniony, jeżeli false to nie
   * @return Obrazek z odpowiednim kamieniem
   */
  public static Image getImgPiece(byte pColor, boolean checked) {
      
    if (checked)
      return (pColor==BoardField.WHITE) ?  WHITE_PIECE_CHECKED : BLACK_PIECE_CHECKED;
    else
      return (pColor==BoardField.WHITE) ?  WHITE_PIECE : BLACK_PIECE;  
 
  }

  
  /**
   * Metoda pobierająca obrazek kursora planszy 
   * @param pColor Kolor kamienia w kursorze
   * @return Obrazek kursora planszy
   */
  public static Image getImgCursor(byte pColor) {
      
     return (pColor==BoardField.WHITE) ? CURSOR_WHITE_PIECE : CURSOR_BLACK_PIECE;
      
  }
  
  
  /**
   * Statyczna metoda pobierająca ikonę ze wskazanego pliku z /resources/img
   * @param fileName Nazwa pliku z /resources/img/
   * @return Ikona wczytana ze wskazanego pliku
   */
  public static ImageIcon getIcon(String fileName) {
      
    ImageIcon icon = null; 
      
    try {    	
      icon = (new ImageIcon(ImageRes.class.getResource("/img/"+fileName)));     
    }
    catch (NullPointerException e) {
       try {
    	 icon = (new ImageIcon(ImageRes.class.getResource("/resources/img/"+fileName))); 
       }
       catch (NullPointerException ex) {
         System.err.println("Brak pliku /resources/img/"+fileName);
       }
    }
        
    return icon;  
    
  }
  
 
   
  /**
   * Statyczna metoda pobierająca obrazek ze wskazanego pliku z /resources/img
   * @param fileName Nazwa pliku z /resources/img/
   * @return Obrazek wczytany ze wskazanego pliku
   */ 
  public static Image getImage(String fileName) {
      
    ImageIcon ico = getIcon(fileName);  
    return (ico != null) ? getIcon(fileName).getImage() : null;
      
  }  
  
  
  
  

}
