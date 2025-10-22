package tn.esprit.entities;

import java.util.List;

public class Order {
    private int idOrder;
    private User user;
    private List<Product> products;
    private double total;

    @Override
    public String toString() {
        return "Order{" +
                "idOrder=" + idOrder +
                ", user=" + user +
                ", products=" + products +
                ", total=" + total +
                '}';
    }

    // Constructeur vide (obligatoire pour certaines librairies)
    public Order() {}

    // Constructeur avec tous les champs
    public Order(int idOrder, User user, List<Product> products) {
        this.idOrder = idOrder;
        this.user = user;
        this.products = products;
        this.total = products.stream().mapToDouble(Product::getPrice).sum(); // calcule automatique du total
    }

    // Getters et setters
    public int getIdOrder() { return idOrder; }
    public void setIdOrder(int idOrder) { this.idOrder = idOrder; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
