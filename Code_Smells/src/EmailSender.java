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

    public void sendEmail() {
        String message = "Thank you for your order, " + customer.getCustomerName() + "!\n\n" +
                "Your order details:\n";

        for (Item item : items) {
            message += item.getProductInfo().getName() + " - " + item.getProductInfo().getPrice() + "\n";
        }

        message += "Total: " + totalPrice;
        emailConfig(customer.getCustomerEmail(), "Order Confirmation", message);
    }

    private static void emailConfig(String customerEmail, String subject, String message) {
        System.out.println("Email to: " + customerEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + message);
    }
}
