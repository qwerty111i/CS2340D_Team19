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
}