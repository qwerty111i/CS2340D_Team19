import java.util.ArrayList;
import java.util.List;

public class main {
	public static void main(String[] args) {
        ProductInfo pi1 = new ProductInfo("Book", 20, 1);
        DiscountInfo di1 = new DiscountInfo(DiscountType.AMOUNT, 5);
        Item item1 = new Item(pi1, di1);
        
        ProductInfo pi2 = new ProductInfo("Laptop", 1000, 1);
        DiscountInfo di2 = new DiscountInfo(DiscountType.PERCENTAGE, 0.1);
        Item item2 = new TaxableItem(pi2, di2);
        
        ProductInfo pi3 = new ProductInfo("Gift Card", 10, 1);
        DiscountInfo di3 = new DiscountInfo(DiscountType.AMOUNT, 0);
        Item item3 = new Item(pi3, di3);

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        Customer customer = new Customer("John Doe", "johndoe@example.com", true);
        Order order = new Order(customer, items);

        System.out.println("Total Price: " + order.calculateTotalPrice());

        order.sendConfirmationEmail();
        order.printOrder();
    }
}

