SMSJ
====

Java library implementing the 3GPP TS 23.040 and WAP-230-WSP, and allowing development of SMS or MMS and more

SMSJ is based on the [marre SMSJ](https://github.com/marre/smsj) libraries. It contains several bug fixes, and has been generally refactored, and released at maven central repository.

### Maven

```
<dependency>  
  <groupId>com.github.xfslove</groupId>
  <artifactId>smsj</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Packages

- mms - a several models represents mms file.
- sms - a several models represents sms messages.
- wap - a several models represents wap.

### Usages

- Gsm-7bit charset

  `Gsm7BitCharset.INSTANCE`

- create SMS data coding scheme

  ```
  SmsDcs dataCodingScheme = SmsDcs.general(DcsGroup.GENERAL_DATA_CODING, SmsAlphabet.LATIN1, SmsMsgClass.Cass_1);
  ```

  ```
  SmsDcs dataCodingScheme = SmsDcs.waitingInfo(DcsGroup.MESSAGE_WAITING_STORE_UCS2, SmsWaitingInfo.VOICE);
  ```

- create SMS text message

  ```
  SmsTextMessage message = new SmsTextMessage("github");
  SmsPdu[] pdus = message.getPdus();
  for (SmsPdu smsPdu : pdus) {
    smsPdu.writeTo(os);
  }
  ```

- create WAP SI Push message

  ```
  WapSIPush push = new WapSIPush("www.github.com", "github");
  SmsWapPushMessage message = new SmsWapPushMessage(push);
  SmsPdu[] pdus = message.getPdus();
  for (SmsPdu smsPdu : pdus) {
    smsPdu.writeTo(os);
  }
  ```

- create WAP SL Push message

  ```
  WapSLPush push = new WapSLPush("www.github.com");
  SmsWapPushMessage message = new SmsWapPushMessage(push);
  SmsPdu[] pdus = message.getPdus();
  for (SmsPdu smsPdu : pdus) {
    smsPdu.writeTo(os);
  }
  ```

- create MMS Notification message

  ```
  SmsMmsNotificationMessage message = new SmsMmsNotificationMessage("http://mms-location", 0);
  message.setFrom("from");
  message.setExpiry(absolute);
  message.setTransactionId("transaction-id");
  message.setSubject("subject");
  SmsPdu[] pdus = message.getPdus();
  for (SmsPdu smsPdu : pdus) {
    smsPdu.writeTo(os);
  }
  ```

- create mixed MMS file

  ```
  MixedMms mms = new MixedMms();
  mms.setDate(shownDate);
  mms.setFrom("from");
  mms.setSubject("subject");
  mms.setTransactionId("transaction-id");
  
  mms.addBodyPart(MimeFactory.createBinaryBodyPart(imgBytes, "image/jpg"));
  mms.addBodyPart(MimeFactory.createTextBodyPart("github"));
  mms.writeTo(os);
  ```

- create related MMS file

  ```
  RelatedMms mms = new RelatedMms();
  mms.setDate(shownDate);
  mms.setFrom("from");
  mms.setSubject("subject");
  mms.setTransactionId("transaction-id");
  // px
  mms.setHeight(600);
  mms.setWidth(400);
  
  SmilRegion imgRegion = new SmilRegion(SmilRegion.IMAGE);
  // percentage
  imgRegion.setHeight(60);
  imgRegion.setWidth(100);
  imgRegion.setLeft(0);
  imgRegion.setTop(0);
  mms.addRegion(imgRegion);
  
  SmilRegion txtRegion = new SmilRegion(SmilRegion.TEXT);
  // percentage
  txtRegion.setHeight(40);
  txtRegion.setWidth(100);
  txtRegion.setLeft(0);
  txtRegion.setTop(60);
  mms.addRegion(txtRegion);
  
  SmilPar par1 = new SmilPar();
  // ms
  par1.setDur(3000);
  mms.addPar(par1);
  mms.addBodyPart(MimeFactory.createBinaryBodyPart(imgBytes, "image/jpg"), SmilRegion.IMAGE);
  
  SmilPar par2 = new SmilPar();
  par2.setDur(3000);
  mms.addPar(par2);
  mms.addBodyPart(MimeFactory.createTextBodyPart("github"), SmilRegion.TEXT);
  
  mms.writeTo(os);
  ```
