package org.example;

import java.math.BigDecimal;
import java.util.UUID;

public class Preferences {
    private UUID userID;
    private String roomType;
    private Integer numberOfPeople;
    private BigDecimal rate;

    // Constructor
    public Preferences(UUID userID, String roomType, Integer numberOfPeople, BigDecimal rate) {
        this.userID = userID;
        this.roomType = roomType;
        this.numberOfPeople = numberOfPeople;
        this.rate = rate;
    }

    // Getters and Setters
    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
