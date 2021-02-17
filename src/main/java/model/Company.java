package model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "COMPANY")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int companyID;

    String name;
    String street;
    String number;
    String city;
    String country;
    String postcode;
    String NIP;
    String REGON;

    @OneToMany(mappedBy = "company",cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    List<Plane> planes = new ArrayList<>();

    public Company(){}

    public Company(String name, String street, String number, String city, String country, String postcode, String NIP, String REGON) {
        this.name = name;
        this.street = street;
        this.number = number;
        this.city = city;
        this.country = country;
        this.postcode = postcode;
        this.NIP = NIP;
        this.REGON = REGON;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getNIP() {
        return NIP;
    }

    public void setNIP(String NIP) {
        this.NIP = NIP;
    }

    public String getREGON() {
        return REGON;
    }

    public void setREGON(String REGON) {
        this.REGON = REGON;
    }

    public List<Plane> getPlanes() {
        return planes;
    }

    public void setPlanes(List<Plane> planes) {
        this.planes = planes;
    }

    public void addPlane(Plane plane){
        planes.add(plane);
    }

    public void removePlane(Plane plane){
        planes.remove(plane);
    }

    public Map<Integer, Double> getBenefit(int year){
        Map <Integer, Double> benefits = new TreeMap<>();
        for(Plane plane:planes){
            Map<Integer, Double> pMap = plane.getBenefitForPlane(year);
            for(int key=1;key<=12; key++){
                if(pMap.containsKey(key)){
                    if(! benefits.containsKey(key)){
                        benefits.put(key, pMap.get(key));
                    }
                    else{
                        Double currentBenefit = benefits.get(key);
                        benefits.replace(key, currentBenefit+pMap.get(key));
                    }
                }
            }
        }
        return benefits;

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return NIP.equals(company.NIP) &&
                REGON.equals(company.REGON);
    }

    @Override
    public int hashCode() {
        return Objects.hash(NIP, REGON);
    }
}
