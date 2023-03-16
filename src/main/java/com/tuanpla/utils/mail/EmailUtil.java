/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuanpla.utils.mail;

import com.tuanpla.utils.http.ListNets;
import com.tuanpla.utils.logging.LogUtils;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author tuanpla
 */
public class EmailUtil {

    static Logger logger = LoggerFactory.getLogger(EmailUtil.class);


    /*
    Bạn chỉ có thể sử dụng hoặc fixedDelay hoặc fixedRate trong @Schedule annotation, bạn không thể sử dụng đồng thời cả hai.
        fixedDelay là khoảng thời gian nghỉ sau khi nhiệm vụ trước đã hoàn thành, sau đó nó mới thực thi lại nhiệm vụ lần tiếp theo.
        fixedRate là khoảng thời gian giữa lần bắt đầu thực thi nhiệm vụ trước và lần bắt đầu thực thi nhiệm vụ tiếp theo, nó không phụ thuộc vào nhiệm vụ trước đã kết thúc hay chưa.

     */
    // initialDelay = 3 second.
    // fixedDelay = 2 second.
//    @Scheduled(initialDelay = 3 * 1000, fixedDelay = 5 * 1000)
    public void writeCurrentTime() {
//        Date now = new Date();
//        String nowString = df.format(now);
//        MyConfig.debugOut(DE_BUG,"EmailUtil Now [" + this.hashCode() + "] is: " + nowString);
//        try {
//            ArrayList<MailQueue> allQueue = mailQueueDao.allQueue();
//            if (!allQueue.isEmpty()) {
//                allQueue.forEach(one -> {
//                    MailSetting mailSetting = mailSettingSV.findById(one.getMailStId());
//                    if (mailSetting != null) {
//                        boolean result = sendEmail(mailSetting, one.getFromName(), one.getSubject(), one.getBody(), one.getSendTo());
//                        if (!result) {
//                            mailQueueDao.updateCount(one.getId());
//                        } else {
//                            mailQueueDao.delete(one.getId());
//                        }
//                    }
//                });
//            } else {
////                MyConfig.debugOut(DE_BUG,"Mail Queue empty");
//            }
//        } catch (Exception e) {
//            logger.error(Tool.getLogMessage(e));
//        }
    }

