package spring.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender mailSender;
    public void sendResetTokenByEmail(String userEmail, String resetUrl, String resetToken) throws IOException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        mimeMessageHelper.setTo(userEmail);
        mimeMessageHelper.setSubject("Create new password");
        mimeMessageHelper.setText("Link for creating a new password:\n" + resetUrl + "?token=" + resetToken);
        mailSender.send(message);
    }

//    public void sendResetTokenByEmail(String userEmail, String resetUrl) throws IOException, MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
//        mimeMessageHelper.setTo(userEmail);
//        mimeMessageHelper.setSubject("Create new password");
//        mimeMessageHelper.setText("Link for create new password\n" + resetUrl);
//        mailSender.send(message);
//    }

//    public void sendEmail(String to, String subject, String body) throws MessagingException, IOException {
//        StringBuilder csvString = new StringBuilder();
//        for (String line : body.split("\n")) {
//            csvString.append(line);
//            csvString.append("\n");
//        }
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
//        messageHelper.setTo(to);
//        messageHelper.setSubject(subject);
//        messageHelper.setText(csvString.toString());
//        messageHelper.addAttachment("report.csv", new ByteArrayDataSource(csvString.toString().getBytes(), "text/csv"));
//
//        mailSender.send(message);
//    }
}
