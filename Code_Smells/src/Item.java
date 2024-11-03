class Item {
    private final ProductInfo productInfo;
    private final DiscountInfo discountInfo;

    public Item(ProductInfo productInfo, DiscountInfo discountInfo) {
        this.productInfo = productInfo;
        this.discountInfo = discountInfo;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public DiscountInfo getDiscountInfo() {
        return discountInfo;
    }

    // Moved Leila's Method here and made some slight variable based changes to keep it here instead of in Order
    public double priceWithDiscount() {
        double price = productInfo.getPrice();
        if (discountInfo.getDiscountType() == DiscountType.PERCENTAGE) {
            price -= discountInfo.getDiscountAmount() * price;
        } else if (discountInfo.getDiscountType() == DiscountType.AMOUNT) {
            price -= discountInfo.getDiscountAmount();
        }
        // Returns price with discount applied
        return price;
    }
}