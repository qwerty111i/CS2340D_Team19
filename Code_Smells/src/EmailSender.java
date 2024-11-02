
public class EmailSender {
    // Make private constructor to hide the implicit public constructor
    private EmailSender() {
        throw new IllegalStateException("Utility class");
    }
    public static void sendEmail(String customerEmail, String subject, String message){
        System.out.println("Email to: " + customerEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + message);
    }
}
