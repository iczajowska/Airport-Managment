package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "AIRPORT")
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int airportID;

    private String name;
    private int gmt_offset;
    private String city;
    private String airport_code;

    @OneToMany(mappedBy = "departureAirport",cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Flight> departureFlights = new ArrayList<>();

    @OneToMany(mappedBy = "arrivalAirport",cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Flight> arrivalFlights = new ArrayList<>();

    public Airport(){}

    public Airport(String name, int gmt_offset, String city, String airport_code) {
        this.name = name;
        this.gmt_offset = gmt_offset;
        this.city = city;
        this.airport_code = airport_code;
    }

    public String getAirport_code() {
        return airport_code;
    }

    public void setAirport_code(String airport_code) {
        this.airport_code = airport_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGmt_offset() {
        return gmt_offset;
    }

    public void setGmt_offset(int gmt_offset) {
        this.gmt_offset = gmt_offset;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public List<Flight> getDepartureFlights() {
        return departureFlights;
    }

    public List<Flight> getArrivalFlights() {
        return arrivalFlights;
    }
}
