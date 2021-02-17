package model;


import javax.persistence.*;

@Entity
@Table(name= "PERSON")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personID;

    private String firstName;
    private String surname;
    private String documentID;


    public void setAccount(Account account) {
        this.account = account;
    }

    @OneToOne(mappedBy="person")
    private Account account;

    @Enumerated(EnumType.STRING)
    private TypeDocument document;

    public Person(){}

    public Person(String firstName, String surname, String documentID, TypeDocument document) {
        this.firstName = firstName;
        this.surname = surname;
        this.documentID = documentID;
        this.document = document;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public TypeDocument getDocument() {
        return document;
    }

    public void setDocument(TypeDocument document) {
        this.document = document;
    }
}
