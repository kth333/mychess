package service.impl;

import com.g1.mychess.email.EmailServiceApplication;
import com.g1.mychess.email.controller.EmailController;
import com.g1.mychess.email.service.EmailService;
import com.g1.mychess.email.service.impl.EmailServiceImpl;
import com.g1.mychess.email.util.EmailContentBuilder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

// classes = EmailServiceApplication.class
// classes = EmailService.class
@SpringBootTest(classes = EmailServiceApplication.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class EmailServiceIntegrationTest {

    private EmailServiceImpl emailService;

    @Mock
    private JavaMailSender javaMailSender;  // Mocking JavaMailSender, or any other dependencies

    private static GreenMail smtpServer;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Value("${frontend.url}")
    private String frontendUrl;

    @BeforeEach
    public void setUp() {
        // Email Server for testing
        ServerSetup serverSetup = new ServerSetup(2525, "localhost", "smtp");
        smtpServer = new GreenMail(serverSetup);
        smtpServer.start();

        // Setup the mail sender to point to GreenMail
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(2525);
        emailService = new EmailServiceImpl(mailSender);
    }

    @AfterEach
    public void tearDown() {
        if (smtpServer != null) {
            smtpServer.stop();
        }
    }

    /*
    * Service Layer is relatively simple without many failure cases.
    * As such only the success cases are implemented
    * */

    @Test
    public void test_SendVerificationEmail_Success() throws MessagingException, IOException {
        String to = "recipient@example.com";
        String username = "username";
        String verificationToken = "verificationToken";
        emailService.sendVerificationEmail(to, username, verificationToken);

        MimeMessage[] receivedMessages = smtpServer.getReceivedMessages();
        // assert email sent to correct recipient
        assertEquals(to, receivedMessages[0].getRecipients(MimeMessage.RecipientType.TO)[0].toString(), "Recipient does not match.");
        // assert email content & body
        String emailContent = (String) receivedMessages[0].getContent();
        assertTrue(emailContent.contains(verificationToken), "Email body does not contain the verification token.");
        String emailSubject = receivedMessages[0].getSubject();
        assertTrue(emailSubject.contains("Verification"), "Email body does not contain \"Verification\".");
        assertTrue(emailSubject.contains("MyChess"), "Email Subject does not contain \"MyChess\".");

    }

    @Test
    void test_sendBlacklistEmail_Success() throws MessagingException, IOException {
        String to = "recipient@example.com";
        String username = "username";
        String reason = "This is a legitimate reason";
        Long banDuration = 50L;
        emailService.sendBlacklistEmail(to, username, reason,banDuration);

    MimeMessage[] receivedMessages = smtpServer.getReceivedMessages();
    // assert email sent to correct recipient
    assertEquals(to, receivedMessages[0].getRecipients(MimeMessage.RecipientType.TO)[0].toString(), "Recipient does not match.");
    // assert email content & body
    String emailContent = (String) receivedMessages[0].getContent();
    assertTrue(emailContent.contains(banDuration.toString()), "Email body does not contain the verification token.");
    String emailSubject = receivedMessages[0].getSubject();
    assertTrue(emailSubject.contains("Blacklist"), "Email body does not contain \"Blacklist\".");
    assertTrue(emailSubject.contains("MyChess"), "Email Subject does not contain \"MyChess\".");

    }

    @Test
    void test_sendWhitelistEmail_Success() throws MessagingException, IOException {

        String to =  "recipient@example.com";
        String username = "username";
        String reason = "This is a legitimate reason";

        emailService.sendWhitelistEmail(to, username, reason);

        MimeMessage[] receivedMessages = smtpServer.getReceivedMessages();
        // assert email sent to correct recipient
        assertEquals(to, receivedMessages[0].getRecipients(MimeMessage.RecipientType.TO)[0].toString(), "Recipient does not match.");
        // assert email content & body
        String emailContent = (String) receivedMessages[0].getContent();
        String emailSubject =  receivedMessages[0].getSubject();
        assertTrue(emailContent.contains(reason), "Email body does not contain the verification token.");
        assertTrue(emailSubject.contains("Whitelist"), "Email body does not contain \"Whitelist\".");
        assertTrue(emailSubject.contains("MyChess"), "Email Subject does not contain \"MyChess\".");

    }
    @Test
    void test_sendPasswordResetEmail() throws MessagingException, IOException {

        String to = "recipient@example.com";
        String username = "username";
        String resetToken = "RESET-TOKEN";

        emailService.sendPasswordResetEmail(to, username, resetToken);

        MimeMessage[] receivedMessages = smtpServer.getReceivedMessages();
        // assert email sent to correct recipient
        assertEquals(to, receivedMessages[0].getRecipients(MimeMessage.RecipientType.TO)[0].toString(), "Recipient does not match.");
        // assert email content & body
        String emailContent = (String) receivedMessages[0].getContent();
        String emailSubject = receivedMessages[0].getSubject();
        assertTrue(emailContent.contains(resetToken), "Email body does not contain the reset token.");
        assertTrue(emailSubject.contains("Password Reset"), "Email body does not contain \"Password Reset\".");
        assertTrue(emailSubject.contains("MyChess"), "Email Subject does not contain \"MyChess\".");

    }

}
