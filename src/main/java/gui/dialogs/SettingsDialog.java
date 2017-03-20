/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import gomoku.Settings;
import gui.IBaseGUI;
import gui.SimpleDialog;

/**
 *
 * Szablon obiektu wywołującego okienko dialogowe z formularzem zmiany ustawień gry
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class SettingsDialog extends SimpleDialog {
    
    /** Ustawienia wybrane przez użytkownika */
    private final Settings settings; 
    /** Czy okno wywołane z okna serwera */
    private final boolean server;
    
  
    /**
     * Konstruktor, wywołanie konstruktora klasy nadrzędnej, wypełnienie wewn. pól 
     * i wyświetlenie okienka
     * @param frame Referencja do interfejsu GUI
     */        
    public SettingsDialog(IBaseGUI frame) {
     
      super(frame);
      settings = frame.getSettings();
      server = frame.isServer();
      super.showDialog(320, server ? 260 : 290);
      
    }        
   
    
    /**
     * Metoda wyświetlająca zawartość okienka
     */    
    @Override
    protected void getContent()  {
        
       setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
       JLabel t =  new JLabel("Ustawienia" + (frame.isServer() ? " (serwer)" : ""));
       t.setAlignmentX(Component.CENTER_ALIGNMENT);
       t.setBorder(new EmptyBorder(10, 0, 20, 0)); 
       
       add(t);
       
       // panel do umieszczenia pól wyboru typu JComboBox
       JPanel p = new JPanel(new GridLayout(2,2));
       
       JLabel label = new JLabel("Wielko\u015b\u0107 planszy:");
       label.setFont(formsFont);
       p.add(label);
       String[] options = {"7\u00d77", "9\u00d79", "11\u00d711", "13\u00d713", "15\u00d715"};
       final Integer[] boardOptionIndex = new Integer[] {7, 9, 11, 13, 15};
       final JComboBox<String> boardSize = new JComboBox<>(options);
       boardSize.setFont(formsFont);
       boardSize.setSelectedIndex(Arrays.asList(boardOptionIndex).indexOf(settings.getColsAndRows()));
       boardSize.setBorder(new EmptyBorder(5, 0, 5, 0)); 
       p.add(boardSize);

       label = new JLabel("Warunek wygranej:");
       label.setFont(formsFont);
       p.add(label);
       options = new String[] {"3 kamienie", "4 kamienie", "5 kamieni", "6 kamieni"};
       final Integer[] piecesOptionIndex = new Integer[] {3, 4, 5, 6};
       final JComboBox<String> piecesInLine = new JComboBox<>(options);
       piecesInLine.setFont(formsFont);
       piecesInLine.setSelectedIndex(Arrays.asList(piecesOptionIndex).indexOf(settings.getPiecesInRow()));
       piecesInLine.setBorder(new EmptyBorder(5, 0, 5, 0)); 
       p.add(piecesInLine);     
       p.setBorder(new EmptyBorder(5, 15, 5, 15)); 
       add(p);
       
       final JCheckBox compStartsField = new JCheckBox(" Komputer rozpoczyna gr\u0119", settings.isComputerStarts());
              
       if (!server) {
    	   
         p = new JPanel(new FlowLayout(FlowLayout.LEFT));

         compStartsField.setFont(formsFont);
         compStartsField.setBorder(new EmptyBorder(0, 10, 0, 0)); 
         compStartsField.setFocusPainted(false);
         p.add(compStartsField);
           
         add(p); 
    	   
    	   
       }
       
       // ostrzezenie  - tekst
       JTextPane tx = new JTextPane();
       tx.setEditable(false);
       tx.setOpaque(false);
       tx.setBorder(new EmptyBorder(10, 0, 0, 0));
    
       StyledDocument doc =  tx.getStyledDocument();  
       
       Style style = StyleContext.getDefaultStyleContext().getStyle(
                     StyleContext.DEFAULT_STYLE);
      
       StyleConstants.setFontSize(style, 12);
       StyleConstants.setForeground(style, Color.DARK_GRAY);
       StyleConstants.setBackground(style, getBackground());
       
       Style style2 = doc.addStyle("red", style);
       StyleConstants.setForeground(style2, Color.RED);
      
       // inny tekst dla serwera i dla klienta
       final String info = (frame.isServer())  ?  " zmiana ustawie\u0144 wymaga zako\u0144czenia" 
                             + " bie\u017c\u0105cej gry. Serwer zostanie zrestartowany."
                             : " zmiana ustawie\u0144 wymaga zako\u0144czenia bie\u017c\u0105cej gry. " 
                             + "W przypadku do\u0142\u0105czenia do gry sieciowej, " 
                             + "obowi\u0105zuj\u0105 ustawienia po stronie serwera gry.";   

       
       // umieszczenie tekstu
       try {
         doc.insertString(doc.getLength(), "Uwaga: ", style2);
         doc.insertString(doc.getLength(), info, style);
       }
       catch(BadLocationException e) {
         System.err.println(e.getMessage());
       }     
 
       tx.setBorder(new EmptyBorder(20, 12, 15, 12)); 
       add(tx);       
       
       
       // przygotowanie przycisków Zastosuj i Anuluj
       JButton buttonChange = new JButton("Zastosuj");
       buttonChange.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) { 
              
             // jezeli cos sie zmienilo, zakonczenie rozgrywki i wprowadzenie zmian 
             if (settings.setSettings(boardOptionIndex[boardSize.getSelectedIndex()], 
                                  piecesOptionIndex[piecesInLine.getSelectedIndex()], 
                                  compStartsField.isSelected())) {
               dispose();
               frame.restartGameSettings();
               
             }
             
             else dispose();
             
          }
       });
       
       JButton buttonCancel = new JButton("Anuluj");
       buttonCancel.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) {   
             dispose();
          }
       });       
      
       p = new JPanel(new FlowLayout());
       p.setBorder(new EmptyBorder(5, 0, 5, 0)); 
      
       p.add(buttonChange);
       p.add(buttonCancel);
       add(p);

    }

    
    
}



