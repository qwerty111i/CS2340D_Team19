class Item {
    private ProductInfo productInfo;
    private DiscountType discountType;
    private double discountAmount;

    public Item(ProductInfo productInfo, DiscountType discountType, double discountAmount) {
        this.productInfo = productInfo;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
    }

    public String getName() {
        return productInfo.getName();
    }

    public double getPrice() {
        return productInfo.getPrice();
    }

    public int getQuantity() {
        return productInfo.getQuantity();
    }


    //Moved Leila's Method here and made some slight variable based changes to keep it here instead of in Order
    public double priceWithDiscount() {
        double price = getPrice();
        if (discountType == DiscountType.PERCENTAGE) {
            price -= discountAmount * price;
        } else if (discountType == DiscountType.AMOUNT) {
            price -= discountAmount;
        }
        return price; // Returns price with discount applied
    }
}