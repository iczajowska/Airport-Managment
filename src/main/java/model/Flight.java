package model;

import com.sun.xml.bind.v2.TODO;
import javafx.beans.property.*;

import javax.persistence.*;
import java.sql.SQLOutput;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FLIGHT")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int flightID;

    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Long flightDuration;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "priceID", referencedColumnName = "priceID")
    private Price price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "planeID", referencedColumnName ="planeID", nullable = false)
    private Plane plane;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "airportDeparture", referencedColumnName ="airportID", nullable = false)
    private Airport departureAirport;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "airportArrival", referencedColumnName ="airportID", nullable = false)
    private Airport arrivalAirport;

    @OneToMany(mappedBy = "flight",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations ;
    @OneToMany(mappedBy = "flight",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> allSeats ;

    public Flight(){}

    public Flight(String flightNumber,  Plane plane, Airport departureAirport, Airport arrivalAirport,  LocalDateTime departureDateTime,
                  LocalDateTime arrivalDateTime, Price price) {

        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureDateTime;
        this.arrivalTime = arrivalDateTime;
        this.flightDuration = Duration.between(departureDateTime, arrivalDateTime).toMinutes();
        this.price = price;
        this.plane = plane;
        this.reservations = new ArrayList<Reservation>();
        this.allSeats = plane.getSeatsList(this);
    }


    public Seat bookSeat(TypeClass td){
        Seat seat = null;
        int min = 0;
        int max = 0;
        if(td == TypeClass.FIRST_CLASS){
            max = this.plane.getFirstClassCapacity();
        } else if(td == TypeClass.BUSINESS){
            min = this.plane.getFirstClassCapacity()+1;
            max = this.plane.getBusinessCapacity()+min;
        } else{
            max = this.plane.getCapacity();
            min = max - this.plane.getBusinessCapacity() - this.plane.getFirstClassCapacity();
        }

        for(int i=min; i<max; i++){
            if(!this.allSeats.get(i).isBooked()){
                seat = this.allSeats.get(i);
                this.allSeats.get(i).setBooked(true);
                break;
            }
        }
        return seat;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
        this.allSeats = plane.getSeatsList(this);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void addReservation(Reservation reservation){
        this.reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation){
        this.reservations.remove(reservation);
    }

    public List<Seat> getAllSeats() {
        return allSeats;
    }

    public void setAllSeats(List<Seat> allSeats) {
        this.allSeats = allSeats;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Long getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(Long flightDuration) {
        this.flightDuration = flightDuration;
    }

    public Price getPrice() {
        return price;
    }

    public double getEconomyClassPrice() {
        return price.getEconomyClassPrice();
    }


    public void setPrice(Price price) {
        this.price = price;
    }

    public double getPriceForTypeClass(TypeClass typeClass){
        if(typeClass.equals(TypeClass.ECONOMY))
            return getEconomyClassPrice();
        else if(typeClass.equals(TypeClass.BUSINESS))
            return price.getBusinessClassPrice();
        else
            return price.getFirstClassPrice();
    }

    public double sumOfReservationsBenefit(){
        double sum = 0;
        for(int i=0; i<this.plane.getCapacity(); i++){
            Seat seat = this.allSeats.get(i);
            if(seat.isBooked()){
                sum+=getPriceForTypeClass(seat.getTypeClass());
            }
        }
        System.out.println("Suma: " + sum + "Flight ID" + flightID);
        return sum;
    }

    @Override
    public String toString() {
        return String.join(" ", this.flightNumber,this.plane.getCompany().getName(), this.departureAirport.getAirport_code(), this.arrivalAirport.getAirport_code(), this.departureTime.toString(), this.arrivalTime.toString(), Double.toString( this.price.getEconomyClassPrice()) );
    }
}
