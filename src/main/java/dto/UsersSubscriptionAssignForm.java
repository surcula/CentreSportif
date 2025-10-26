package dto;

import java.time.LocalDate;

public class UsersSubscriptionAssignForm {
    private int userId;
    private int subscriptionId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int quantity;
    private boolean active;

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getSubscriptionId() { return subscriptionId; }
    public void setSubscriptionId(int subscriptionId) { this.subscriptionId = subscriptionId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
