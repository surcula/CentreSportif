package dto;

import java.time.LocalDate;

public class UsersSubscriptionUpdateForm {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer quantity; // nouveau solde
    private boolean active;

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
