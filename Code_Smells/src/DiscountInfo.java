public class DiscountInfo {
    private final DiscountType discountType;
    private final double discountAmount;

    public DiscountInfo(DiscountType discountType, double discountAmount) {
        this.discountType = discountType;
        this.discountAmount = discountAmount;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }
}
