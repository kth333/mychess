import com.g1.mychess.email.EmailServiceApplication;
import com.g1.mychess.email.service.impl.EmailServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = EmailServiceApplication.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class EmailServiceIntegrationTest {

    private EmailServiceImpl emailService;

    private JavaMailSenderImpl javaMailSender;

    private static GreenMail smtpServer;

    /**
     * Sets up the test environment before each test execution.
     *
     * This method initializes and starts a GreenMail SMTP server for testing purposes
     * on localhost at port 2525. It also configures a JavaMailSenderImpl instance to
     * send emails through the GreenMail server. An EmailServiceImpl instance is
     * then configured with the JavaMailSenderImpl.
     *
     * This setup ensures that any email interactions performed by the
     * EmailServiceImpl during tests are done through the fake GreenMail server, thus
     * avoiding any real email transmissions.
     *
     * Email Service Layer is relatively simple without many failure cases.
     * Validity of local and domain part of email covered by auth-service.
     *      And correctness of logic tested by AuthServiceUnitTests
     *
     * Existence of username and email already in db, is handled by player-service.
     * We'll just test the "end-points"
     */

    @BeforeEach
    public void setUp() {
        // Email Server for testing
        ServerSetup serverSetup = new ServerSetup(2525, "localhost", "smtp");
        smtpServer = new GreenMail(serverSetup);
        smtpServer.start();

        // Setup the mail sender to point to GreenMail
        javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("localhost");
        javaMailSender.setPort(2525);
        emailService = new EmailServiceImpl(javaMailSender);
    }

    @AfterEach
    public void tearDown() {
        if (smtpServer != null) {
            smtpServer.stop();
        }
    }

    /**
     * Tests the successful sending of a verification email.
     *
     * This test verifies that the email is sent to the correct recipient, and the content and subject
     * of the email contain the expected verification token and relevant text.
     *
     * @throws MessagingException if there is a failure in the messaging components.
     * @throws IOException if there is an input-output failure during the email sending process.
     */

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

    /**
     * Tests the successful sending of a blacklist email.
     *
     * This test verifies that the email is sent to the correct recipient, and the content and subject
     * of the email contain the appropriate reason for blacklisting and the ban duration.
     *
     * @throws MessagingException if there is a failure in the messaging components.
     * @throws IOException if there is an input-output failure during the email sending process.
     */
    @Test
    void test_sendBlacklistEmail_Success() throws MessagingException, IOException {
        String to = "recipient@example.com";
        String username = "username";
        String reason = "This is a legitimate reason";
        Long banDuration = 50L;
        emailService.sendBlacklistEmail(to, username, reason, banDuration);

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

    /**
     * Tests the successful sending of a whitelist email.
     *
     * This test verifies that the email is sent to the correct recipient, and the content and subject
     * of the email contain the expected whitelist reason and relevant text.
     *
     * @throws MessagingException if there is a failure in the messaging components.
     * @throws IOException if there is an input-output failure during the email sending process.
     */
    @Test
    void test_sendWhitelistEmail_Success() throws MessagingException, IOException {

        String to = "recipient@example.com";
        String username = "username";
        String reason = "This is a legitimate reason";

        emailService.sendWhitelistEmail(to, username, reason);

        MimeMessage[] receivedMessages = smtpServer.getReceivedMessages();
        // assert email sent to correct recipient
        assertEquals(to, receivedMessages[0].getRecipients(MimeMessage.RecipientType.TO)[0].toString(), "Recipient does not match.");
        // assert email content & body
        String emailContent = (String) receivedMessages[0].getContent();
        String emailSubject = receivedMessages[0].getSubject();
        assertTrue(emailContent.contains(reason), "Email body does not contain the verification token.");
        assertTrue(emailSubject.contains("Whitelist"), "Email body does not contain \"Whitelist\".");
        assertTrue(emailSubject.contains("MyChess"), "Email Subject does not contain \"MyChess\".");
    }

    /**
     * Tests the successful sending of a password reset email.
     *
     * This test verifies that the email is sent to the correct recipient and the content
     * and subject of the email contain the expected password reset token and relevant text.
     *
     * @throws MessagingException if there is a failure in the messaging components.
     * @throws IOException if there is an input-output failure during the email sending process.
     */
    @Test
    void test_sendPasswordResetEmail_Success() throws MessagingException, IOException {

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
