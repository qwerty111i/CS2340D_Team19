public class Customer {
    private String customerName;
    private String customerEmail;
    private final boolean hasGiftCard;

    public Customer(String customerName, String customerEmail, boolean hasGiftCard) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.hasGiftCard = hasGiftCard;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public boolean hasGiftCard() {
        return hasGiftCard;
    }
}
