/*
 * B3P Commons Core is a library with commonly used classes for webapps.
 * Included are clieop3, oai, security, struts, taglibs and other
 * general helper classes and extensions.
 *
 * Copyright 2000 - 2008 B3Partners BV
 * 
 * This file is part of B3P Commons Core.
 * 
 * B3P Commons Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Commons Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Commons Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    static {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        try {	// Use the Windows look and feel.

            URL mainPage;

            mainPage = new URL(args.length >= 1
                    ? args[0]
                    : "http://b3pnet05.b3partners.nl/visweb/");

            final HTMLPane pane = new HTMLPane();

            pane.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    FormActionEvent act = (FormActionEvent) event;
                    act.data().list(System.out);
                    String value = act.data().getProperty("value");
                    if (value != null && value.equals("Cancel")) {
                        System.out.println("CANCEL");
                    } else {
                        System.out.println("\n" + act.getActionCommand());
                        System.out.println("\t" + "method=" + act.method());
                        System.out.println("\t" + "action=" + act.action());
                        act.data().list(System.out);
                        System.out.println("");
                        try {
                            act.source().setPage(new URL(
                                    "http://b3pnet05.b3partners.nl/nazcaweb/"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("");
                    }
                }
            });


            JFrame frame = new JFrame();
            frame.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    System.exit(255);
                }
            });

            frame.getContentPane().add(new JScrollPane(pane));

            try {
                pane.setPage(mainPage);
            } catch (Exception e) {
                System.err.println("Can't open " + mainPage + "\n");
                e.printStackTrace();
            }

            frame.setSize(pane.getPreferredSize());
            frame.pack();
            frame.show();


        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

