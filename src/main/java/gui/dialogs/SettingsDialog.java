/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui.dialogs;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Locale;

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

import gomoku.IConf;
import gomoku.Lang;
import gomoku.Settings;
import gui.IBaseGUI;
import gui.SimpleDialog;

/**
 *
 * Okienko dialogowe z formularzem zmiany ustawień gry
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
     * Konstruktor 
     * @param frame Interfejs GUI
     */        
    public SettingsDialog(IBaseGUI frame) {
     
      super(frame);
      settings = frame.getSettings();
      server = frame.isServer();
      super.showDialog(320, server ? 280 : 320);
      
    }        
   
    
    /**
     * Metoda wyświetlająca zawartość okienka
     */    
    @Override
    protected void getContent()  {
        
       setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
       setTitle(Lang.get("GomokuSettings") 
    		   + (frame.isServer() ? " (" + Lang.get("Server") + ")" : ""));
       
       JPanel p = new JPanel(new GridLayout(2,2));
       
       JLabel label = new JLabel(Lang.get("BoardSize") + ":");
       label.setFont(formsFont);
       p.add(label);
       
       int j = 0;
       int k = (IConf.MAX_COLS_AND_ROWS - IConf.MIN_COLS_AND_ROWS) / 2 + 1;
       String[] options = new String[k];
       Integer[] boardOptionIndex = new Integer[k];
       for (int i = IConf.MIN_COLS_AND_ROWS; i <= IConf.MAX_COLS_AND_ROWS; i+=2) {
    	 options[j] = String.valueOf(i) + " \u00d7 " + String.valueOf(i);   
    	 boardOptionIndex[j++] = i;
       }
       
       final JComboBox<String> boardSize = new JComboBox<>(options);
       boardSize.setFont(formsFont);
       boardSize.setSelectedIndex(Arrays.asList(boardOptionIndex).indexOf(settings.getColsAndRows()));
       boardSize.setBorder(new EmptyBorder(5, 0, 5, 0)); 
       p.add(boardSize);

       label = new JLabel(Lang.get("WinCondition") + ":");
       label.setFont(formsFont);
       p.add(label);
       
       
       k = IConf.MAX_PIECES_IN_ROW - IConf.MIN_PIECES_IN_ROW + 1;
       options = new String[k];
       Integer[] piecesOptionIndex = new Integer[k];
       for (int i = 0; i < k; i++) {
    	 j = i + IConf.MIN_PIECES_IN_ROW;  
    	 options[i] = Lang.get("RowOfStones", j);
    	 piecesOptionIndex[i] = j;
       }
       
       final JComboBox<String> piecesInLine = new JComboBox<>(options);
       piecesInLine.setFont(formsFont);
       piecesInLine.setSelectedIndex(Arrays.asList(piecesOptionIndex).indexOf(settings.getPiecesInRow()));
       piecesInLine.setBorder(new EmptyBorder(5, 0, 5, 0)); 
       p.add(piecesInLine);     
       p.setBorder(new EmptyBorder(5, 15, 5, 15)); 
       add(p);
       
       final JCheckBox compStartsField = new JCheckBox(" " + Lang.get("ComputerStarts"),
    		   settings.isComputerStarts());
              
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
       final String info = frame.isServer() ? Lang.get("ChangeSettingsWarningServer") 
    		   				: Lang.get("ChangeSettingsWarningClient");  

       
       // umieszczenie tekstu
       try {
         doc.insertString(doc.getLength(), Lang.get("Warning") + ": ", style2);
         doc.insertString(doc.getLength(), info, style);
       }
       catch(BadLocationException e) {
         System.err.println(e.getMessage());
       }     
 
       tx.setBorder(new EmptyBorder(20, 12, 15, 12)); 
       add(tx);       
       
       p = new JPanel(new GridLayout(1, 2));
       label = new JLabel(Lang.get("Language") + ":");
       label.setFont(formsFont);
       p.add(label);
       
       options = new String[IConf.LOCALES.length];
       int i = 0;
       for (Locale l: IConf.LOCALES) options[i++] = l.getDisplayLanguage();
       
       final JComboBox<String> language = new JComboBox<>(options);
       language.setSelectedIndex(Lang.getLocaleIndex());
       language.setFont(formsFont);
       language.setBorder(new EmptyBorder(5, 0, 5, 0)); 
       p.add(language);       
       p.setBorder(new EmptyBorder(5, 15, 5, 15)); 
       add(p);
       
       
       JButton buttonChange = new JButton(Lang.get("Apply"));
       buttonChange.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) { 
              
        	 if (Lang.setLocale(language.getSelectedIndex())) frame.translate();
        	  
             // jezeli cos sie zmienilo, zakonczenie rozgrywki i wprowadzenie zmian 
             if (settings.setGameSettings(boardOptionIndex[boardSize.getSelectedIndex()], 
                                  piecesOptionIndex[piecesInLine.getSelectedIndex()], 
                                  compStartsField.isSelected())) {
               
               frame.restartGameSettings();
               
             }
             
             settings.save(server);
             
             dispose();
             
          }
       });
       
       JButton buttonCancel = new JButton(Lang.get("Cancel"));
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



