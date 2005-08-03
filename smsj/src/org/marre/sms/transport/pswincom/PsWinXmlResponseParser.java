/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is "SMS Library for the Java platform".
 *
 * The Initial Developer of the Original Code is Markus Eriksson.
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */
package org.marre.sms.transport.pswincom;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marre.sms.SmsException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Parses the response from PsWin.
 * 
 * Uses a DOM parser internally. Currently only looking at the LOGIN result. 
 * 
 * @author Markus
 * @version $Id$
 */
public class PsWinXmlResponseParser
{
    private static Log log_ = LogFactory.getLog(PsWinXmlResponseParser.class);

    protected InputStream xmlInputStream_;
    protected Document respDoc_;
    
    /**
     * Creates the response parser.
     * 
     * @param xmlInputStream
     */
    public PsWinXmlResponseParser(InputStream xmlInputStream)
    {
        xmlInputStream_ = xmlInputStream;
    }
    
    /**
     * Parses the response.
     * 
     * @throws IOException
     * @throws SmsException
     */
    public void parse() throws IOException, SmsException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        try
        {
            Reader isReader = new InputStreamReader(xmlInputStream_);
            Reader filterReader = new PsWinXmlCleanupReader(isReader);
            
            // Build DOM
            DocumentBuilder builder = factory.newDocumentBuilder();
            respDoc_ = builder.parse( new InputSource(filterReader) );            
        }
        catch (ParserConfigurationException ex)
        {
            throw new SmsException("Something wrong with xml", ex);
        }
        catch (SAXException ex)
        {
            throw new SmsException("Failed to parse xml response", ex);
        }
    }
    
    /**
     * Returns the logon result.
     * 
     * @return
     */
    public String getLogon()
    {
        NodeList listOfLogons = respDoc_.getElementsByTagName( "LOGON" );
        Node logon = listOfLogons.item(0);
        return getText(logon);
    }
    
    /**
     * Returns the reason result.
     * 
     * @return
     */
    public String getReason()
    {
        NodeList listOfReasons = respDoc_.getElementsByTagName( "REASON" );
        Node reason = listOfReasons.item(0);
        return getText(reason);
    }

    /**
     * Returns the text within the given node.
     * 
     * @param node
     * @return
     */
    protected String getText(Node node)
    {
        StringBuffer strBuff = new StringBuffer();
        
        if (node.hasChildNodes())
        {
            Node child = node.getFirstChild();
            while (child != null)
            {
                if (child.getNodeType() == Node.TEXT_NODE)
                {
                    Text childText = (Text)child;
                    String text = childText.getData();
                    strBuff.append(text);
                }
                child = child.getNextSibling();
            }
        }
        
        // return null if the string is empty
        return (strBuff.length() > 0)?strBuff.toString():null;
    }   
}
