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
package org.marre.wap;

/**
 * @author Markus Eriksson
 */
public class MmsConstants
{
    private MmsConstants()
    {
    }

    public static final int HEADER_ID_BCC = 0x01;
    public static final int HEADER_ID_CC = 0x02;
    public static final int HEADER_ID_CONTENT_LOCATION = 0x03;
    public static final int HEADER_ID_CONTENT_TYPE = 0x04;
    public static final int HEADER_ID_DATE = 0x05;
    public static final int HEADER_ID_DELIVERY_REPORT = 0x06;
    public static final int HEADER_ID_DELIVERY_TIME = 0x07;
    public static final int HEADER_ID_EXPIRY = 0x08;
    public static final int HEADER_ID_FROM = 0x09;
    public static final int HEADER_ID_MESSAGE_CLASS = 0x0A;
    public static final int HEADER_ID_MESSAGE_ID = 0x0B;
    public static final int HEADER_ID_MESSAGE_TYPE = 0x0C;
    public static final int HEADER_ID_MMS_VERSION = 0x0D;
    public static final int HEADER_ID_MESSAGE_SIZE = 0x0E;
    public static final int HEADER_ID_PRIORITY = 0x0F;

    public static final int HEADER_ID_READ_REPLY = 0x10;
    public static final int HEADER_ID_REPORT_ALLOWED = 0x11;
    public static final int HEADER_ID_RESPONSE_STATUS = 0x12;
    public static final int HEADER_ID_RESPONSE_TEXT = 0x13;
    public static final int HEADER_ID_SENDER_VISIBILITY = 0x14;
    public static final int HEADER_ID_STATUS = 0x15;
    public static final int HEADER_ID_SUBJECT = 0x16;
    public static final int HEADER_ID_TO = 0x17;
    public static final int HEADER_ID_TRANSACITON_ID = 0x18;

    public static final String HEADER_NAMES[] = {
        null,
        "bcc",
        "cc",
        "content-location",
        "content-type",
        "date",
        "delivery-report",
        "delivery-time",
        "expiry",
        "from",
        "message-class",
        "message-id",
        "message-type",
        "mms-version",
        "message-size",
        "priority",
        "read-reply",
        "report-allowed",
        "response-status",
        "response-text",
        "sender-visibility",
        "status",
        "subject",
        "to",
        "transaction-id",
    };
}
