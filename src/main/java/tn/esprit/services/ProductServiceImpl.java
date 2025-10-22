package tn.esprit.services;

import tn.esprit.entities.Category;
import tn.esprit.entities.Product;
import tn.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements IGenericService<Product> {

    private Connection conn;

    public ProductServiceImpl() {
        try {
            conn = DataBase.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Product product) {
        String sql = "INSERT INTO product (name, price, idCategory) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getCategory().getIdCategory());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE product SET name=?, price=?, idCategory=? WHERE idProduct=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getCategory().getIdCategory());
            ps.setInt(4, product.getIdProduct());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int idProduct) {
        String sql = "DELETE FROM product WHERE idProduct=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProduct);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.idProduct, p.name, p.price, c.idCategory, c.name AS category_name " +
                "FROM product p JOIN category c ON p.idCategory = c.idCategory";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Category cat = new Category(rs.getInt("idCategory"), rs.getString("category_name"));
                Product p = new Product(rs.getInt("idProduct"), rs.getString("name"), rs.getDouble("price"), cat);
                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public Product getById(int idProduct) {
        String sql = "SELECT p.idProduct, p.name, p.price, c.idCategory, c.name AS category_name " +
                "FROM product p JOIN category c ON p.idCategory = c.idCategory WHERE p.idProduct=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProduct);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Category cat = new Category(rs.getInt("idCategory"), rs.getString("category_name"));
                return new Product(rs.getInt("idProduct"), rs.getString("name"), rs.getDouble("price"), cat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
