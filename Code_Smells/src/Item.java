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

    public DiscountType getDiscountType() {
        return discountType;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }
}