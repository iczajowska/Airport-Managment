package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Account;
import model.Person;
import model.TypeDocument;
import model.Authorization;
import service.AccountService;

import java.util.List;

public class AccountLoginController {
    private AccountService accountService;

    private AccountAppController appController;

    @FXML
    private TextField user;
    @FXML
    private PasswordField password;

    @FXML private Button loginButton;

    public void initialize() {

    }

    public void setAppController(AccountAppController appController) {
        this.appController = appController;
    }

    public void setData(AccountService accountService) {
        this.accountService = accountService;
    }

    private Account getAccountByEmail(String email){
        return FXCollections.observableArrayList(accountService.findAll())
                .stream().filter(account -> account.getEmail().equals(email)).findFirst().orElse(null);
    }

    @FXML
    public void handleLogInAction(ActionEvent actionEvent) {
        String userEmail = user.getText();
        String userPassword = password.getText();

        Account account = getAccountByEmail(userEmail);

        if(account!=null && account.getPassword().equals(userPassword)){
            if(account.getAuthorizations().contains(Authorization.ADD_DELETE_EDIT_RESERVATION)){
                appController.setCurrentAccount(account);
                appController.showAllFlights();
            } else if (account.getAuthorizations().contains(Authorization.ADD_DELETE_EDIT_COMPANY)){
                appController.setCurrentAccount(account);
                appController.showAdminPanel();
            }

        }
    }

    public void handleSignUpAction(ActionEvent actionEvent) {
        appController.showSignUpLayout();
    }
}

