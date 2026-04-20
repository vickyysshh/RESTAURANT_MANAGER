package com.example.restaurant.model;

public class OrderItem {
    private MenuItemRef menuItem;
    private int quantity;
    private double subtotal;

    public OrderItem() {}

    public MenuItemRef getMenuItem() { return menuItem; }
    public void setMenuItem(MenuItemRef menuItem) { this.menuItem = menuItem; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    // Embedded reference to avoid full document duplication
    public static class MenuItemRef {
        private String id;
        private String itemName;
        private double price;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }
}
