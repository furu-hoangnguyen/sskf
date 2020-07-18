package sskf.service;

import sskf.model.EmailModel;
import java.util.List;

public interface EmailServices {
    void sendEmail (EmailModel mailModel);
    void sendEmails (List<EmailModel> mailModels);
}
