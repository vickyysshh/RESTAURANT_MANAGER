package com.example.restaurant.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "table_bookings")
public class TableBooking {

    @Id
    private String id;
    private String userId;
    private String userName;
    private String contactPhone;
    private LocalDateTime bookingDateTime;
    private int guestCount;
    private String specialRequest;
    private String status; // PENDING, APPROVED, REJECTED
    private String managerNote;

    @CreatedDate
    private LocalDateTime createdAt;

    public TableBooking() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public LocalDateTime getBookingDateTime() { return bookingDateTime; }
    public void setBookingDateTime(LocalDateTime bookingDateTime) { this.bookingDateTime = bookingDateTime; }
    public int getGuestCount() { return guestCount; }
    public void setGuestCount(int guestCount) { this.guestCount = guestCount; }
    public String getSpecialRequest() { return specialRequest; }
    public void setSpecialRequest(String specialRequest) { this.specialRequest = specialRequest; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getManagerNote() { return managerNote; }
    public void setManagerNote(String managerNote) { this.managerNote = managerNote; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
