package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.UserServiceImpl;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class UserController {

    @FXML
    private ListView<String> userListView;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;
    @FXML
    private Button goToOrderButton;
    @FXML
    public void handleGoToOrder() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn.esprit.views/order.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) goToOrderButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Commandes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private final UserServiceImpl userService = new UserServiceImpl();

    // Méthode appelée automatiquement après le chargement du FXML
    @FXML
    public void initialize() {
        loadUsers();
    }

    // Charger tous les utilisateurs dans la ListView
    private void loadUsers() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (User user : userService.getAll()) {
            items.add(user.getId() + " - " + user.getUsername() + " (" + user.getEmail() + ")");
        }
        userListView.setItems(items);
    }


    // Ajouter un utilisateur
    @FXML
    private void handleAddUser() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();

        if (!username.isEmpty() && !email.isEmpty()) {
            User user = new User(0, username, email); // id = 0 car auto-increment en DB
            userService.add(user);
            loadUsers(); // recharger la liste
            usernameField.clear();
            emailField.clear();
        }
    }

    // Supprimer un utilisateur sélectionné
    @FXML
    private void handleDeleteUser() {
        String selected = userListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                int id = Integer.parseInt(selected.split(" - ")[0]);
                userService.delete(id);
                loadUsers();
            } catch (NumberFormatException e) {
                System.err.println("Erreur lors de la suppression : id invalide");
            }
        }
    }
}