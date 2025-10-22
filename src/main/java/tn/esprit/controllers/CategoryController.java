package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import tn.esprit.entities.Category;
import tn.esprit.services.CategoryServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CategoryController {

    @FXML
    private ListView<Category> listViewCategories;

    @FXML
    private Button btnGoToProduct;

    @FXML
    private Button btnAddCategory;

    @FXML
    private Button btnDeleteCategory;

    private CategoryServiceImpl categoryService;

    @FXML
    public void initialize() {
        categoryService = new CategoryServiceImpl();
        loadCategories();
    }

    private void loadCategories() {
        List<Category> categories = categoryService.getAll();
        ObservableList<Category> observableList = FXCollections.observableArrayList(categories);
        listViewCategories.setItems(observableList);
    }

    @FXML
    public void handleGoToProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/views/product.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnGoToProduct.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddCategory() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Category");
        dialog.setHeaderText("Enter category name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            Category c = new Category(0, name); // id sera auto_increment
            categoryService.addCategory(c);
            loadCategories();
        });
    }

    @FXML
    public void handleDeleteCategory() {
        Category selected = listViewCategories.getSelectionModel().getSelectedItem();
        if (selected != null) {
            categoryService.deleteCategory(selected.getIdCategory());
            loadCategories();
        }
    }
}
