/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import gomoku.IConf;
import gui.IBaseGUI;
import gui.ImageRes;
import gui.SimpleDialog;

/**
 *
 * Szablon obiektu wywołującego okienko dialogowe z regułami gry Gomoku
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class RulesDialog extends SimpleDialog {
    
   /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej i wyświetlenie okienka
    * @param frame Referencja do interfejsu GUI
    */     
    public RulesDialog(IBaseGUI frame) {
     
      super(frame);
      super.showDialog(320, 350);
        
    }    
   
   /**
    * Metoda wyświetlająca zawartość okienka
    */    
    @Override
    protected void getContent()  {
        
       setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
       JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
       p.setBorder(new EmptyBorder(10, 10, 10, 10));
       JLabel ico = new JLabel(ImageRes.getIcon("icon.png"));
       p.add(ico);
       JPanel p0 = new JPanel(new GridLayout(2, 1, 0, 10));      
       p0.add(new JLabel("Gomoku v." + IConf.VERSION));
       JLabel lab = new JLabel("Autor: M. Kawecki " + IConf.YEARS);
       lab.setFont(formsFont);
       p0.add(lab);
       p0.setBorder(new EmptyBorder(0, 25, 0, 0));
       p.add(p0);
       add(p);
       
       JTextPane tx = new JTextPane();
       tx.setEditable(false);
       
       // pozycja kursora na poczatku pola tekstowego
       ((DefaultCaret)tx.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

       tx.setBackground(new Color(0xff, 0xff, 0xee));
       tx.setPreferredSize(new Dimension(320, 290));
       
       String txt = null;
       
       // wczytanie zasad gry z pliku tekstowego
       try {
         txt = loadRules();
       } catch (IOException e) {
         System.err.println(e);
       }
    
       StyledDocument doc =  tx.getStyledDocument();  
       
       Style style = StyleContext.getDefaultStyleContext().getStyle(
                     StyleContext.DEFAULT_STYLE);

       StyleConstants.setFontSize(style, 12);
       StyleConstants.setForeground(style, Color.BLACK);
       StyleConstants.setBackground(style, tx.getBackground());
      
       // umieszczenie wczytanego z pliku tekstu
       try {
         doc.insertString(doc.getLength(), txt, style);
       }
       catch(BadLocationException e) {
         System.err.println(e.getMessage());
       }     
    

       add(tx);
      
       // dodanie paska przewijania
       JScrollPane sc = new JScrollPane(tx, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                     JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
       getContentPane().add(sc);
      
       
       JButton b = new JButton(" OK ");
       b.setFocusPainted(false);
       b.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) {   
             dispose();
          }
       });
      
       p = new JPanel(new FlowLayout());
       p.setBorder(new EmptyBorder(5, 0, 5, 0)); 
       b.setAlignmentX(Component.CENTER_ALIGNMENT);
      
       p.add(b);
       add(p);

    }
    
    
    /**
     * Metoda wczytująca zasady gry z pliku tekstowego /resources/rules.txt
     * @return Tekst pobrany z pliku 
     * @throws IOException Błąd wejścia-wyjścia przy próbie odczytu
     */
    private String loadRules() throws IOException {
        
       InputStream input =  getClass().getResourceAsStream("/rules.txt");
       if (input == null) input =  getClass().getResourceAsStream("/resources/rules.txt");
       BufferedReader reader;
       
       try {
         reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
       }
       catch (NullPointerException e) {
         System.err.println("Brak pliku /resources/rules.txt");
         return null;
       }
       
       StringBuilder strBuilder = new StringBuilder();
       String line;
       
       while((line = reader.readLine()) != null) {
           
          strBuilder.append(line);
          strBuilder.append(System.getProperty("line.separator"));
           
       }
  
       reader.close();              
       
       return strBuilder.toString();
        
    }
    

    
}



