package sskf.service.email;

import lombok.extern.slf4j.Slf4j;
import sskf.model.EmailModel;
import sskf.model.EmailSetting;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
public class SmtpEmailService {

    public static void sendEmail (EmailModel mailModel, EmailSetting emailSetting) throws Exception{
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", emailSetting.getVpnPort());
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information.
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(emailSetting.getSource(), emailSetting.getFromName()));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", mailModel.getEmailAddresses())));
        msg.setSubject(mailModel.getSubject());
        msg.setContent(mailModel.getTextBody(),"text/html");

        msg.setHeader("X-SES-CONFIGURATION-SET", "ConfigSet");

        Transport transport = session.getTransport();
        try {
            transport.connect(emailSetting.getVpnHost(), emailSetting.getVpnUser(), emailSetting.getVpnPassword());
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception ex) {
            log.error( ex.getMessage(), ex);
        } finally {
            try {
                transport.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
