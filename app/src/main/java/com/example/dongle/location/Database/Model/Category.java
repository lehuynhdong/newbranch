package com.example.dongle.location.Database.Model;

/**
 * Created by DongLe on 01-Dec-17.
 */

public class Category {
    public String getCategoryID() {
        return categoryID;
    }

    public Category(String categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    private String categoryID;
    private String categoryName;
}
