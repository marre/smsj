/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
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
 * The Initial Developer of the Original Code is Boris von Loesch.
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */
package org.marre.sms.nokia;

import java.io.*;
import java.util.*;
import org.marre.sms.util.*;
import org.marre.sms.SmsMessage;
import org.marre.sms.*;

/**
 *  Returns a SmsConcatMessage with a Nokia ringtone, converted from a
 *  rtttl File or rtttl String
 *
 * @author     Boris von Loesch
 * @created    19. Juni 2002
 * @version    $Id$
 */
public class NokiaRingTone extends SmsConcatMessage
{
   private String rtttlString;

   private final int DEFAULT_BEATS_PER_MINUTE = 63;
   private final int DEFAULT_OCTAVE = 5;
   //Possible BPM-Values
   private final int[] BPM_TABLE = {25, 28, 31, 35, 40, 45, 50, 56, 63,
         70, 80, 90, 100, 112, 125, 140, 160, 180,
         200, 225, 250, 285, 320, 355, 400, 450,
         500, 565, 635, 715, 800, 900};

   //Possible RTTL-Note Values
   private final String[] RTTL_NOTE_TABLE = {"p", "c", "c#", "d", "d#", "e",
         "f", "f#", "g", "g#", "a", "a#", "b"};

   private final int LOWEST_RTTL_OCTAVE = 4;
   private int aktOctave = DEFAULT_OCTAVE;
   private byte[] ringToneData;
   private int elementCount = 0;


   /**
    *  Constructor for the RingTone object
    *
    *@param  rtttlFile        The rtttl File
    *@exception  IOException  If there occur problems with the rtttlFile
    */
   public NokiaRingTone(File rtttlFile) throws IOException
   {
      super(SmsConstants.DCS_DEFAULT_8BIT);
      BufferedReader reader = new BufferedReader(new FileReader(rtttlFile));
      rtttlString = reader.readLine();
      reader.close();
      makeMessage();
      setContent(ringToneData);
   }


   /**
    *  Constructor for the RingTone object
    *
    *@param  rtttlString  A rtttl String
    */
   public NokiaRingTone(String rtttlString)
   {
      super(SmsConstants.DCS_DEFAULT_8BIT);
      this.rtttlString = rtttlString;
      makeMessage();
      setContent(ringToneData);
   }


   /**
    *  Convert a char-Array with '0' and '1' to an int.
    *
    *@param  bits   The char Array, only '1' and '0' are allowed
    *@param  count  How many charecters should convertet
    *@param  posi   From which position
    *@return
    */
   private static int bitstobyte(char[] bits, int count, int posi)
   {
      int a = 0;
      for (int i = posi; (i < posi + count && i < bits.length); i++)
         if (bits[i] == '1')
            a = a + (1 << (7 - (i - posi)));
      return a;
   }


   /**
    *  Converts a bitstring to a byte array.
    *
    *@param  bitsst  The bitstring ('0' and '1')
    *@return         resulted byte array
    */
   public static byte[] BitStringtoByteArray(String bitsst)
   {
      try {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         char[] bits = bitsst.toCharArray();
         int posi = 0;
         for (int i = 0; i < (bits.length) / 8; i++) {
            baos.write(new byte[]{(byte)bitstobyte(bits, 8, posi)});
            posi += 8;
         }
         if (((bits.length) % 8) > 0)
            baos.write(new byte[]{(byte)bitstobyte(bits, (bits.length) % 8, posi)});
         return baos.toByteArray();
      }
      catch (IOException e) {
         e.printStackTrace();
         return null;
      }
   }


   /**
    *  Converts an int to a bitstring
    *
    *@param  i          the int
    *@param  maxdigits  how much digits to return
    *@return            The bitstring
    */
   private String getBits(int i, int maxdigits)
   {
      int l = i + (1 << maxdigits);
      return Integer.toBinaryString(l).substring(1);
   }


