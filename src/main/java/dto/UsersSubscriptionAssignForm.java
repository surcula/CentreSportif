package dto;

import java.time.LocalDate;

public class UsersSubscriptionAssignForm {
    private Integer userId;
    private Integer subscriptionId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer quantity; // solde initial
    private boolean active;

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getSubscriptionId() { return subscriptionId; }
    public void setSubscriptionId(Integer subscriptionId) { this.subscriptionId = subscriptionId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
