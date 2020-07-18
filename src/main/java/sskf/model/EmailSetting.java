package sskf.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailSetting {
    private String source;
    private String fromName;
    private String service;
    private String vpnHost;
    private String vpnPort;
    private String vpnUser;
    private String vpnPassword;
}
