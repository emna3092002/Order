package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import tn.esprit.entities.Order;
import tn.esprit.entities.Product;
import tn.esprit.entities.User;
import tn.esprit.services.OrderServiceImpl;
import tn.esprit.services.ProductServiceImpl;
import tn.esprit.services.UserServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

    @FXML
    private ComboBox<User> userComboBox;

    @FXML
    private ListView<Product> productListView;

    @FXML
    private ListView<Order> orderListView;

    private OrderServiceImpl orderService;
    private UserServiceImpl userService;
    private ProductServiceImpl productService;
    @FXML
    private void handleGoToProduct(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn.esprit.views" +
                    "/product.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        orderService = new OrderServiceImpl();
        userService = new UserServiceImpl();
        productService = new ProductServiceImpl();

        ObservableList<User> users = FXCollections.observableArrayList(userService.getAll());
        userComboBox.setItems(users);

        ObservableList<Product> products = FXCollections.observableArrayList(productService.getAll());
        productListView.setItems(products);
        productListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        loadOrders();
    }

    private void loadOrders() {
        List<Order> orders = orderService.getAll();
        orderListView.setItems(FXCollections.observableArrayList(orders));
    }

    @FXML
    public void handleAddOrder() {
        User selectedUser = userComboBox.getSelectionModel().getSelectedItem();
        ObservableList<Product> selectedProducts = productListView.getSelectionModel().getSelectedItems();

        if (selectedUser == null || selectedProducts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un utilisateur et au moins un produit !");
            alert.showAndWait();
            return;
        }

        Order order = new Order(0, selectedUser, new ArrayList<>(selectedProducts));
        orderService.add(order);

        loadOrders();
    }

    @FXML
    public void handleDeleteOrder() {
        Order selectedOrder = orderListView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            orderService.delete(selectedOrder.getIdOrder());
            loadOrders();
        }
    }
}
