/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import game.BoardField;
import game.BoardLogic;
import gomoku.IConf;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *
 * Szablon obiektu odpowiedzialnego za graficzną reprezentację planszy
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class BoardGraphics extends JLayeredPane {
    
  /** Szerokość i wysokość (w pikselach) pojedynczego pola planszy */ 
  private static final int PX_FIELD = 24;
  /** Lewy margines planszy w pikselach */
  private static final int PX_BOARD_MARGIN = 22;
  /** Szerokość całego panelu planszy w pikselach */
  private static final int B_WIDTH=400;
  /** Wysokość całego panelu planszy w pikselach */
  private static final int B_HEIGHT=400;  
  /** Ilość wierszy i kolumn planszy */
  private Integer colsAndRows;  
  /** Przechowanie poprzednio narysowanego na planszy kursora */
  private JLabel tmpCursor;

  
  /**
   * Konstruktor obiektu odpowiedzialnego za graficzną reprezentację planszy, 
   * rysuje planszę wykorzystując konstruktor klasy nadrzędnej oraz ustawia 
   * rozmiary planszy i ładuje obrazki kamieni i kursorów
   * @param colsAndRows Ilość kolumn i wierszy
   */
  public BoardGraphics(int colsAndRows) {
      
    super();  
    this.colsAndRows = colsAndRows;
    setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT)); 
    tmpCursor = null;

  }
  
  /**
   * Konstruktor z domyślną ilością kolumn i wierszy
   */
  public BoardGraphics() {
      
    this(IConf.DEFAULT_COLS_AND_ROWS);  
      
  }
  

  /**
   * Przeciążona metoda klasy nadrzędnej komponentu JLayeredPane, 
   * wykonywana w momencie wołania konstruktora klasy nadrzędnej.  
   * Wykorzystywana do narysowania planszy na komponencie.
   * @param g Referencja do urządzenia wyjściowego (ekran)
   */
  @Override
  public void paintComponent(Graphics g)  {
    
    Graphics2D g2D = (Graphics2D)g;   
    // rysowanie tla
    g2D.drawImage(ImageRes.getImage("bgboard.jpg"), 0, 0, this);
    
    // ustawienie grubosci linii, czcionki i koloru
    g2D.setStroke(new BasicStroke(1.5f));  
    g2D.setFont(new Font("Serif", Font.BOLD, 10));  
    g2D.setColor(Color.DARK_GRAY);
    // wlaczenie antyaliasingu
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                         RenderingHints.VALUE_ANTIALIAS_ON);

    int i,j;
   
    // rysowanie planszy (siatka i podpisy)
    for (i=0;i<colsAndRows;i++) {
        
      g2D.drawLine(PX_BOARD_MARGIN+i*PX_FIELD+12, PX_BOARD_MARGIN, PX_BOARD_MARGIN+i*PX_FIELD+12, 
                 PX_BOARD_MARGIN+(colsAndRows-1)*PX_FIELD);
      g2D.drawString(Character.toString((char)('A' + i)), PX_BOARD_MARGIN+i*PX_FIELD+9, 
                   PX_BOARD_MARGIN+(colsAndRows-1)*PX_FIELD+21);
    
      g2D.drawLine(PX_BOARD_MARGIN+12, PX_BOARD_MARGIN+i*PX_FIELD, 
                   PX_BOARD_MARGIN+(colsAndRows-1)*PX_FIELD+12, PX_BOARD_MARGIN+i*PX_FIELD);
      g2D.drawString(Integer.toString(colsAndRows-i), PX_BOARD_MARGIN-(i<6 ? 13:11), 
                  PX_BOARD_MARGIN+i*PX_FIELD+4);

    }
   
    //  kropki na planszy dla oryg. wymiarów 15x15
    if (colsAndRows == IConf.DEFAULT_COLS_AND_ROWS) {
      for (i=3;i<=11;i+=4) for (j=3;j<=11;j+=4) 
        g2D.fillOval(24+PX_FIELD*i+8, 12+PX_FIELD*j+8, 6, 5);
    }
    
   
   
  }
 
  
  /**
   * Ogólna metoda ustawiająca element graficzny (kamień lub kursor) na planszy, oraz zmieniająca 
   * w obrębie planszy kursor myszy
   * @param lBoard Referencja do obiektu logicznej warstwy planszy (do sprawdzenia czy pole jest wolne)
   * @param a Indeks a (kolumna) pola
   * @param b Indeks b (wiersz) pola
   * @param pColor Kolor ustawianego kamienia lub kursora
   * @param pChecked true jeżeli kamień ma być wyróżniony (w razie wygranej), false jeżeli nie
   * @param cursor true jeżeli ustawiany jest kursor, false jeżeli kamień
   */
  private void setElement(BoardLogic lBoard, int a, int b, byte pColor, boolean pChecked, boolean cursor) {
      
     // usuniecie poprzedniego kursora-kamienia 
     if (tmpCursor!=null) remove(tmpCursor);
  
     // czy kursor myszy jest na planszy i czy nie wskazuje na zajete pole (nie dotyczy zaznaczonej linii)
     // !!!! czy nie trzeba Integer.compareTo ???
     if (a>=0 && b>=0 && colsAndRows.compareTo(a)>0 && colsAndRows.compareTo(b)>0 //a<colsAndRows && b<colsAndRows 
         && (pChecked || lBoard.getFieldState(a,b)==BoardField.EMPTY)) {
        
       // zmiana kursora myszy na 'lape'  
       if (getCursor().getType()!=Cursor.HAND_CURSOR) setCursor(new Cursor(Cursor.HAND_CURSOR));  
         
       // wstawienie odpowiedniego obrazka do komp.JLabel
       ImageIcon img = new ImageIcon(cursor ? ImageRes.getImgCursor(pColor) : ImageRes.getImgPiece(pColor, pChecked));
       JLabel piece = new JLabel(img); 
       // ustawienie pozycji komponentu JLabel i dodanie go do planszy
       piece.setBounds(24+PX_FIELD*a, 12+PX_FIELD*b, ImageRes.IMG_PIECES_WIDTH, ImageRes.IMG_PIECES_HEIGHT);
       add(piece);
       
       // przesuniecie na wierzch zaznaczonego rzedu (wygrana)
       if (pChecked) moveToFront(piece);
       
       // ustalenie referencji do obecnego kursora-kamienia, aby pozniej go usunac
       if (cursor) tmpCursor = piece;
       
     }
     
     else {
         
       // zmiana kursora myszy na domyslny (po wyjsciu poza plansze)  
       setDefaultMouseCursor(); 
         
     }
     
  }
  
  /**
   * Metoda przywracająca domyślny kursor myszy na planszy
   */
  public void setDefaultMouseCursor() {
      
    if (getCursor().getType()!=Cursor.getDefaultCursor().getType()) setCursor(Cursor.getDefaultCursor());  
      
  }

  /**
   * Metoda ustawiająca kursor myszy WAIT na planszy
   */
  public void setWaitMouseCursor() {
      
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      
  }


    
  public void setColsAndRows(Integer colsAndRows) {
      
    this.colsAndRows = colsAndRows;
    
  }
  
  
  
  
  
  /**
   * Metoda ustawiająca kamień na planszy
   * @param lBoard Referencja do obiektu logicznej warstwy planszy
   * @param a Indeks a (kolumna) pola
   * @param b Indeks b (wiersz) pola
   * @param pColor Kolor ustawianego kamienia
   * @param pChecked true jeżeli kamień ma być wyróżniony (w razie wygranej), false jeżeli nie
   * @see BoardGraphics#setElement(game.BoardLogic, int, int, byte, boolean, boolean) 
   */
  public void setPiece(BoardLogic lBoard, int a, int b, byte pColor, boolean pChecked) {
      
    setElement(lBoard, a, b, pColor, pChecked, false);  
      
  }
  
  /**
   * Metoda ustawiająca nie wyróżniony kamień na planszy
   * @param lBoard Referencja do obiektu logicznej warstwy planszy (do sprawdzenia czy pole jest wolne)
   * @param a Indeks a (kolumna) pola
   * @param b Indeks b (wiersz) pola
   * @param pColor Kolor ustawianego kamienia
   * @see BoardGraphics#setElement(game.BoardLogic, int, int, byte, boolean, boolean) 
   */  
  public void setPiece(BoardLogic lBoard, int a, int b, byte pColor) {
      
     setPiece(lBoard, a, b, pColor, false); 
      
  }
  
  /**
   * Metoda ustawiająca kursor na planszy
   * @param lBoard Referencja do obiektu logicznej warstwy planszy (do sprawdzenia czy pole jest wolne)
   * @param a Indeks a (kolumna) pola
   * @param b Indeks b (wiersz) pola
   * @param pColor Kolor ustawianego kursora
   * @see BoardGraphics#setElement(game.BoardLogic, int, int, byte, boolean, boolean) 
   */    
  public void setCursor(BoardLogic lBoard, int a, int b, byte pColor) {
      
     setElement(lBoard, a, b, pColor, false, true);  
      
  }  
  
  /**
   * Metoda ustawiająca na planszy rząd wyróżnionych kamieni w razie wygranej
   * @param list Lista indeksów pól składających się na wygrywający rząd
   * @param pColor Kolor ustawianych kamieni
   * @see BoardGraphics#setPiece(game.BoardLogic, int, int, byte, boolean) 
   */
  public void setPiecesRow(List<BoardField> list, byte pColor) {
        
    for(BoardField field:list) {
       
        setPiece(null, field.getA(), field.getB(), pColor, true);
        repaint();
        
    } 
      
  }
  
  /**
   * Usunięcie wszystkich elementów graficznych położonych na planszy
   */
  public void clear() {
    
      removeAll();
      
  }
  
  
  
  /**
   * Statyczna metoda przeliczająca współrzędną x (w pikselach) okna aplikacji 
   * na indeks a (kolumnę) pola planszy
   * @param x Współrzędna pozioma położenia w oknie aplikacji
   * @return Indeks a (kolumna) pola planszy
   */
  
  public static int getFieldA(int x) {

     //x = col
     return (int) Math.floor((((float)x - PX_BOARD_MARGIN/2 - ImageRes.IMG_PIECES_WIDTH/2)/PX_FIELD));
  
  }
  
  /**
   * Statyczna metoda przeliczająca współrzędną y (w pikselach) okna aplikacji 
   * na indeks b (wiersz) pola planszy
   * @param y Współrzędna pionowa położenia w oknie aplikacji 
   * @return Indeks b (wiersz) pola planszy
   */  
  public static int getFieldB(int y) {
     
      return (int) Math.floor(((float)y - ImageRes.IMG_PIECES_HEIGHT/2)/PX_FIELD);
     
  }
  
  /**
   * Statyczna metoda zwracająca nazwę (np.A1) wskazanego pola planszy
   * @param a Indeks a (kolumna) pola planszy
   * @param b Indeks b (wiersz) pola planszy
   * @param colsAndRows Ilość kolumn i wierszy planszy
   * @return Nazwa (np.A1) wskazanego pola planszy
   */
  public static String getFieldName(int a, int b, int colsAndRows) {
      
     return Character.toString((char)('A' + a)) + Integer.toString(colsAndRows-b);
      
  }
  
  
}
