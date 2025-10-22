package tn.esprit.services;

import tn.esprit.entities.Order;
import tn.esprit.entities.Product;
import tn.esprit.entities.User;
import tn.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements IGenericService<Order> {

    private Connection conn;

    public OrderServiceImpl() {
        try {
            conn = DataBase.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Order order) {
        try {
            double total = order.getProducts().stream().mapToDouble(Product::getPrice).sum();

            // Insérer la commande
            String sqlOrder = "INSERT INTO `order` (user_id, total, order_date) VALUES (?, ?, ?)";
            PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, order.getUser().getId());
            psOrder.setDouble(2, total);
            psOrder.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            psOrder.executeUpdate();

            ResultSet rs = psOrder.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            // Insérer les produits dans order_product
            String sqlDetail = "INSERT INTO order_product(order_id, product_id) VALUES (?, ?)";
            for (Product p : order.getProducts()) {
                PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                psDetail.setInt(1, orderId);
                psDetail.setInt(2, p.getIdProduct());
                psDetail.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Order order) {
        try {
            double total = order.getProducts().stream().mapToDouble(Product::getPrice).sum();

            // Mettre à jour la commande
            String sql = "UPDATE `order` SET user_id=?, total=? WHERE idOrder=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, order.getUser().getId());
            ps.setDouble(2, total);
            ps.setInt(3, order.getIdOrder());
            ps.executeUpdate();

            // Supprimer les anciens produits
            String del = "DELETE FROM order_product WHERE order_id=?";
            PreparedStatement psDel = conn.prepareStatement(del);
            psDel.setInt(1, order.getIdOrder());
            psDel.executeUpdate();

            // Ajouter les nouveaux produits
            String sqlDetail = "INSERT INTO order_product(order_id, product_id) VALUES (?, ?)";
            for (Product p : order.getProducts()) {
                PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                psDetail.setInt(1, order.getIdOrder());
                psDetail.setInt(2, p.getIdProduct());
                psDetail.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try {
            String delDetail = "DELETE FROM order_product WHERE order_id=?";
            PreparedStatement psDelDetail = conn.prepareStatement(delDetail);
            psDelDetail.setInt(1, id);
            psDelDetail.executeUpdate();

            String delOrder = "DELETE FROM `order` WHERE idOrder=?";
            PreparedStatement psDel = conn.prepareStatement(delOrder);
            psDel.setInt(1, id);
            psDel.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        try {
            String sql = "SELECT * FROM `order`";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int orderId = rs.getInt("idOrder");
                int userId = rs.getInt("user_id");
                User user = new User(userId, "", "");

                // récupérer les produits
                String sqlProd = "SELECT p.idProduct, p.name, p.price, p.idCategory FROM product p " +
                        "JOIN order_product op ON p.idProduct = op.product_id WHERE op.order_id=?";
                PreparedStatement psProd = conn.prepareStatement(sqlProd);
                psProd.setInt(1, orderId);
                ResultSet rsProd = psProd.executeQuery();
                List<Product> products = new ArrayList<>();
                while (rsProd.next()) {
                    Product p = new Product(
                            rsProd.getInt("idProduct"),
                            rsProd.getString("name"),
                            rsProd.getDouble("price"),
                            null
                    );
                    products.add(p);
                }

                orders.add(new Order(orderId, user, products));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public Order getById(int id) {
        try {
            String sql = "SELECT * FROM `order` WHERE idOrder=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                User user = new User(userId, "", "");

                String sqlProd = "SELECT p.idProduct, p.name, p.price, p.idCategory FROM product p " +
                        "JOIN order_product op ON p.idProduct = op.product_id WHERE op.order_id=?";
                PreparedStatement psProd = conn.prepareStatement(sqlProd);
                psProd.setInt(1, id);
                ResultSet rsProd = psProd.executeQuery();
                List<Product> products = new ArrayList<>();
                while (rsProd.next()) {
                    Product p = new Product(
                            rsProd.getInt("idProduct"),
                            rsProd.getString("name"),
                            rsProd.getDouble("price"),
                            null
                    );
                    products.add(p);
                }

                return new Order(id, user, products);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
