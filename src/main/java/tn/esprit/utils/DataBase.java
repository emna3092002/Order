package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "orderdb";
    private static final String USER = "root";       // ton utilisateur MySQL
    private static final String PASSWORD = "";       // ton mot de passe MySQL

    /**
     * Retourne une connexion à la base de données.
     * Crée la base si elle n'existe pas.
     */
    public static Connection getConnection() throws SQLException {
        // Connexion au serveur MySQL
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

        // Crée la base si elle n'existe pas
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        }

        // Connexion à la base créée
        return DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
    }

    /**
     * Crée toutes les tables si elles n'existent pas.
     */
    public static void initTables() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // Table User
            String userSql = "CREATE TABLE IF NOT EXISTS user (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL," +
                    "email VARCHAR(100) NOT NULL" +
                    ");";
            stmt.executeUpdate(userSql);

            // Table Category
            String categorySql = "CREATE TABLE IF NOT EXISTS category (" +
                    "idCategory INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL" +
                    ");";
            stmt.executeUpdate(categorySql);

            // Table Product
            String productSql = "CREATE TABLE IF NOT EXISTS product (" +
                    "idProduct INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "price DOUBLE NOT NULL," +
                    "idCategory INT," +
                    "FOREIGN KEY (idCategory) REFERENCES category(idCategory) ON DELETE SET NULL" +
                    ");";
            stmt.executeUpdate(productSql);

            // Table Order
            String orderSql = "CREATE TABLE IF NOT EXISTS `order` (\n" +
                    "    idOrder INT AUTO_INCREMENT PRIMARY KEY,\n" +
                    "    user_id INT NOT NULL,\n" +
                    "    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                    "    total DOUBLE,\n" +
                    "    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE\n" +
                    ");\n";
            stmt.executeUpdate(orderSql);
            String orderProductSql = "CREATE TABLE IF NOT EXISTS order_product (\n" +
                    "    order_id INT,\n" +
                    "    product_id INT,\n" +
                    "    PRIMARY KEY(order_id, product_id),\n" +
                    "    FOREIGN KEY (order_id) REFERENCES `order`(idOrder) ON DELETE CASCADE,\n" +
                    "    FOREIGN KEY (product_id) REFERENCES product(idProduct) ON DELETE CASCADE\n" +
                    ");\n";
            stmt.executeUpdate(orderProductSql);

            System.out.println("Base et toutes les tables créées ou déjà existantes.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}