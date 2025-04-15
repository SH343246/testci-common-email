package Haque.Shafai;
import static org.junit.Assert.*;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException; 
import org.apache.commons.mail.SimpleEmail;
import org.junit.Before;
import org.junit.Test;
import javax.mail.Session;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EmailTest1 {
    private Email email;

    @Before
    public void setUp() { // Function creates a new email in all tests
        email = new SimpleEmail();
    }

    @Test
    public void testAddBcc_SingleEmail() throws Exception { //This test only adds  one bcc email and also makes sure the list size is consistant
        email.addBcc("test@example.com");
        Field bccField = Email.class.getDeclaredField("bccList");
        bccField.setAccessible(true);
        List<?> bccList = (List<?>) bccField.get(email);
        assertEquals(1, bccList.size());
    }

    @Test(expected = EmailException.class) //This test purposely puts in a invalid bcc email to make sure that exception is throwed
    public void testAddBcc_InvalidEmail() throws Exception {
        email.addBcc("invalid-email");
    }

    @Test
    public void testAddCc_MultipleEmails() throws Exception { //Adding two cc emails and checks how much are there
        email.addCc("cc1@example.com");
        email.addCc("cc2@example.com");
        Field ccField = Email.class.getDeclaredField("ccList");
        ccField.setAccessible(true);
        List<?> ccList = (List<?>) ccField.get(email);
        assertEquals(2, ccList.size());
    }

  //  @Test(expected = IllegalArgumentException.class)
    //public void testAddCc_NullEmail() throws Exception {
      //  email.addCc((String) null); // 
    //}
    
    @Test(expected = NullPointerException.class) // I pass a null to a CC email which will throw an exception
    public void testAddCc_NullEmail() throws Exception {
        email.addCc((String) null);
    }

    @Test
    public void testAddHeader_MultipleHeaders() throws Exception { //Test adds two headers and makes sure im adding 2
        email.addHeader("Header1", "Value1");
        email.addHeader("Header2", "Value2");
        Field headersField = Email.class.getDeclaredField("headers");
        headersField.setAccessible(true);
        Map<String, String> headers = (Map<String, String>) headersField.get(email);
        assertEquals(2, headers.size());
    }

    @Test(expected = IllegalArgumentException.class) //I put a empty header, exception will be thrown
    public void testAddHeader_EmptyName() {
        email.addHeader("", "Value");
    }

    @Test
    public void testAddReplyTo_MultipleAddresses() throws Exception {// I add two reply to addresses
        email.addReplyTo("reply@example.com", "name1");
        email.addReplyTo("reply1@example.com", "name2");
        Field replyField = Email.class.getDeclaredField("replyList");
        replyField.setAccessible(true);
        List<?> replyList = (List<?>) replyField.get(email);
        assertEquals(2, replyList.size());
    }

    @Test(expected = EmailException.class) //Sees if an invalid reply-to email will have an exception
    public void testAddReplyTo_InvalidEmail() throws EmailException { 
        email.addReplyTo("invalid-email", "Name");
    }

    //@Test
    //public void testBuildMimeMessage() {
        //try {
            //email.setHostName("smtp.example.com");
            //email.setFrom("sender@example.com");
            //email.addTo("receiver@example.com");
  //  }

    @Test
    public void testBuildMimeMessage() { // A Mime message is created and its success is made sure
        try {
            email.setHostName("smtp.example.com");
            email.setFrom("sender@example.com");
            email.addTo("receiver@example.com");
            email.setSubject("Test Email");
            email.setMsg("A test email.");
            email.buildMimeMessage();
            assertNotNull(email.getMimeMessage());
        } catch (Exception e) {
            e.printStackTrace(); 
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetHostName() { //Tests getting the hostname
        email.setHostName("smtp.example.com");
        assertEquals("smtp.example.com", email.getHostName());
    }

    @Test
    public void testGetHostName_Empty() { //is hostname is empty, null is returned
        email.setHostName("");
        assertNull(email.getHostName());
    }

    @Test
    public void testGetMailSession() throws Exception { //Creates a mail session
        email.setHostName("smtp.example.com");
        Session session = email.getMailSession();
        assertNotNull(session);
    }

    @Test
    public void testGetMailSession_WithSSL() throws Exception { // Tests a SSL mail session
        email.setSSLOnConnect(true);
        email.setHostName("smtp.gmail.com");
        Session session = email.getMailSession();
        assertNotNull(session);
    }

    @Test
    public void testGetSentDate() { // Tests getting/sending a date
        Date now = new Date();
        email.setSentDate(now);
        assertEquals(now, email.getSentDate());
    }

    @Test
    public void testGetSentDate_Null() { //if a date is null, then the current date is used
        email.setSentDate(null);
        assertNotNull(email.getSentDate());
    }

    @Test
    public void testGetSocketConnectionTimeout() { //Sees if the value is valid
        email.setSocketConnectionTimeout(5000);
        assertEquals(5000, email.getSocketConnectionTimeout());
    }

    @Test
    public void testGetSocketConnectionTimeout_Zero() { //tests a value that is null
        email.setSocketConnectionTimeout(0);
        assertEquals(0, email.getSocketConnectionTimeout());
    }

    @Test
    public void testGetSocketConnectionTimeout_Negative() {// tests a value that is negative
        email.setSocketConnectionTimeout(-1000);
        assertEquals(-1000, email.getSocketConnectionTimeout());
    }

    @Test
    public void testSetFrom() throws Exception { //Sees if a from@.. email is valid
        email.setFrom("from@example.com");
        assertEquals("from@example.com", email.getFromAddress().getAddress());
    }

    @Test(expected = EmailException.class) //an invalid from email will throw exception
    public void testSetFrom_InvalidEmail() throws EmailException { 
        email.setFrom("invalid-email");
    }
}