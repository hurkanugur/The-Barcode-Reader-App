package com.example.mobiletermproject;

public class Information {
    private String imageURL;
    private String title;
    private String price;
    private String seller;
    private String sellerURL;

    public Information(String imageURL, String title, String price, String seller, String sellerURL) {
        this.imageURL = imageURL;
        this.title = title;
        this.price = price;
        this.seller = seller;
        this.sellerURL = sellerURL;
    }
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSellerURL() { return sellerURL; }

    public void setSellerURL(String sellerURL) {
        this.sellerURL = sellerURL;
    }

}
