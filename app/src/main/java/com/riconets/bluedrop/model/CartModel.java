package com.riconets.bluedrop.model;

public class CartModel {
    String Name,productID,ProductPrice,ProductQuantity,ProductImageUri,CartID;

    public CartModel() {
    }

    public CartModel(String name, String productID,
                     String productPrice, String productQuantity, String productImageUri,String cartID) {
        this.Name = name;
        this.productID = productID;
        this.ProductPrice = productPrice;
        this.ProductQuantity = productQuantity;
        this.ProductImageUri = productImageUri;
        this.CartID=cartID;
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

    public void setCartID(String cartID) {
        CartID = cartID;
    }
}
