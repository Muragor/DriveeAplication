package com.example.driveeaplication.models;

public class User {
    private String name;
    private int points;
    private String status; // noob pro master
    private boolean isDriver = false;
    public boolean getDriver(){
        return this.isDriver;
    }
    public void setDriver(boolean isDriver){
        this.isDriver = isDriver;
    }
    public User(String name, int points) {
        this.name = name;
        this.points = points;
        this.status = calculateStatus(points);
    }
    private String calculateStatus(int points) {
        if (points < 500) return "Новичок";
        if (points < 1500) return "Опытный";
        return "Мастер Дорог";
    }
    public String getName() { return name; }
    public int getPoints() { return points; }
    public void addPoints(int amount) {
        this.points += amount;
        this.status = calculateStatus(this.points);
    }
    public boolean spendPoints(int amount) {
        if (this.points >= amount) {
            this.points -= amount;
            this.status = calculateStatus(this.points);
            return true;
        }
        return false;
    }
    public String getStatus() { return status; }
    public int calculateTripReward(int price,  boolean isHighDemand, boolean isDriver) {
        double coefficient = 0.05;
        if (isDriver) {
            // Геймификация: повышаем процент от статуса
            if (this.status.equals("Опытный")) {
                coefficient = 0.07; // 7%
            } else if (this.status.equals("Мастер Дорог")) {
                coefficient = 0.10; // 10%
            }
            if (isHighDemand) coefficient *= 2;
        }
        return (int) (price * coefficient);
    }

}
