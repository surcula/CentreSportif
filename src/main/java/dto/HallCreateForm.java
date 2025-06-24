package dto;

public class HallCreateForm {

    private String hallName;
    private boolean active = true;
    private Double width;
    private Double length;

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    private Double height;

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public HallCreateForm(String hallName, boolean active, Double width, Double length, Double height) {
        this.hallName = hallName;
        this.active = active;
        this.width = width;
        this.length = length;
        this.height = height;
    }
}
