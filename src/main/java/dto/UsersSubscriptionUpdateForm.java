package dto;

import java.time.LocalDate;

/**
 * Champs optionnels :
 * - startDate / endDate / quantity peuvent être null => ne pas modifier
 * - active est toujours fourni
 */
public class UsersSubscriptionUpdateForm {
    private int usersSubscriptionId;
    private LocalDate startDate;   // nullable
    private LocalDate endDate;     // nullable
    private Integer quantity;      // nullable
    private boolean active;

    public int getUsersSubscriptionId() { return usersSubscriptionId; }
    public void setUsersSubscriptionId(int usersSubscriptionId) { this.usersSubscriptionId = usersSubscriptionId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
