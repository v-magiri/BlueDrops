package com.riconets.bluedrop.model;

public class CartModel {
    String Name,productID,ProductPrice,ProductQuantity,ProductImageUri,CartID,TotalPrice;

    public CartModel() {
    }

    public CartModel(String name, String productID,
                     String productPrice, String productQuantity, String productImageUri,
                     String cartID,String totalPrice) {
        this.Name = name;
        this.productID = productID;
        this.ProductPrice = productPrice;
        this.ProductQuantity = productQuantity;
        this.ProductImageUri = productImageUri;
        this.CartID=cartID;
        this.TotalPrice=totalPrice;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        ProductQuantity = productQuantity;
    }

    public String getProductImageUri() {
        return ProductImageUri;
    }

    public void setProductImageUri(String productImageUri) {
        ProductImageUri = productImageUri;
    }

    public String getCartID() {
        return CartID;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public void setCartID(String cartID) {
        CartID = cartID;
    }
}
