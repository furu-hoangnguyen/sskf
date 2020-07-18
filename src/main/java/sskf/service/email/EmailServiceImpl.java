package sskf.service.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import sskf.model.EmailModel;
import sskf.model.EmailSetting;
import sskf.service.EmailServices;

import java.util.List;

@Service
@Slf4j
public class EmailServiceImpl implements EmailServices {
    private AmazonSimpleEmailService awsEmailClient;
    private EmailSetting emailSetting;

    @Value("${app.host.name}")
    private String hostName;

    @Autowired
    public EmailServiceImpl (AmazonSimpleEmailService awsEmailClient, EmailSetting emailSetting) {
        this.awsEmailClient = awsEmailClient;
        this.emailSetting = emailSetting;
    }


    @Override
    public void sendEmail(EmailModel mailModel) {
        log.info("Log begin send email");
        try {
            /*if (emailSetting.getService() == "ses") {
                AwsEmailService.sendEmail(awsEmailClient, mailModel, emailSetting.getSource());
            } else {
                SmtpEmailService.sendEmail(mailModel, emailSetting);
            }*/
            mailModel.setHostName(this.hostName);
            SmtpEmailService.sendEmail(mailModel, emailSetting);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void sendEmails(List<EmailModel> mailModels) {
        for (EmailModel emailModel: mailModels) {
            emailModel.setHostName(this.hostName);
            sendEmail(emailModel);
        }
    }
}
