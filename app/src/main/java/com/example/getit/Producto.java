package com.example.getit;

import java.text.DecimalFormat;

public class Producto {
    private int ProductId;
    private int UserId;
    private String Title;
    private String Description;
    private int Amount;
    private double Price;
    private String ImageCode;
    private double Latitude;
    private double Longitude;
    private String Date;
    private boolean IsActive;

    public Producto(int ProductId, int UserId, String Title, String Description, int Amount, double Price, String ImageCode, double Latitude, double Longitude, String Date, boolean IsActive){
        this.ProductId = ProductId;
        this.UserId = UserId;
        this.Title = Title;
        this.Description = Description;
        this.Amount = Amount;
        this.Price = Price;
        this.ImageCode = ImageCode;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Date = Date;
        this.IsActive = IsActive;
    }

    public int getProductId() {
        return ProductId;
    }

    public int getUserId() {
        return UserId;
    }

    public int getAmount() {
        return Amount;
    }

    public String getDate() {
        return Date;
    }

    public String getDescription() {
        return Description;
    }

    public String getImageCode() {
        return ImageCode;
    }

    public boolean getIsActive() {
        return IsActive;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public double getPrice() {
        return Price;
    }

    public String getTitle() {
        return Title;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setImageCode(String imageCode) {
        ImageCode = imageCode;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setIsActive(boolean isActive) {
        IsActive = isActive;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
