package com.chase.product;

public class StockProduct {
    private String type;
    private long instockNumbers; 
    private long soldoutNumbers;
    private Double unitPrice;

    public StockProduct() {
    }

    public StockProduct(String type, long inStock, long soldOut, Double unitPrice) {
        this.type = type;
        this.instockNumbers = inStock;
        this.soldoutNumbers = soldOut;
        this.unitPrice = unitPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getInStock() {
        return instockNumbers;
    }

    public void setInStock(long inStock) {
        this.instockNumbers = inStock;
    }

    public long getSoldOut() {
        return soldoutNumbers;
    }

    public void setSoldOut(long soldOut) {
        this.soldoutNumbers = soldOut;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
