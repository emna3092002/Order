package tn.esprit.services;

import tn.esprit.entities.Category;
import tn.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImpl implements IGenericService<Category> {

    private Connection conn;

    public CategoryServiceImpl() {
        try {
            conn = DataBase.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Category c) {
        addCategory(c); // méthode spécifique
    }

    public void addCategory(Category c) {
        String sql = "INSERT INTO category (name) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Category c) {
        String sql = "UPDATE category SET name=? WHERE idCategory=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setInt(2, c.getIdCategory());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        deleteCategory(id); // méthode spécifique
    }

    public void deleteCategory(int idCategory) {
        String sql = "DELETE FROM category WHERE idCategory=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategory);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Category getById(int idCategory) {
        Category category = null;
        String sql = "SELECT * FROM category WHERE idCategory=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategory);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                category = new Category(rs.getInt("idCategory"), rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public List<Category> getAll() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM category";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Category(rs.getInt("idCategory"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
