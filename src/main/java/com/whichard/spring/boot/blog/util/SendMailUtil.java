package com.whichard.spring.boot.blog.util;

import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendMailUtil {
    private static final Logger log = LoggerFactory.getLogger(SendMailUtil.class);

    public static void sendMail(String to, Map<String, String> map) {

        try {
            Properties props = new Properties();

            // 开启debug调试
            props.setProperty("mail.debug", "true");
            // 发送服务器需要身份验证
            props.setProperty("mail.smtp.auth", "true");
            // 设置邮件服务器主机名
            props.setProperty("mail.host", "smtp.qq.com");
            // 发送邮件协议名称
            props.setProperty("mail.transport.protocol", "smtp");

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);

            Session session = Session.getInstance(props);

            Message msg = new MimeMessage(session);
            msg.setSubject("Whichard.cn邮件通知");
            // send text to the mail
            // StringBuilder builder = new StringBuilder();
            // builder.append("点击测试<a href='http://loc alhost:8080/ssm/listCategory'>listCategory1</a>");
            // msg.setText(builder.toString());
        /*msg.setContent(
                code + "<a href='http://localhost:8080/javamail/ActiveServlet?code="
                        + code + "'>激活</a>", "text/html;charset=UTF-8");*/
            msg.setContent(
                    map.get("username") + "你好，你的账号有异常登录，登录地点为" + map.get("addr") + "，如非本人操作，请及时修改密码"
                    , "text/html;charset=UTF-8");
            msg.setFrom(new InternetAddress("whichard@qq.com"));

            Transport transport = session.getTransport();
            transport.connect("smtp.qq.com", "whichard@qq.com",
                    "gftxtwwlqhgubaic");

            transport.sendMessage(msg, new Address[] { new InternetAddress(to) });
            transport.close();
        } catch (Exception e) {
            log.error("send mail failed : " + e.getMessage());
        }
    }
}
