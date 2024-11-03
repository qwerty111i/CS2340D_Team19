import java.util.List;

public class Order {
    private Customer customer;
    private List<Item> items;

    public Order(Customer customer, List<Item> items) {
        this.customer = customer;
        this.items = items;
    }

    public double calculateTotalPrice() {
    	double total = 0.0;
    	for (Item item : items) {
        	double price = priceWithDiscount(item); //Replaces the switch statement
            
        	total += price * item.getProductInfo().getQuantity();
       	    if (item instanceof TaxableItem) {
                TaxableItem taxableItem = (TaxableItem) item;
                double tax = taxableItem.getTaxRate() / 100.0 * item.getProductInfo().getPrice();
                total += tax;
            }
        }

        return applyDiscounts(total);
    }

    public double priceWithDiscount(Item item) {
        double price = item.getProductInfo().getPrice();
        if (item.getDiscountInfo().getDiscountType() == DiscountType.PERCENTAGE) {
            price -= item.getDiscountInfo().getDiscountAmount() * price;
        } else if (item.getDiscountInfo().getDiscountType() == DiscountType.AMOUNT) {
            price -= item.getDiscountInfo().getDiscountAmount();
        }
        // If there's no discount
        return price;
    }

    private double applyDiscounts(double total) {
        // Subtract $10 for gift card
        if (customer.hasGiftCard()) {
            total -= 10.0;
        }

        // Apply 10% discount for orders over $100
        if (total > 100.0) {
            total *= 0.9;
        }

        return total;
    }

    public void addItemsFromAnotherOrder(Order otherOrder) {
        for (Item item : otherOrder.getItems()) {
            items.add(item);
        }
   }

    public void printOrder() {
        System.out.println("Order Details:");
        for (Item item : items) {
            System.out.println(item.getProductInfo().getName() + " - " + item.getProductInfo().getPrice());
        }
   }

    public void sendConfirmationEmail() {
        EmailSender emailSender = new EmailSender(customer, items, calculateTotalPrice());
        emailSender.sendEmail();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

