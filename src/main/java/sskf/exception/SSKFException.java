package sskf.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SSKFException extends RuntimeException {
    private static final long serialVersionUID = 5926563242473060260L;

    protected String messageCode;
    private String message;

    public SSKFException(String messageCode, String messageContent) {
        this.messageCode = messageCode;
        this.message = messageContent;
    }

}
