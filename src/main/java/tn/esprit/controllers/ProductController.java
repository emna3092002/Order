package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Category;
import tn.esprit.entities.Product;
import tn.esprit.services.CategoryServiceImpl;
import tn.esprit.services.IGenericService;
import tn.esprit.services.ProductServiceImpl;

import java.io.IOException;

public class ProductController {

    private IGenericService<Product> productService = new ProductServiceImpl();
    private CategoryServiceImpl categoryService = new CategoryServiceImpl();

    @FXML
    private ListView<Product> productListView;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private ComboBox<Category> categoryComboBox;

    private ObservableList<Product> productList;

    @FXML
    private void handleGoToProduct(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn.esprit.views/category.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }private ObservableList<Category> categoryList;


    @FXML
    private void initialize() {
        loadCategoriesFromDB();
        loadProducts();

        // Afficher le nom des catégories dans la ComboBox
        categoryComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        });
        categoryComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        });
    }

    private void loadProducts() {
        productList = FXCollections.observableArrayList(productService.getAll());
        productListView.setItems(productList);
    }

    private void loadCategoriesFromDB() {
        categoryList = FXCollections.observableArrayList(categoryService.getAll());
        categoryComboBox.setItems(categoryList);
    }

    @FXML
    private void handleAddProduct() {
        String name = nameField.getText();
        String priceText = priceField.getText();
        Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();

        if (name.isEmpty() || priceText.isEmpty() || selectedCategory == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Le prix doit être un nombre valide !");
            alert.showAndWait();
            return;
        }

        Product product = new Product(0, name, price, selectedCategory);
        productService.add(product);

        loadProducts();
        nameField.clear();
        priceField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleDeleteProduct() {
        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un produit !");
            alert.showAndWait();
            return;
        }

        productService.delete(selectedProduct.getIdProduct());
        loadProducts();
    }
}