    /*
    IMAP uses port 143, but implicit SSL/TLS encrypted IMAP uses port 993.
    POP uses port 110, but implicit SSL/TLS encrypted POP uses port 995.
    SMTP uses port 25, but implicit SSL/TLS encrypted SMTP uses port 465.
    https://www.journaldev.com/2532/javamail-example-send-mail-in-java-smtp
    
    https://www.javatpoint.com/example-of-sending-email-using-java-mail-api
    
    https://javaee.github.io/javamail/docs/api/com/sun/mail/smtp/package-summary.html
     */
//    public static MailSetting testMail(MailSetting mailSetting, String fromName, String subject, String body) {
//        boolean flag = false;
//        try {
//            if (mailSetting == null) {
//                return mailSetting;
//            }
//            // Use the following if you need SSL
//            if ("SSL/TLS".equalsIgnoreCase(mailSetting.getSecureLayer())) {
//                flag = emailSSL(fromName, mailSetting.getUserName(), mailSetting.getPassword(), mailSetting.getMailHost(), mailSetting.getPort(),
//                        mailSetting.getUserName(), // Test thì gửi Email đến chính email test
//                        subject,
//                        body);
//            } else if ("STARTTLS".equalsIgnoreCase(mailSetting.getSecureLayer())) {
//                flag = emailTLS(fromName, mailSetting.getUserName(), mailSetting.getPassword(), mailSetting.getMailHost(), mailSetting.getPort(),
//                        mailSetting.getUserName(), // Test thì gửi Email đến chính email test
//                        subject,
//                        body);
//            } else if ("Auto".equalsIgnoreCase(mailSetting.getSecureLayer())) {
//                flag = sendNomal(fromName, mailSetting.getUserName(), mailSetting.getPassword(), mailSetting.getMailHost(), mailSetting.getPort(),
//                        mailSetting.getUserName(), // Test thì gửi Email đến chính email test
//                        subject,
//                        body);
//                if (!flag) {
//                    flag = emailSSL(fromName, mailSetting.getUserName(), mailSetting.getPassword(), mailSetting.getMailHost(), mailSetting.getPort(),
//                            mailSetting.getUserName(), // Test thì gửi Email đến chính email test
//                            subject,
//                            body);
//                    if (!flag) {
//                        flag = emailTLS(fromName, mailSetting.getUserName(), mailSetting.getPassword(), mailSetting.getMailHost(), mailSetting.getPort(),
//                                mailSetting.getUserName(), // Test thì gửi Email đến chính email test
//                                subject,
//                                body);
//                    }
//                } else {
//
//                }
//            } else {
//                // None
//                // if (encrypt.equalsIgnoreCase("None") || port.equals("25")) {
//                flag = sendNomal(fromName, mailSetting.getUserName(), mailSetting.getPassword(), mailSetting.getMailHost(), mailSetting.getPort(),
//                        mailSetting.getUserName(), // Test thì gửi Email đến chính email test
//                        subject,
//                        body);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mailSetting.setSuccess(flag);
//        return mailSetting;
//    }
//    public static boolean sendEmail(MailSetting mailSetting, String fromName, String subject, String body, String toEmail) {
//        boolean flag = false;
//        try {
//            // Use the following if you need SSL
//            if (mailSetting == null) {
//                return flag;
//            }
//            // Use the following if you need SSL
//            if ("SSL/TLS".equalsIgnoreCase(mailSetting.getSecureLayer())) {
//                flag = emailSSL(fromName, mailSetting.getUserName(), mailSetting.getPassword(), mailSetting.getMailHost(), mailSetting.getPort(),
//                        toEmail,
//                        subject,
//                        body);
//            } else if ("STARTTLS".equalsIgnoreCase(mailSetting.getSecureLayer())) {
//                flag = emailTLS(fromName, mailSetting.getUserName(), mailSetting.getPassword(), mailSetting.getMailHost(), mailSetting.getPort(),
//                        toEmail,
//                        subject,
//                        body);
//            } else if ("Auto".equalsIgnoreCase(mailSetting.getSecureLayer())) {
//                flag = sendNomal(fromName, mailSetting.getUserName(), mailSetting.getPassword(), mailSetting.getMailHost(), mailSetting.getPort(),
//                        toEmail,
//                        subject,
//                        body);
//                if (!flag) {
//                    flag = emailSSL(fromName, mailSetting.getUserName(), mailSetting.getPassword(), mailSetting.getMailHost(), mailSetting.getPort(),
//                            toEmail,
//                            subject,
//                            body);
//                    if (!flag) {
//                        flag = emailTLS(fromName, mailSetting.getUserName(), mailSetting.getPassword(), mailSetting.getMailHost(), mailSetting.getPort(),
//                                toEmail,
//                                subject,
//                                body);
//                    }
//                } else {
//
//                }
//            } else {
//                // None
//                // if (encrypt.equalsIgnoreCase("None") || port.equals("25")) {
//                flag = sendNomal(fromName, mailSetting.getUserName(), mailSetting.getPassword(), mailSetting.getMailHost(), mailSetting.getPort(),
//                        mailSetting.getUserName(), // Test thì gửi Email đến chính email test
//                        subject,
//                        body);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return flag;
//    }
    /**
     * Utility method to send simple HTML email
     *
     * @param fromName
     * @param fromEmail
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     * @return
     */
    private static boolean sendEmail(String fromName, String fromEmail, Session session, String toEmail, String subject, String body) {
        boolean sended = false;
        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress(fromEmail, fromName, "UTF-8"));
            msg.setReplyTo(InternetAddress.parse(fromEmail, false));
            msg.setSubject(subject, "UTF-8");
            // Sets the given String as this part's content,
            Multipart mp = new MimeMultipart("alternative");
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setContent(body, "text/html;charset=utf-8");
            mp.addBodyPart(mbp);
            msg.setContent(mp);
            msg.saveChanges();
            //---
            // with a MIME type of "text/plain".
            //  msg.setText(body, "UTF-8"); // Send Text Message
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            LogUtils.debug("Message is ready");

