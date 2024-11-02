import java.util.ArrayList;
import java.util.List;

public class main {
	public static void main(String[] args) {
        ProductInfo pi1 = new ProductInfo("Book", 20, 1);
        Item item1 = new Item(pi1, DiscountType.AMOUNT, 5);
        ProductInfo pi2 = new ProductInfo("Laptop", 1000, 1);
        Item item2 = new TaxableItem(pi2, DiscountType.PERCENTAGE, 0.1);
        ProductInfo pi3 = new ProductInfo("Gift Card", 10, 1);
        Item item3 = new GiftCardItem(pi3, DiscountType.AMOUNT, 0);

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        Order order = new Order(items, "John Doe", "johndoe@example.com");

        System.out.println("Total Price: " + order.calculateTotalPrice());

        order.sendConfirmationEmail();

        order.printOrder();
    }
}

