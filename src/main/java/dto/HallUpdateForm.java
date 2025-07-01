package dto;

public class HallUpdateForm {

    private int id;

    private String hallName;

    private boolean active = true;

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    private double height;
    private double length;
    private double width;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    /**
     *
     * @param id
     * @param hallName
     * @param active
     * @param height
     * @param length
     * @param width
     */
    public HallUpdateForm(int id, String hallName, boolean active, double height, double length, double width) {
        this.id = id;
        this.hallName = hallName;
        this.active = active;
        this.height = height;
        this.length = length;
        this.width = width;
    }
}
