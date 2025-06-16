package model;

import java.util.Date;

public class Shipment {
    private int id;
    private String sender;
    private String receiver;
    private String contents;
    private String status;
    private String location;
    private Date estimatedDelivery;
    private Integer assignedDriverId;

    public Shipment() {}
    public Shipment(int id, String sender, String receiver, String contents, String status, String location, Date estimatedDelivery, Integer assignedDriverId) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.contents = contents;
        this.status = status;
        this.location = location;
        this.estimatedDelivery = estimatedDelivery;
        this.assignedDriverId = assignedDriverId;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }
    public String getContents() { return contents; }
    public void setContents(String contents) { this.contents = contents; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Date getEstimatedDelivery() { return estimatedDelivery; }
    public void setEstimatedDelivery(Date estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }
    public Integer getAssignedDriverId() { return assignedDriverId; }
    public void setAssignedDriverId(Integer assignedDriverId) { this.assignedDriverId = assignedDriverId; }
    @Override
    public String toString() {
        return "Shipment{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", contents='" + contents + '\'' +
                ", status='" + status + '\'' +
                ", location='" + location + '\'' +
                ", estimatedDelivery=" + estimatedDelivery +
                ", assignedDriverId=" + assignedDriverId +
                '}';
    }
} 