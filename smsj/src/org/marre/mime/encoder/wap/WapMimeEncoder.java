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
package org.marre.mime.encoder.wap;

import org.marre.mime.encoder.MimeEncoder;

import java.io.*;

import org.marre.mime.*;

import org.marre.wap.util.*;

public class WapMimeEncoder implements MimeEncoder
{
    /**
     * Writes an WSP encoded content type header to the given stream.
     * <p>
     * NOTE! It only writes an WSP encoded content-type to the stream.
     * It does not add the content type header id.
     *
     * @param theOs
     * @param theMsg
     * @throws IOException
     */
    public void writeContentType(OutputStream theOs, MimeBodyPart theMsg)
        throws IOException
    {
        WspUtil.writeContentType(theOs, theMsg.getContentType());
    }

    public void writeHeaders(OutputStream theOs, MimeBodyPart theMsg)
        throws IOException
    {
        for (int i=0; i < theMsg.getHeaderCount(); i++)
        {
            MimeHeader header = theMsg.getHeader(i);
            WspUtil.writeHeader(theOs, header);
        }
    }

    public void writeData(OutputStream theOs, MimeBodyPart theMsg)
        throws IOException
    {
        if (theMsg instanceof MimeMultipart)
        {
            writeMultipart(theOs, (MimeMultipart) theMsg);
        }
        else
        {
            theOs.write(theMsg.getContent());
        }
    }

    // Section 8.5.2 in WAP-230-WSP-20010705
    private void writeMultipart(OutputStream theOs, MimeMultipart theMultipart)
        throws IOException
    {
        // nEntries
        WspUtil.writeUintvar(theOs, theMultipart.getBodyPartCount());

        for(int i=0; i < theMultipart.getBodyPartCount(); i++)
        {
            MimeBodyPart part = (MimeBodyPart) theMultipart.getBodyPart(i);
            ByteArrayOutputStream headers = new ByteArrayOutputStream();

            // Generate content-type + headers
            writeContentType(headers, part);
            writeHeaders(headers, part);
            // Done with the headers...
            headers.close();

            // Length of the content type and headers combined
            WspUtil.writeUintvar(theOs, headers.size());
            // Length of the data (content)
            WspUtil.writeUintvar(theOs, part.getContentSize());
            // Content type + headers
            theOs.write(headers.toByteArray());
            // Data
            writeData(theOs, part);
        }
    }
}
