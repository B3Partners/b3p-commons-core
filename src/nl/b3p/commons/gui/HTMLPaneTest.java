package nl.b3p.commons.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import nl.b3p.commons.gui.HTMLPane.FormActionEvent;

/*******************************************************************
 *	A test class. Creates pages using test/main.html and
 *	test/submit.html. This test is, unfortunately, highly interactive.
 */

class HTMLPaneTest {
    static
    {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName() );
        } catch( Exception e ) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    public static void main( String[] args ) {
        try {	// Use the Windows look and feel.
            
            URL mainPage;
            
            mainPage =new URL( args.length >= 1
                    ? args[0]
                    : "http://b3pnet05.b3partners.nl/visweb/" );
            
            final HTMLPane pane = new HTMLPane();
            
            pane.addActionListener
                    (	new ActionListener() {
                public void actionPerformed( ActionEvent event ) {
                    FormActionEvent act = (FormActionEvent)event;
                    act.data().list(System.out);
                    String value = act.data().getProperty( "value" );
                    if( value != null && value.equals("Cancel") ) {
                        System.out.println("CANCEL");
                    } else {
                        System.out.println("\n"+ act.getActionCommand() );
                        System.out.println("\t"+"method=" + act.method() );
                        System.out.println("\t"+"action=" + act.action() );
                        act.data().list( System.out );
                        System.out.println("");
                        try {
                            act.source().setPage
                                    ( new URL(
                                    "http://b3pnet05.b3partners.nl/nazcaweb/")
                                    );
                        } catch( Exception e ) {
                            e.printStackTrace();
                        }
                        System.out.println("");
                    }
                }
            }
            );
            
            
            JFrame 	frame = new JFrame();
            frame.addWindowListener
                    (	new WindowAdapter() {
                public void windowClosing( WindowEvent e ) {
                    System.exit(255);
                }
            }
            );
            
            frame.getContentPane().add( new JScrollPane(pane) );
            
            try {
                pane.setPage( mainPage );
            } catch(Exception e) {
                System.err.println( "Can't open " + mainPage + "\n" );
                e.printStackTrace();
            }
            
            frame.setSize( pane.getPreferredSize() );
            frame.pack();
            frame.show();
            
            
        } catch( Throwable e ) {
            e.printStackTrace();
        }
    }
}

