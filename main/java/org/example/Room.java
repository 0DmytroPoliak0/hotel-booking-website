package org.example;

import java.math.BigDecimal;

public class Room {
    private Integer roomID; // Nullable for auto-increment
    private String roomType;
    private BigDecimal rate;
    private Boolean availability;
    private Integer numberOfPeople;
    private Integer hotelID; // Assuming hotelID is an Integer in the corresponding Java class for Hotel

    // Constructor without roomID for creating new Room instances
    public Room(String roomType, BigDecimal rate, Boolean availability, Integer numberOfPeople, Integer hotelID) {
        this.roomType = roomType;
        this.rate = rate;
        this.availability = availability;
        this.numberOfPeople = numberOfPeople;
        this.hotelID = hotelID;
    }

    // Constructor including roomID, for instances retrieved from the database
    public Room(Integer roomID, String roomType, BigDecimal rate, Boolean availability, Integer numberOfPeople, Integer hotelID) {
        this.roomID = roomID;
        this.roomType = roomType;
        this.rate = rate;
        this.availability = availability;
        this.numberOfPeople = numberOfPeople;
        this.hotelID = hotelID;
    }

    // Getters and Setters
    public Integer getRoomID() {
        return roomID;
    }

    public void setRoomID(Integer roomID) {
        this.roomID = roomID;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public Integer getHotelID() {
        return hotelID;
    }

    public void setHotelID(Integer hotelID) {
        this.hotelID = hotelID;
    }
}
