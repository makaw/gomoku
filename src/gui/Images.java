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
public class Images {
    
  /** Obrazek z czarnym kamieniem */
  private final Image blackPiece;
  /** Obrazek z wyróżnionym czarnym kamieniem */
  private final Image blackPieceChecked;
  /** Obrazek z białym kamieniem */
  private final Image whitePiece;
  /** Obrazek z wyróżnionym białym kamieniem */
  private final Image whitePieceChecked;
  /** Obrazek z białym kursorem planszy */
  private final Image cursorWhitePiece;
  /** Obrazek z czarnym kursorem planszy */
  private final Image cursorBlackPiece;
  
  /** Szerokość obrazka z kamieniem w pikselach */
  protected static int imgPiecesWidth = 20;
  /** Wysokość obrazka z kamieniem w pikselach */
  protected static int imgPiecesHeight = 20;

  /**
   * Konstruktor wczytujący obrazki elementów planszy do wewnętrznych pól 
   */
  protected Images() {
      
    blackPiece = getImage("black.png");
    blackPieceChecked = getImage("black_checked.png");
    whitePiece = getImage("white.png");   
    whitePieceChecked = getImage("white_checked.png");
    cursorWhitePiece = getImage("cursor_white.png");
    cursorBlackPiece = getImage("cursor_black.png");
    
  }

  /**
   * Metoda pobierająca obrazek z odpowiednim kamieniem do umieszczenia na planszy
   * @param pColor Kolor kamienia
   * @param checked Jeżeli true kamień wyróżniony, jeżeli false to nie
   * @return Obrazek z odpowiednim kamieniem
   */
  public Image getImgPiece(byte pColor, boolean checked) {
      
    if (checked)
      return (pColor==BoardField.WHITE) ?  whitePieceChecked : blackPieceChecked;
    else
      return (pColor==BoardField.WHITE) ?  whitePiece : blackPiece;  
 
  }

  /**
   * Metoda pobierająca obrazek kursora planszy 
   * @param pColor Kolor kamienia w kursorze
   * @return Obrazek kursora planszy
   */
  public Image getImgCursor(byte pColor) {
      
     return (pColor==BoardField.WHITE) ? cursorWhitePiece : cursorBlackPiece;
      
  }
  
  /**
   * Statyczna metoda pobierająca ikonę ze wskazanego pliku z /resources/img
   * @param fileName Nazwa pliku z /resources/img/
   * @return Ikona wczytana ze wskazanego pliku
   */
  public static ImageIcon getIcon(String fileName) {
      
    ImageIcon icon = null; 
      
    try {
      icon = (new ImageIcon(Images.class.getResource("/resources/img/"+fileName)));
    }
    catch (NullPointerException e) {
       System.err.println("Brak pliku /resources/img/"+fileName);      
    }
        
    return icon;  
    
  }
  
  
   
  /**
   * Statyczna metoda pobierająca obrazek ze wskazanego pliku z /resources/img
   * @param fileName Nazwa pliku z /resources/img/
   * @return Obrazek wczytany ze wskazanego pliku
   * @see Images#getIcon(java.lang.String) 
   */ 
  public static Image getImage(String fileName) {
      
    return getIcon(fileName).getImage();
      
  }  
  
  
  

}
