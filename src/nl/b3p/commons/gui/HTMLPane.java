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

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.event.*;

public class HTMLPane extends JEditorPane {

    private ActionListener actionListeners = null;

    public HTMLPane() {
        registerEditorKitForContentType("text/html",
                "nl.b3p.commons.gui.HTMLPane$HTMLPaneEditorKit");

        setEditorKit(new HTMLPaneEditorKit());

        setContentType("text/html");
        addHyperlinkListener(new HyperlinkHandler());
        setEditable(false);

    }

    public class HTMLPaneEditorKit extends HTMLEditorKit {

        public ViewFactory getViewFactory() {
            return new CustomViewFactory();
        }
    }

    /*******************************************************************
     *	Create Views for the various HTML elements. This factory differs from
     *	the standard one in that it can create views that handle the
     *	modifications that aree made to EditorKit. For the most part, it
     *	just delegates to its base class.
     */
    private final class CustomViewFactory extends HTMLEditorKit.HTMLFactory {

        public View create(Element element) {

            HTML.Tag kind = (HTML.Tag) (element.getAttributes().getAttribute(
                    javax.swing.text.StyleConstants.NameAttribute));

            if ((kind == HTML.Tag.INPUT) || (kind == HTML.Tag.SELECT) || (kind == HTML.Tag.TEXTAREA)) {
                // Create special views that understand Forms and
                // route submit operations to form listeners only
                // if listeners are registered.
                //
                FormView view = (actionListeners != null)
                        ? new LocalFormView(element)
                        : (FormView) (super.create(element));

                String type = (String) (element.getAttributes().
                        getAttribute(HTML.Attribute.TYPE));
                return view;
            }
            return super.create(element);
        }
    }

    /*******************************************************************
     * Special handling for elements that can occur inside forms.
     */
    public final class LocalFormView extends javax.swing.text.html.FormView {

        public LocalFormView(Element element) {
            super(element);
        }

        /** Chase up through the form hierarchy to find the
         *	 <code>&lt;form&gt;</code> tag that encloses the current
         *	 <code>&lt;input&gt;</code> tag. There's a similar
         *	 method in the base class, but it's private so I can't use it.
         */
        private Element findFormTag() {
            for (Element e = getElement(); e != null; e = e.getParentElement()) {
                if (e.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.FORM) {
                    return e;
                }
            }
            throw new Error("HTMLPane.LocalFormView Can't find <form>");
        }

        /** Override the base-class method that actually submits the form
         *	 data to process it locally instead if the URL in the action
         *	 field matches the "local" URL.
         */
        protected void submitData(String data) {
            AttributeSet attributes = findFormTag().getAttributes();
            String action =
                    (String) attributes.getAttribute(HTML.Attribute.ACTION);
            String method =
                    (String) attributes.getAttribute(HTML.Attribute.METHOD);

            if (action == null) {
                action = "";
            }
            if (method == null) {
                method = "";
            }
            handleSubmit(method.toLowerCase(), action, data);
        }

        protected void imageSubmit(String data) {
            submitData(data);
        }
    }

    /*******************************************************************
     *	Used by {@link HTMLPane} to pass form-submission information to
     *	any ActionListener objects.
     *	When a form is submitted by the user, an actionPerformed() message
     *	that carries a FormActionEvent is sent to all registered
     *	action listeners. They can use the event object to get the
     *	method and action attributes of the form tag as well as the
     *	set of data provided by the form elements.
     */
    public class FormActionEvent extends ActionEvent {

        private final String method;
        private final String action;
        private final Properties data = new Properties();

        /**
         * @param method	method= attribute to Form tag.
         * @param action	action= attribute to Form tag.
         * @param data		Data provided by standard HTML element. Data
         * 					provided by custom tags is appended to this set.
         */
        private FormActionEvent(String method, String action, String data) {
            super(HTMLPane.this, 0, "submit");
            this.method = method;
            this.action = action;
            try {
                //                data = UrlUtil.decodeUrlEncoding(data) + "\n";
                this.data.load(new ByteArrayInputStream(data.getBytes()));
            } catch (IOException e) {
            }
        }

        /** Return the method= attribute of the &lt;form&gt; tag */
        public String method() {
            return method;
        }

        /** Return the action= attribute of the &lt;form&gt; tag */
        public String action() {
            return action;
        }

        /** Return the a set of properties representing the name=value
         *  pairs that would be sent to the server on form submission.
         */
        public Properties data() {
            return data;
        }

        /** Convenience method, works the same as
         *  <code>(HTMLPane)( event.getSource() )</code>
         */
        public HTMLPane source() {
            return (HTMLPane) getSource();
        }
    }
    //@form-action-event-end
    //-----------------------------------------------------------------
    public final void addActionListener(ActionListener listener) {
        actionListeners = AWTEventMulticaster.add(actionListeners, listener);
    }

    public final void removeActionListener(ActionListener listener) {
        actionListeners = AWTEventMulticaster.remove(actionListeners, listener);
    }

    private final void handleSubmit(final String method, final String action, final String data) {
        actionListeners.actionPerformed(
                new FormActionEvent(method, action, data));
    }

    private final class HyperlinkHandler implements HyperlinkListener {

        public void hyperlinkUpdate(HyperlinkEvent event) {
            try {
                if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    HTMLPane source = (HTMLPane) event.getSource();
                    String description = event.getDescription();
                    Element e = event.getSourceElement();

                    // Get the attributes of the <a ...> tag that got
                    // us here, then extract the target= attribute. If
                    // we find target=_blank, then display the page
                    // in a popup window. I'm assuming that the
                    // href references an html file, because it wouldn't
                    // make much sense to use target=_blank if it didn't.

                    AttributeSet tagAttributes = (AttributeSet) (e.getAttributes().getAttribute(HTML.Tag.A));

                    String target = null;
                    if (tagAttributes != null) {
                        target = (String) tagAttributes.getAttribute(
                                HTML.Attribute.TARGET);
                    }
                    if (target != null && target.equals("_blank")) {
                        // popupBrowser( event.getURL() );
                        return;
                    }

                    JEditorPane pane = (JEditorPane) event.getSource();

                    if (event instanceof HTMLFrameHyperlinkEvent) {
                        ((HTMLDocument) (source.getDocument())).processHTMLFrameHyperlinkEvent((HTMLFrameHyperlinkEvent) event);
                    } else {
                        setPage(event.getURL());
                    }
                }
            } catch (Exception e) {
                //                log.warning
                //                        ( "Unexpected exception caught while processing hyperlink: "
                //                        + e.toString() + "\n"
                //                        + Log.stackTraceAsString( e )
                //                        );
            }
        }
    }
}

