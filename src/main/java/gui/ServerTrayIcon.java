/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import gomoku.IConf;
import gomoku.Lang;
import gui.dialogs.ConfirmDialog;


/**
*
* Menu do zasobnika systemowego (system tray)
* 
* @author Maciej Kawecki
* 
*/
public class ServerTrayIcon extends TrayIcon implements ILocalizable {

  /** Użyta w menu czcionka */
  private static final Font TRAY_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
  /** Szerokość menu */
  private static final int WIDTH = 180;
  /** Czy wyświetlać powiadomienia */
  private static final boolean SHOW_MSG = true;

  /** Czy jest wsparcie dla traya */	
  private boolean supported = false;	
  /** System tray - opcja menu: pokaż/ukryj okno */
  private final JMenuItem trayShowItem;
  /** System tray - opcja menu: restart */
  private final JMenuItem trayRestartItem;
  /** System tray - opcja menu: koniec */
  private final JMenuItem trayQuitItem;  
  /** Nagłówek menu */
  private final JLabel stateLabel;
  
  
  /** Wewn. klasa: element menu */
  @SuppressWarnings("serial")
  private class TrayMenuItem extends JMenuItem {
	  
	TrayMenuItem(String text) {		
	  super(text);
	  setFont(TRAY_FONT);
	  setBorder(new EmptyBorder(5, 1, 5, 1));
	  setMnemonic(IBaseGUI.getKeyCode(this));		
	}
	
	@Override
	public void setText(String text) {
	  super.setText(text);
	  setMnemonic(IBaseGUI.getKeyCode(this));	
	}
	  
  }
  
  
  /** Wewn. klasa: separator */
  @SuppressWarnings("serial")
  private class TrayMenuSeparator extends JSeparator {
	  
	TrayMenuSeparator() {
	  super();
	  setForeground(new Color(0xa0a0a0));
	  setPreferredSize(new Dimension(WIDTH - 30, 1));
	}
	  
  }
  
  /**
   * Konstruktor
   * @param frame Ref. do GUI serwera
   */
  public ServerTrayIcon(ServerGUI frame) {
	  
	super(frame.getIconImage(), frame.getTitle());
	setImageAutoSize(true);
	setToolTip(frame.getTitle());
	
	JPopupMenu trayMenu = new JPopupMenu();	
	trayMenu.setBorder(new LineBorder(new Color(0xa0a0a0), 1));
	
	JPanel header = new JPanel(new GridLayout(2, 1));
	JLabel title = new JLabel(frame.getTitle());
	title.setFont(TRAY_FONT.deriveFont(Font.BOLD));
	title.setBorder(new EmptyBorder(0, 5, 0, 0));
	header.add(title);
	
	stateLabel = new JLabel();
	stateLabel.setText(Lang.get("ServerRunning", String.valueOf(IConf.SERVER_PORT)));
	stateLabel.setFont(TRAY_FONT);
	stateLabel.setBorder(new EmptyBorder(0, 5, 0, 0));
	stateLabel.setForeground(Color.GRAY);
	
	header.setBackground(new Color(0xe5e5e5));
	header.setBorder(new LineBorder(new Color(0xccccff), 1));
	header.add(stateLabel);
	trayMenu.add(header);

	header = new JPanel(new GridLayout(1, 1));
	header.setBorder(new EmptyBorder(0, 0, 4, 0));
	header.setPreferredSize(new Dimension(250, 1));
	trayMenu.add(header);
	
	trayShowItem = new TrayMenuItem(Lang.get("ShowWindow"));
	trayShowItem.addActionListener(new ActionListener() {	    
	  @Override
	  public void actionPerformed(final ActionEvent e) {   
	     frame.setVisible(true);
	     trayShowItem.setEnabled(false);
	   }
	});			
	trayShowItem.setEnabled(!frame.isVisible());
	trayMenu.add(trayShowItem);	
	
	trayMenu.add(new TrayMenuSeparator());
	
	trayRestartItem = new TrayMenuItem(Lang.get("Restart"));
	trayRestartItem.addActionListener(new ActionListener() {	    
	  @Override
	  public void actionPerformed(final ActionEvent e) {   
	     boolean res = new ConfirmDialog(frame, 
	    		 Lang.get("ServerRestartConfirm")).isConfirmed();  
	     if (res) frame.getServerSpy().sendObject("state", "restart");
	   }
	});			
	trayMenu.add(trayRestartItem);
	
	trayMenu.add(new TrayMenuSeparator());
	
	trayQuitItem = new TrayMenuItem(Lang.get("Quit"));
	trayQuitItem.addActionListener(new ActionListener() {	    
	   @Override
	   public void actionPerformed(final ActionEvent e) {   
		 boolean res = new ConfirmDialog(frame, 
		    		   Lang.get("ServerExitConfirm")).isConfirmed();           
		 if (res) System.exit(0);
	   }
	});
	trayMenu.add(trayQuitItem);
		
	// ukrywanie menu
	frame.addMouseListener(new MouseAdapter() {			 
	    @Override
	    public void mouseReleased(MouseEvent e) {
	      trayMenu.setVisible(false);
	 	}			    
		@Override
	    public void mousePressed(MouseEvent e) {
	      trayMenu.setVisible(false);
	    }			 
	});
	  
	// pokazywanie menu
	super.addMouseListener(new MouseAdapter() {

	  @Override
	  public void mouseReleased(MouseEvent e) {
	     showMenu(e);
	  }

	  @Override
	  public void mousePressed(MouseEvent e) {
	     showMenu(e);
	  }

	  private void showMenu(MouseEvent e) {
		 if (e.isPopupTrigger()) {
		   trayMenu.setLocation(e.getX(), e.getY());
	       trayMenu.setInvoker(trayMenu);
	       trayMenu.setVisible(true);
	     }
	  }
	         
	});
	
	trayMenu.setPreferredSize(new Dimension(WIDTH, trayMenu.getPreferredSize().height));

  }
		 
	    
  /**
   * Dodanie ikony do tray'a
   * @throws Exception Nie można dodać
   */
  public void add() throws Exception {

	if (!SystemTray.isSupported()) throw new Exception();
		 
	SystemTray.getSystemTray().add(this);
	
	supported = true;
		
  }
  
  
  public void displayMessage(String message) {
	  
	 if (!supported || !trayShowItem.isEnabled() || !SHOW_MSG) return;
	 displayMessage(getToolTip(), message, MessageType.NONE);
	  
  }


  @Override
  public void translate() {
		
	if (!supported) return;
	  
	stateLabel.setText(Lang.get("ServerRunning", String.valueOf(IConf.SERVER_PORT)));
	trayShowItem.setText(Lang.get("ShowWindow"));
	trayRestartItem.setText(Lang.get("Restart"));	
	trayQuitItem.setText(Lang.get("Quit"));
		
  }	
  
  
  public void enableShowItem(boolean enabled) {
	  
	if (!supported) return;  
	trayShowItem.setEnabled(enabled);  
	  
  }
	
	
}
