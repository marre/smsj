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
 * Contributor(s): Markus Eriksson
 *
 * ***** END LICENSE BLOCK ***** */
package org.marre.sms.nokia;

/*
import java.io.*;
import java.util.*;
import org.marre.sms.util.*;
import org.marre.sms.SmsMessage;
import org.marre.sms.*;
*/

/**
 * Converts various ring tone formats to the nokia ring tone format
 *
 * @author     Boris von Loesch, Markus Eriksson
 * @version    $Id$
 */
public final class RingToneConverter
{
   protected static final int DEFAULT_BEATS_PER_MINUTE = 63;
   protected static final int DEFAULT_OCTAVE = 5;
   //Possible BPM-Values
   protected static final int[] BPM_TABLE = {
         25, 28, 31, 35, 40, 45, 50, 56, 63,
         70, 80, 90, 100, 112, 125, 140, 160, 180,
         200, 225, 250, 285, 320, 355, 400, 450,
         500, 565, 635, 715, 800, 900};

   //Possible RTTL-Note Values
   protected static final String[] RTTL_NOTE_TABLE = {
         "p", "c", "c#", "d", "d#", "e",
         "f", "f#", "g", "g#", "a", "a#", "b"};

   protected static final int LOWEST_RTTL_OCTAVE = 4;
   
   protected int elementCount;

   private int aktOctave = DEFAULT_OCTAVE;
   
   /**
    * 
    */
   private RingToneConverter()
   {
   }

   /**
    *  Converts the rtttl File to a nokia ringtone
    */
/*
   public static final byte[] ConvertRtttl(String rtttl)
   {
      BitArrayOutputStream baos = new BitArrayOutputStream();
      StringTokenizer stk1 = new StringTokenizer(rtttlString, ":");
      int standardDuration = 4;
      aktOctave = DEFAULT_OCTAVE;
      int defOctave = DEFAULT_OCTAVE;
      int tempo = DEFAULT_BEATS_PER_MINUTE;

      int posi = 0;
      //<command-length>=2
      baos.write(0x02);
      //output.append("00000010");

      //<ringing-tone-programming>
      baos.writeBits(0x25, 7);
      //output.append("0100101");

      // octet align command
      baos.flushByte();

      //<sound>
      baos.writeBits(0x1d, 7);
      //output.append("0011101");

      //<basic song type>
      baos.writeBits(0x01, 3);
      //output.append("001");

      //<song title length>
      String title = stk1.nextToken();
      baos.writeBits(title.length(), 4);
      //output.append(getBits(title.length(), 4));

      //<Title Charakters>
      baos.write(title.getBytes("ISO-8859-1"));

      //<song sequence length>=1
      baos.write(0x01);
//      output.append("00000001");

      //<pattern header>
      baos.writeBits(0x00, 3);
//      output.append("000");

      //<pattern id>
      baos.writeBits(0x00, 2);
//      output.append("00");

      //<loop value>=no loop
      baos.writeBits(0x00, 4);
//      output.append("0000");

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
         baos.writeBits(0x04, 3);
//         output.append("100");

         int i = 0;
         while (BPM_TABLE[i++] < tempo)
            ;
         baos.writeBits(i-1, 5);
//         output.append(getBits(i - 1, 5));
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

      return BitStringtoByteArray(output.toString());
   }
*/


   /**
    *  Converts a rtttl notestring to nokia format
    *
    *@param  noteString   The rtttl notestring
    *@param  defOctave    Default octave, which is set by the rtttl file
    *@param  defDuration  Default duration, which is set by the rtttl file
    *@return              The note as bitstring in Nokia format
    */
/*
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
*/
}

