package model;

import javax.persistence.*;

@Entity
@Table(name = "RESERVATION")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationID;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flightID", referencedColumnName ="flightID", nullable = false)
    private Flight flight;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountID", referencedColumnName ="accountID", nullable = false)
    private Account account;

    @OneToOne
    @JoinColumn(name="seatID")
    private Seat seat;

    public  Reservation(){}

    public Reservation(Account account, Flight flight, Seat seat) {
        this.account = account;
        this.flight = flight;
        this.seat = seat;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }
}
