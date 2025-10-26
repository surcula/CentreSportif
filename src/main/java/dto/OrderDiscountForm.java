// dto/OrderDiscountForm.java
package dto;
public class OrderDiscountForm {
    private int orderId;
    private int discountId;
    public int getOrderId(){ return orderId; }
    public void setOrderId(int orderId){ this.orderId = orderId; }
    public int getDiscountId(){ return discountId; }
    public void setDiscountId(int discountId){ this.discountId = discountId; }
}

