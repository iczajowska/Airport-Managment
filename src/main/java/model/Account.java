package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ACCOUNT")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountID;

    private String bankAccountNumber;
    private String email;
    private String password;

    @OneToOne
    @JoinColumn(name="personID")
    private Person person;


    @Column
    @Enumerated
    @ElementCollection(targetClass = Authorization.class, fetch = FetchType.EAGER)
    //Collection<InterestsEnum> interests;
    private List<Authorization> authorizations = new ArrayList<>();

    @OneToMany(mappedBy = "account",cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    public Account(){}
    public Account(Person person, String bankAccountNumber, String email, String password) {
        this.person = person;
        this.bankAccountNumber = bankAccountNumber;
        this.email = email;
        this.password = password;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    //TODO
    public List<Reservation> getFutureReservations() { return new ArrayList<>(); }

    public List<Reservation> getPastReservations() {
        return new ArrayList<>();
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {reservations.remove(reservation); }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Authorization> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(List<Authorization> authorizations) {
        this.authorizations = authorizations;
    }

}
