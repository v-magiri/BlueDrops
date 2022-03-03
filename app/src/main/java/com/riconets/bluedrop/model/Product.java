package com.riconets.bluedrop.model;

public class Product {
    String ProductName,ProductId,ProductPrice,ProductSize,ProductDesc,ProductImageUri,VendorID,ProductStatus;

    public Product() {
    }

    public Product(String productName,String productID, String productPrice,String productSize,
                   String productDesc, String productImageUri, String vendorID,String productStatus) {
        this.ProductName = productName;
        this.ProductId=productID;
        this.ProductPrice = productPrice;
        this.ProductSize=productSize;
        this.ProductDesc = productDesc;
        this.ProductImageUri = productImageUri;
        this.VendorID = vendorID;
        this.ProductStatus=productStatus;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductSize() {
        return ProductSize;
    }

    public void setProductSize(String productSize) {
        ProductSize = productSize;
    }

    public String getProductDesc() {
        return ProductDesc;
    }

    public void setProductDesc(String productDesc) {
        ProductDesc = productDesc;
    }

    public String getProductImageUri() {
        return ProductImageUri;
    }

    public void setProductImageUri(String productImageUri) {
        ProductImageUri = productImageUri;
    }

    public String getVendorID() {
        return VendorID;
    }

    public void setVendorID(String vendorID) {
        VendorID = vendorID;
    }

    public String getProductStatus() {
        return ProductStatus;
    }

    public void setProductStatus(String productStatus) {
        ProductStatus = productStatus;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }
}