            Transport.send(msg);
            sended = true;
            LogUtils.debug("EMail Sent Successfully!!");
        } catch (UnsupportedEncodingException | MessagingException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return sended;
    }

    private static boolean sendNoAuthen(String fromEmail, String fromName, String hostMail, String toEmail, String subject, String body) {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", hostMail);
        props.put("mail.debug", "true");
        Session session = Session.getInstance(props, null);
        return sendEmail(fromName, fromEmail, session, toEmail, subject, body);
    }

    public static boolean sendNomal(String fromName, String fromEmail, String password, String hostMail, String mailPort, String toEmail, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", hostMail);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.localhost", ListNets.getHostName()); // HELO host
//        props.put("mail.smtp.port", mailPort); //SMTP Port

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });
        return sendEmail(fromName, fromEmail, session, toEmail, subject, body);
    }

    private static boolean emailTLS(String fromName, String fromEmail, String password, String hostMail, String mailPort, String toEmail, String subject, String body) {
        /**
         * Outgoing Mail (SMTP) Server requires TLS or SSL: smtp.gmail.com (use
         * authentication) Use Authentication: Yes Port for TLS/STARTTLS: 587
         */
        Properties props = new Properties();
        props.put("mail.smtp.host", hostMail); //SMTP Host
//        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.port", mailPort); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        props.put("mail.smtp.localhost", ListNets.getHostName()); // HELO host
        //create Authenticator object to pass in Session.getInstance argument
        props.put("mail.debug", "true");
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        return sendEmail(fromName, fromEmail, session, toEmail, subject, body);

    }

    private static boolean emailSSL(String fromName, String fromEmail, String password, String mailHost, String mailPort, String toEmail, String subject, String body) {
        /**
         * Outgoing Mail (SMTP) Server requires TLS or SSL: smtp.gmail.com (use
         * authentication) Use Authentication: Yes Port for SSL: 465
         */
        LogUtils.debug("Send Email emailSSL Started...");
        Properties props = new Properties();
        props.put("mail.smtp.host", mailHost); //SMTP Host
//        props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
        props.put("mail.smtp.socketFactory.port", mailPort); //SSL Port
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        props.put("mail.smtp.port", mailPort); //SMTP Port
        props.put("mail.smtp.localhost", ListNets.getHostName()); // HELO host
//        props.put("mail.smtp.localhost", ListNets.getExternalIp()); // HELO host
//        props.put("mail.smtp.allow8bitmime","true");    ??
        props.put("mail.debug", "true");
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getDefaultInstance(props, auth);
        return sendEmail(fromName, fromEmail, session, toEmail, subject, body);
//        EmailUtil.sendAttachmentEmail(session, toEmail, "SSLEmail Testing Subject with Attachment", "SSLEmail Testing Body with Attachment");
//        EmailUtil.sendImageEmail(session, toEmail, "SSLEmail Testing Subject with Image", "SSLEmail Testing Body with Image");

    }

    /**
     * Utility method to send image in email body
     *
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     */
    private static void sendImage(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));
            msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            // Create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();
            // Set text message part
            multipart.addBodyPart(messageBodyPart);
            // Second part is image attachment
            messageBodyPart = new MimeBodyPart();
            String filename = "image.png";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            //Trick is to add the content-id header here
            messageBodyPart.setHeader("Content-ID", "image_id");
            multipart.addBodyPart(messageBodyPart);
            //third part for displaying image in the email body
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent("<h1>Attached Image</h1>"
                    + "<img src='cid:image_id'>", "text/html");
            multipart.addBodyPart(messageBodyPart);
            //Set the multipart message to the email message
            msg.setContent(multipart);
            // Send message
            Transport.send(msg);
            LogUtils.debug("EMail Sent Successfully with image!!");
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error(LogUtils.getLogMessage(e));
        }

    }

    /**
     * Utility method to send email with attachment
     *
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     */
    private static void sendAttachment(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));
            msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            // Create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();
            // Fill the message
            messageBodyPart.setText(body);
            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();
            // Set text message part
            multipart.addBodyPart(messageBodyPart);
            // Second part is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = "abc.txt";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            // Send the complete message parts
            msg.setContent(multipart);
            // Send message
            Transport.send(msg);
            LogUtils.debug("EMail Sent Successfully with attachment!!");
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
    }

}
