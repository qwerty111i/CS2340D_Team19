import java.util.List;

public class EmailSender {
    private Customer customer;
    private List<Item> items;
    private double totalPrice;

    public EmailSender(Customer customer, List<Item> items, double totalPrice) {
        this.customer = customer;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public String sendEmail() {
        String message = "Thank you for your order, " + customer.getCustomerName() + "!\n\n" +
                "Your order details:\n";

        for (Item item : items) {
            message += item.getProductInfo().getName() + " - " + item.getProductInfo().getPrice() + "\n";
        }

        message += "Total: " + totalPrice;
        return emailConfig(customer.getCustomerEmail(), "Order Confirmation", message);
    }

    private static String emailConfig(String customerEmail, String subject, String message) {
        String fullEmail;

        fullEmail = "Email to: " + customerEmail + "\n";
        fullEmail += "Subject: " + subject + "\n";
        fullEmail += "Body: " + message;

        return fullEmail;
    }
}
