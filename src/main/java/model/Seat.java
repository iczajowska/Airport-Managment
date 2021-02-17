package model;


import javax.persistence.*;

@Entity
@Table(name = "SEAT")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seatID;

    @Enumerated(EnumType.STRING)
    private TypeClass typeClass;
    private int seatNumber;
    private boolean isBooked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flightID", referencedColumnName ="flightID", nullable = false)
    Flight flight;

    @OneToOne(mappedBy="seat")
    Reservation reservation;

    public Seat(){}

    public Seat(TypeClass typeClass, int seatNumber, Flight flight) {
        this.typeClass = typeClass;
        this.seatNumber = seatNumber;
        this.flight = flight;
    }

    public TypeClass getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(TypeClass typeClass) {
        this.typeClass = typeClass;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
