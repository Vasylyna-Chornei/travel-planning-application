package com.vasylyna.travelplanningapplication.database.entity;

public class ChecklistItem {

    public int id;
    public String itemName;
    public boolean isChecked;

    public ChecklistItem(int id, String itemName, boolean isChecked) {
        this.id = id;
        this.itemName = itemName;
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isChecked() {
        return isChecked;
    }

}
