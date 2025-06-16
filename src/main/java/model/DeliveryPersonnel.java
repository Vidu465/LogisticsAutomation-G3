package model;

public class DeliveryPersonnel {
    private int id;
    private String name;
    private String contact;
    private String schedule;
    private String assignedRoutes;

    public DeliveryPersonnel() {}
    public DeliveryPersonnel(int id, String name, String contact, String schedule, String assignedRoutes) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.schedule = schedule;
        this.assignedRoutes = assignedRoutes;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    public String getAssignedRoutes() { return assignedRoutes; }
    public void setAssignedRoutes(String assignedRoutes) { this.assignedRoutes = assignedRoutes; }
    @Override
    public String toString() {
        return "DeliveryPersonnel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", schedule='" + schedule + '\'' +
                ", assignedRoutes='" + assignedRoutes + '\'' +
                '}';
    }
} 