package com.ygg.admin.util;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class MailUtil
{
    private static Logger logger = Logger.getLogger(MailUtil.class);
    
    //发送者，显示的发件人名字,默认设置为左岸城堡
    private static String from;
    
    //发件人邮箱
    private static String user;
    
    //发件人邮箱密码
    private static String password;
    
    private static Session session;
    
    private static Properties prop;
    
    // 加载配置文件，只加载一次
    static
    {
        prop = new Properties();
        try
        {
            prop.load(MailUtil.class.getClassLoader().getResourceAsStream("mail.properties"));
            
            from = prop.getProperty("from", "左岸城堡");
            
            user = prop.getProperty("user");
            
            password = prop.getProperty("password");
            
            session = Session.getInstance(prop, new MailAuthenticator(user, password));
        }
        catch (IOException e)
        {
            logger.error(e.getMessage(), e);
        }
    }
    
    private static Transport getTransport()
        throws Exception
    {
        Transport transport = session.getTransport("smtp");
        transport.connect(user, password);
        return transport;
        
    }
    
    /**
     * 发送纯文本格式邮件
     * @param email：收件人邮箱
     * @param subject：邮件主题
     * @param body：邮件内容
     */
    public static void sendMail(String email, String subject, String body)
    {
        Transport transport = null;
        try
        {
            transport = getTransport();
            MimeMessage msg = new MimeMessage(session);
            //发送日期
            msg.setSentDate(new Date());
            //发件人
            msg.setFrom(new InternetAddress(user, from, "UTF-8"));
            //收件地址
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //主题
            msg.setSubject(subject, "UTF-8");
            //内容
            msg.setText(body, "UTF-8");
            msg.saveChanges();
            transport.sendMessage(msg, msg.getAllRecipients());
            logger.info("*************向" + email + "发送邮件成功*************");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            logger.info("*************向" + email + "发送邮件失败*************");
        }
        finally
        {
            if (transport != null)
            {
                try
                {
                    transport.close();
                }
                catch (MessagingException e)
                {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    /**
     * 发送HTML格式文件
     * @param email
     * @param subject
     * @param body
     */
    public static void sendHTMLMail(String email, String subject, String body)
    {
        Transport transport = null;
        try
        {
            transport = getTransport();
            MimeMessage msg = new MimeMessage(session);
            //发送日期
            msg.setSentDate(new Date());
            //发件人
            msg.setFrom(new InternetAddress(user, from, "UTF-8"));
            //收件地址
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //主题
            msg.setSubject(subject, "UTF-8");
            //内容
            msg.setContent(body, "text/html;charset=UTF-8");
            msg.saveChanges();
            transport.sendMessage(msg, msg.getAllRecipients());
            logger.info("*************向" + email + "发送邮件成功*************");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            logger.info("*************向" + email + "发送邮件失败*************");
        }
        finally
        {
            if (transport != null)
            {
                try
                {
                    transport.close();
                }
                catch (MessagingException e)
                {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    /**
     * 群发纯文本邮件
     * @param emails：群发邮件地址
     * @param subject：邮件主题
     * @param body：邮件内容
     */
    public static void sendMail(String[] emails, String subject, String body)
    {
        Transport transport = null;
        try
        {
            transport = getTransport();
            MimeMessage msg = new MimeMessage(session);
            // 发送日期
            msg.setSentDate(new Date());
            // 发件人
            msg.setFrom(new InternetAddress(user, from, "UTF-8"));
            InternetAddress[] toAddress = new InternetAddress[emails.length];
            for (int i = 0; i < emails.length; i++)
            {
                toAddress[i] = new InternetAddress(emails[i]);
            }
            //收件地址
            msg.setRecipients(Message.RecipientType.TO, toAddress);
            //主题
            msg.setSubject(subject, "UTF-8");
            //内容
            msg.setText(body, "UTF-8");
            msg.saveChanges();
            transport.sendMessage(msg, msg.getAllRecipients());
            logger.info("*************邮件群发成功*************");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            logger.info("*************邮件群发失败*************");
        }
        finally
        {
            if (transport != null)
            {
                try
                {
                    transport.close();
                }
                catch (MessagingException e)
                {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    /**
     * 群发HTML格式邮件
     * @param emails
     * @param subject
     * @param body
     */
    public static void sendHTMLMail(String[] emails, String subject, String body)
    {
        Transport transport = null;
        try
        {
            transport = getTransport();
            MimeMessage msg = new MimeMessage(session);
            // 发送日期
            msg.setSentDate(new Date());
            // 发件人
            msg.setFrom(new InternetAddress(user, from, "UTF-8"));
            InternetAddress[] toAddress = new InternetAddress[emails.length];
            for (int i = 0; i < emails.length; i++)
            {
                toAddress[i] = new InternetAddress(emails[i]);
            }
            //收件地址
            msg.setRecipients(Message.RecipientType.TO, toAddress);
            //主题
            msg.setSubject(subject, "UTF-8");
            //内容
            msg.setContent(body, "text/html;charset=UTF-8");
            msg.saveChanges();
            transport.sendMessage(msg, msg.getAllRecipients());
            logger.info("*************邮件群发成功*************");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            logger.info("*************邮件群发失败*************");
        }
        finally
        {
            if (transport != null)
            {
                try
                {
                    transport.close();
                }
                catch (MessagingException e)
                {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    //    public static void main(String[] args)
    //    {
    //        try
    //        {
    //            sendMail(new String[] {"xiongl@yangege.com"}, "测试", "测试");
    //        }
    //        catch (Exception e)
    //        {
    //            logger.error(e.getMessage(), e);
    //        }
    //    }
}