   /**
    *  Converts the rtttl File to a nokia ringtone
    */
   private void makeMessage()
   {
      StringBuffer output = new StringBuffer();
      StringTokenizer stk1 = new StringTokenizer(rtttlString, ":");
      int standardDuration = 4;
      aktOctave = DEFAULT_OCTAVE;
      int defOctave = DEFAULT_OCTAVE;
      int tempo = DEFAULT_BEATS_PER_MINUTE;

      int posi = 0;
      //<command-length>=2
      output.append("00000010");

      //<ringing-tone-programming>
      output.append("01001010");

      //<sound>
      output.append("0011101");

      //<basic song type>
      output.append("001");

      //<song title length>
      String title = stk1.nextToken();
      output.append(getBits(title.length(), 4));

      //<Title Charakters>
      for (int i = 0; i < title.length() && i < 16; i++) {
         output.append(getBits(title.charAt(i), 8));
      }

      //<song sequence length>=1
      output.append("00000001");

      //<pattern header>
      output.append("000");

      //<pattern id>
      output.append("00");

      //<loop value>=no loop
      output.append("0000");

      //<pattern specifier>, later

      //Get Bpm, duration and octave
      StringTokenizer stk2 = new StringTokenizer(stk1.nextToken(), ",");
      while (stk2.hasMoreTokens()) {
         String k = stk2.nextToken().trim().toLowerCase();
         if (k.startsWith("d=")) {
            standardDuration = Integer.valueOf(k.substring(2)).intValue();
         }
         else if (k.startsWith("o=")) {
            defOctave = Integer.valueOf(k.substring(2)).intValue();
         }
         else if (k.startsWith("b=")) {
            tempo = Integer.valueOf(k.substring(2)).intValue();
         }
      }

      //if not Default change BPM
      if (standardDuration != DEFAULT_BEATS_PER_MINUTE) {
         //<tempo instr. id>
         output.append("100");

         int i = 0;
         while (BPM_TABLE[i++] < tempo)
            ;
         output.append(getBits(i - 1, 5));
         elementCount++;
      }
      //notes
      stk2 = new StringTokenizer(stk1.nextToken(), ",");
      while (stk2.hasMoreTokens()) {
         String k = stk2.nextToken();
         output.append(getNoteString(k, defOctave, standardDuration));
      }
      //filler bits
      while (output.length() % 8 != 0)
         output.append("0");
      //<command end>
      output.append("00000000");

      //insert <pattern specifier>
      output.insert(47 + title.length() * 8, getBits(elementCount, 8));
      ringToneData = BitStringtoByteArray(output.toString());
   }


   /**
    *  Converts a rtttl notestring to nokia format
    *
    *@param  noteString   The rtttl notestring
    *@param  defOctave    Default octave, which is set by the rtttl file
    *@param  defDuration  Default duration, which is set by the rtttl file
    *@return              The note as bitstring in Nokia format
    */
   private String getNoteString(String noteString, int defOctave, int defDuration)
   {
      //Parse note
      boolean moreDur = false;
      int newoctave = defOctave;
      String notevalue = "";
      String note = noteString;
      note = note.toLowerCase();
      note = note.replace('h', 'b');

      int posi = note.length() - 1;
      StringBuffer retValue = new StringBuffer();
      int duration = defDuration;
      //dotted? (duration specifier)
      if (note.charAt(posi) == '.') {
         moreDur = true;
         posi--;
      }
      //change ocatve?
      if ((note.charAt(posi) >= '0') && (note.charAt(posi) <= '9')) {
         newoctave = Integer.valueOf("" + note.charAt(posi)).intValue();
         posi--;
      }
      //Some rtttl files are not conform, so another dotted check.
      if (note.charAt(posi) == '.') {
         moreDur = true;
         posi--;
      }
      //#?
      if (note.charAt(posi) == '#') {
         notevalue = "#";
         posi--;
      }
      //notevalue
      notevalue = "" + note.charAt(posi) + notevalue;
      posi--;
      //what duration
      if (posi == 1)
         duration = Integer.valueOf(note.substring(0, 2)).intValue();
      else if (posi == 0)
         duration = Integer.valueOf(note.substring(0, 1)).intValue();

      if (newoctave != aktOctave) {
         //<scale instr. id>
         retValue.append("010");
         retValue.append(getBits(newoctave - LOWEST_RTTL_OCTAVE, 2));
         aktOctave = newoctave;
         elementCount++;
      }

      //<note instr. id>
      retValue.append("001");
      int i = 0;
      //TODO:Check length
      //<note value>
      while (!RTTL_NOTE_TABLE[i++].equals(notevalue))
         ;
      retValue.append(getBits(i - 1, 4));
      i = 0;
      //<note duration>
      while ((1 << i++) != duration)
         ;
      retValue.append(getBits(i - 1, 3));
      //<note duration specifier>
      if (moreDur)
         retValue.append("01");
      else
         retValue.append("00");
      elementCount++;
      return (retValue.toString());
   }


   /**
    *  Returns the ringtone userdata
    *
    *@return    ringtone userdata
    */
   public byte[] getBytes()
   {
      byte[] retValue = new byte[ringToneData.length + 7];
      retValue[0] = 0;
      return ringToneData;
   }


   /**
    *
    *
    *@param  ringtone  The nokia ringtone as byte array
    */
   private void setContent(byte[] ringtone)
   {
      SmsUdhElement[] udhElements = new SmsUdhElement[1];

      // Port
      udhElements[0] = SmsUdhUtil.get16BitApplicationPortUdh(SmsConstants.PORT_NOKIA_RING_TONE, 0);

      // Let SmsConcatMessage build the pdus...
      setContent(udhElements, ringtone, ringtone.length);
   }

}
