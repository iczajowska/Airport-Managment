package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Entity
@Table(name ="PLANE")
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int planeID;
    private String model;
    private int capacity;
    private int businessCapacity;
    private int firstClassCapacity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "companyID", referencedColumnName ="companyID", nullable = false)
    private Company company;

    public List<Flight> getFlights() {
        return flights;
    }

    @OneToMany(mappedBy = "plane",cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    List<Flight> flights = new ArrayList<>();


//    @OneToMany(mappedBy = "plane",cascade = CascadeType.ALL ,fetch = FetchType.LAZY )
//    private List<Seat> seatsList;

    public Plane(){}

    public Plane(String model, Company company, int capacity, int businessCapacity, int firstClassCapacity) {
        this.model = model;
        this.company = company;
        this.capacity = capacity;
        this.businessCapacity = businessCapacity;
        this.firstClassCapacity = firstClassCapacity;
        //this.seatsList = generateSeats();
    }
    private List<Seat> generateSeats(Flight flight){
        List<Seat> seats = new ArrayList<>();

        for(int i=0; i<this.capacity; i++){
            if(i<this.firstClassCapacity){
                seats.add(new Seat(TypeClass.FIRST_CLASS,i+1,flight));
            } else if(i<this.firstClassCapacity+this.businessCapacity){
                seats.add(new Seat(TypeClass.BUSINESS,i+1, flight));
            }else {
                seats.add(new Seat(TypeClass.ECONOMY,i+1,flight));
            }
        }
        System.out.println("TEST");
        return seats;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getBusinessCapacity() {
        return businessCapacity;
    }

    public void setBusinessCapacity(int businessCapacity) {
        this.businessCapacity = businessCapacity;
    }

    public int getFirstClassCapacity() {
        return firstClassCapacity;
    }

    public void setFirstClassCapacity(int firstClassCapacity) {
        this.firstClassCapacity = firstClassCapacity;
    }

    public List<Seat> getSeatsList(Flight flight){
        return this.generateSeats(flight);
    }

    public Map<Integer, Double> getBenefitForPlane(int year){
        Map<Integer,Double> benefitMap = new TreeMap<>();
        System.out.println("Inside plane");
        for(Flight flight:flights){
            System.out.println("Inside loop");
            if(flight.getDepartureTime().getYear() == year){
                Integer key = flight.getDepartureTime().getMonthValue();
                if(! benefitMap.containsKey(key)){
                    benefitMap.put(key, flight.sumOfReservationsBenefit());
                }
                else{
                    Double currentBenefit = benefitMap.get(key);
                    benefitMap.replace(key, currentBenefit+flight.sumOfReservationsBenefit());
                }
            }
        }
        return benefitMap;
    }
}
