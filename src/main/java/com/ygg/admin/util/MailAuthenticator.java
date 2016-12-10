package com.ygg.admin.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator
{
    String username;
    
    String password;
    
    public MailAuthenticator()
    {
    }
    
    public MailAuthenticator(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    
    protected PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(username, password);
    }
}
