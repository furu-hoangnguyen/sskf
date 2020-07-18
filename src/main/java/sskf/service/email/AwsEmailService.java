package sskf.service.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import sskf.model.EmailModel;


public class AwsEmailService{

    public static void sendEmail (AmazonSimpleEmailService awsEmailClient, EmailModel mailModel, String mailSource) {
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(mailModel.getEmailAddresses()))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withText(new Content()
                                        .withCharset("UTF-8").withData(mailModel.getTextBody() + mailModel.signature)))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(mailModel.getSubject())))
                .withSource(mailSource);
        awsEmailClient.sendEmail(request);
    }
}
