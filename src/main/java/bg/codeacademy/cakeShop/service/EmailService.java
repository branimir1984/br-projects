package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.model.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;


@Service
public class EmailService {
//    private final JavaMailSender javaMailSender;
//
//    public EmailService(JavaMailSender javaMailSender) {
//        this.javaMailSender = javaMailSender;
//    }
//    public String sendSimpleMail(EmailDetails details) {
//
//        try {
//            SimpleMailMessage mailMessage
//                    = new SimpleMailMessage();
//
//
//            mailMessage.setFrom("gospodinov.petar46@gmail.com");
//            mailMessage.setTo(details.getRecipient());
//            mailMessage.setText(details.getMsgBody());
//            mailMessage.setSubject(details.getSubject());
//
//            javaMailSender.send(mailMessage);
//            return "Mail Sent Successfully...";
//        }
//
//        catch (Exception e) {
//            return "Error while Sending Mail";
//        }
//    }

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
