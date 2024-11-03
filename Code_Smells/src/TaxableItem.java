
public class TaxableItem extends Item {
    private double taxRate = 7;
    
    public TaxableItem(ProductInfo productInfo, DiscountInfo discountInfo) {
        super(productInfo, discountInfo);
    }

    public double getTaxRate() {
        return taxRate;
    }
    
    public void setTaxRate(double rate) {
        if (rate >= 0) {
            taxRate = rate;
        }
    }
}
