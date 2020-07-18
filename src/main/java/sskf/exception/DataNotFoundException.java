package sskf.exception;

import sskf.model.BaseStatusError;

public class DataNotFoundException extends SSKFException{
    private static final long serialVersionUID = -3676966545046845850L;

    public DataNotFoundException(String message) {
        super("", message);
    }

    public DataNotFoundException(String code, String message) {
        super(code, message);
    }

    public DataNotFoundException(BaseStatusError status) {
        super(status.getCode(), status.getMessage());
    }
}
