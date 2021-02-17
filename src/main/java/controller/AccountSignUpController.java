package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.*;
import service.AccountService;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.lang.IllegalArgumentException;

public class AccountSignUpController {
    private AccountService accountService;

    private AccountAppController appController;

    @FXML
    private TextField user;
    @FXML
    private PasswordField password;
    @FXML
    private TextField firstName;
    @FXML
    private TextField surname;
    @FXML
    private TextField documentID;
    @FXML
    private TextField documentType;

    @FXML
    private Button signUpButton;
    @FXML
    private Button returnButton;

    public void initialize() {
    }

    private static boolean isValidEmail(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    private boolean isAdmin(String email){
        String regex = "^(.+)@admin[.]com$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }
    private boolean isEmailUnique(String email){
        return FXCollections.observableArrayList(this.accountService.findAll())
                .stream().noneMatch(account -> account.getEmail().equals(email));
    }
    private String checkUserEmail(String userEmail) {
        if(isValidEmail(userEmail) && isEmailUnique(userEmail)){
            return userEmail;
        }else{
            throw new IllegalArgumentException("WRONG EMAIL!|Enter correct email.");
        }
    }

    private String checkUserFirstName(String userFirstName) {
        if(userFirstName.matches( "[A-Z][a-z]*" )) {
            return userFirstName;
        }
        throw new IllegalArgumentException("WRONG FIRST NAME|Your first name should start with uppercase letter followed by lower case (no digits or special signs).");
    }

    private String checkUserSurname(String userSurname) {
        if(userSurname.matches( "[A-Z][a-z]*([ -][A-Z][a-z]*)*" )) {
            return userSurname;
        }
        throw new IllegalArgumentException("WRONG SURNAME|Your surname should start with uppercase letter followed by lower case (no digits or special signs).");
    }

    private String checkUserPassword(String userPassword) {
        if(userPassword.length() > 3) {
            return userPassword;
        }
        throw new IllegalArgumentException("WRONG PASSWORD|Your password is too short. Enter at least 4 characters.");
    }

    private String checkUserDocumentID(String userDocumentID) {
        if(userDocumentID.length() > 6 && userDocumentID.length() < 10) {
            return userDocumentID;
        }
        throw new IllegalArgumentException("WRONG DOCUMENT ID|Your document ID should be 7, 8 or 9 characters long.");
    }

    private TypeDocument checkUserDocumentType(String userDocumentType) {
        if(userDocumentType.equals( "ID"))
            return TypeDocument.IDENTITY_DOCUMENT;
        else if(userDocumentType.equals("PASSPORT")){
            return TypeDocument.PASSPORT;
        } else{
            throw new IllegalArgumentException("WRONG DOCUMENT TYPE!|Enter: ID, PASSPORT");
        }
    }

    private ArrayList<Authorization> addAuthorizationForEmail(String userEmail) {
        ArrayList<Authorization> authorizations = new ArrayList<>();
        if(isAdmin(userEmail)){
            authorizations.add(Authorization.ADD_DELETE_EDIT_COMPANY);
            authorizations.add(Authorization.ADD_AIRPORT);
            authorizations.add(Authorization.DELETE_EDIT_AIRPORT);
            authorizations.add(Authorization.ADD_FLIGHT);
            authorizations.add(Authorization.DELETE_EDIT_FLIGHT);
        }else{
            authorizations.add(Authorization.ADD_DELETE_EDIT_RESERVATION);
        }
        return authorizations;
    }

    @FXML
    public void handleSignUpAction(ActionEvent actionEvent) {
        String userEmail = user.getText();
        String userPassword = password.getText();
        String userFirstName = firstName.getText();
        String userSurname = surname.getText();
        String userDocumentID = documentID.getText();
        String userDocumentType = documentType.getText();
        TypeDocument td;
        ArrayList<Authorization> authorizations = new ArrayList<Authorization>();

        try {
            userEmail = checkUserEmail(userEmail);
            userPassword = checkUserPassword(userPassword);
            userFirstName = checkUserFirstName(userFirstName);
            userSurname = checkUserSurname(userSurname);
            userDocumentID = checkUserDocumentID(userDocumentID);
            td = checkUserDocumentType(userDocumentType);
            authorizations = addAuthorizationForEmail(userEmail);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            String[] msgs = e.getMessage().split("[|]");
            alert.setHeaderText(msgs[0]);
            alert.setContentText(msgs[1]);

            alert.showAndWait();
            return;
        }

        Person p = new Person(userFirstName,userSurname,userDocumentID,td);
        Account a = new Account(p, "", userEmail, userPassword );
        a.setAuthorizations(authorizations);
        accountService.persist(a);
        appController.initLoginLayout();
    }

    public void setAppController(AccountAppController accountAppController) {
        this.appController = accountAppController;
    }

    public void setData(AccountService accountService) {
        this.accountService = accountService;
    }


    @FXML
    public void handleReturnAction(ActionEvent actionEvent) {
        appController.initLoginLayout();
    }
}
