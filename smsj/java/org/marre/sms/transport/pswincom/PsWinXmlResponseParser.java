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

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.marre.sms.SmsException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses the response from PsWin.
 * 
 * Uses a SAX parser internally. Currently only looking at the LOGIN result. 
 * 
 * @author Markus
 * @version $Id$
 */
public class PsWinXmlResponseParser extends DefaultHandler
{
    private SAXParser saxParser_;
    private LinkedList stack_;
    
    private StringBuffer reasonBuffer_;
    private StringBuffer logonBuffer_;
    
    /**
     * Creates the response parser.
     */
    public PsWinXmlResponseParser()
    {
        saxParser_ = getParser();
        
        stack_ = new LinkedList();
        reasonBuffer_ = new StringBuffer();
        logonBuffer_ = new StringBuffer();
    }
    
    /**
     * 
     * @return
     */
    protected SAXParser getParser() {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
//            factory.setNamespaceAware(false);
//            factory.setValidating(false);
//            factory.setXIncludeAware(false);
            return factory.newSAXParser();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Parses the response.
     * @param xmlInputStream 
     * 
     * @throws IOException
     * @throws SmsException
     */
    public PsWinXmlResponse parse(InputStream xmlInputStream) throws IOException, SmsException
    {
        try
        {
            Reader isReader = new InputStreamReader(xmlInputStream);
            Reader cleanupReader = new PsWinXmlCleanupReader(isReader);

            // Reset object
            stack_ = new LinkedList();
            reasonBuffer_ = new StringBuffer();
            logonBuffer_ = new StringBuffer();
            
            saxParser_.parse(new InputSource(cleanupReader), this);
        }
        catch (SAXException ex)
        {
            throw new SmsException("Failed to parse xml response", ex);
        }
        
        return new PsWinXmlResponse(logonBuffer_.toString(), reasonBuffer_.toString());        
    }
    
    public void startElement(String uri, String localName, String qName, Attributes attributes)
    {
        stack_.addLast(qName);
    }
    
    public void endElement(String uri, String localName, String qName)
    {
        stack_.removeLast();
    }
        
    public void characters(char[] ch, int start, int length)
    {
        String topOfStack = (String)stack_.getLast();
        
        if ("REASON".equals(topOfStack)) {
            reasonBuffer_.append(ch, start, length);
        } else if ("LOGON".equals(topOfStack)) {
            logonBuffer_.append(ch, start, length);
        }
    }
}
