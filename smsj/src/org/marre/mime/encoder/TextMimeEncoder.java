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
package org.marre.mime.encoder;

import java.io.IOException;
import java.io.OutputStream;

import org.marre.mime.MimeBodyPart;
import org.marre.mime.MimeContentType;
import org.marre.mime.MimeHeader;
import org.marre.mime.MimeHeaderParam;
import org.marre.mime.MimeMultipart;
import org.marre.util.StringUtil;

/**
 * Converts mime documents to text.
 * 
 * TODO: Content-Transfer-Encoding. <br>
 * TODO: Special handling of some headers like Content-Id.
 * 
 * @author Markus Eriksson
 * @version $Id$
 */
public class TextMimeEncoder implements MimeEncoder
{
    /** The length of the random boundary string. */
    private static final int DEFAULT_BOUNDARY_STRING_LENGTH = 35;

    /**
     * Creates a TextMimeEncoder.
     */
    public TextMimeEncoder()
    {
        super();
    }

    /**
     * Writes the content-type of the message to the given stream.
     * 
     * @param theOs
     *            The stream to write to
     * @param theMsg
     *            The message to get the content-type from
     * @throws IOException
     *             Thrown if we fail to write the content-type to the stream
     */
    public void writeContentType(OutputStream theOs, MimeBodyPart theMsg) throws IOException
    {
        MimeContentType ct = theMsg.getContentType();

        if (theMsg instanceof MimeMultipart)
        {
            String boundary = StringUtil.randString(DEFAULT_BOUNDARY_STRING_LENGTH);
            ct.setParam("boundary", boundary);
        }

        writeHeader(theOs, ct);
    }

    /**
     * Writes the headers of the message to the given stream.
     * 
     * @param theOs
     *            The stream to write to
     * @param theMsg
     *            The message to get the headers from
     * @throws IOException
     *             Thrown if we fail to write the headers to the stream
     */
    public void writeHeaders(OutputStream theOs, MimeBodyPart theMsg) throws IOException
    {
        for (int i = 0; i < theMsg.getHeaderCount(); i++)
        {
            MimeHeader header = theMsg.getHeader(i);
            writeHeader(theOs, header);
        }
        theOs.write("\r\n".getBytes());
    }

    /**
     * Writes the body of the message to the given stream.
     * 
     * @param theOs
     *            The stream to write to
     * @param theMsg
     *            The message to get the data from
     * @throws IOException
     *             Thrown if we fail to write the body to the stream
     */
    public void writeBody(OutputStream theOs, MimeBodyPart theMsg) throws IOException
    {
        if (theMsg instanceof MimeMultipart)
        {
            String ct = theMsg.getContentType().getValue();
            if (ct.startsWith("application/vnd.wap.multipart."))
            {
                // WSP encoded multipart
                // TODO: Write wsp encoded multipart
            }
            else
            {
                writeMultipart(theOs, (MimeMultipart) theMsg);
            }
        }
        else
        {
            theOs.write(theMsg.getBody());
            theOs.write("\r\n".getBytes());
        }
    }

    /**
     * Write one header to the stream.
     * 
     * @param theOs
     *            The stream to write to
     * @param header
     *            The header to write.
     * @throws IOException
     *             Thrown if we fail to write the header to the stream
     */
    protected void writeHeader(OutputStream theOs, MimeHeader header) throws IOException
    {
        StringBuffer strBuff = new StringBuffer();

        String name = header.getName();
        String value = header.getValue();

        strBuff.append(name + ": " + value);

        for (int i = 0; i < header.getParamCount(); i++)
        {
            MimeHeaderParam headerParam = header.getParam(i);
            // + "; charset=adsfasdf; param=value"
            strBuff.append("; " + headerParam.getName() + "=" + headerParam.getValue());
        }

        // <CR><LF>
        strBuff.append("\r\n");

        theOs.write(strBuff.toString().getBytes());
    }

    /**
     * Writes a multipart entry to the stream.
     * 
     * @param theOs
     *            The stream to write to
     * @param theMultipart
     *            The header to write.
     * @throws IOException
     *             Thrown if we fail to write an entry to the stream
     */
    private void writeMultipart(OutputStream theOs, MimeMultipart theMultipart) throws IOException
    {
        MimeContentType ct = theMultipart.getContentType();
        MimeHeaderParam boundaryParam = ct.getParam("boundary");
        String boundary = "--" + boundaryParam.getValue();

        for (int i = 0; i < theMultipart.getBodyPartCount(); i++)
        {
            MimeBodyPart part = (MimeBodyPart) theMultipart.getBodyPart(i);

            // Write boundary string
            theOs.write(boundary.getBytes());
            theOs.write("\r\n".getBytes());

            // Generate headers + content-type
            writeContentType(theOs, part);
            writeHeaders(theOs, part);

            // Write data
            writeBody(theOs, part);
        }
        // Write end of boundary
        theOs.write(boundary.getBytes());
        theOs.write("--\r\n".getBytes());
    }
}
