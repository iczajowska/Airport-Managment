package model;


import javax.persistence.*;

@Entity
@Table(name = "PRICE")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int priceID;

    private double businessClassPrice;
    private double economyClassPrice;
    private double firstClassPrice;

    @OneToOne(mappedBy = "price")
    Flight flight;

    public Price(){}

    public Price(double economyClassPrice, double businessClassPrice, double firstClassPrice) {
        this.businessClassPrice = businessClassPrice;
        this.economyClassPrice = economyClassPrice;
        this.firstClassPrice = firstClassPrice;
    }

    public double getBusinessClassPrice() {
        return businessClassPrice;
    }

    public void setBusinessClassPrice(int businessClassPrice) {
        this.businessClassPrice = businessClassPrice;
    }

    public double getEconomyClassPrice() {
        return economyClassPrice;
    }

    public void setEconomyClassPrice(int economyClassPrice) {
        this.economyClassPrice = economyClassPrice;
    }

    public double getFirstClassPrice() {
        return firstClassPrice;
    }

    public void setFirstClassPrice(int firstClassPrice) {
        this.firstClassPrice = firstClassPrice;
    }

    public double getPriceByType(TypeClass typeClass){
        switch (typeClass){
            case FIRST_CLASS:
                return getFirstClassPrice();
            case BUSINESS:
                return getBusinessClassPrice();
            case ECONOMY:
                return getEconomyClassPrice();
            default:
                throw new IllegalArgumentException("Wrong type class getPriceByType function\n");
        }
    }

    public void setPrices(double economy, double business, double firstClass){
        this.economyClassPrice=economy;
        this.businessClassPrice=business;
        this.firstClassPrice=firstClass;
    }

    @Override
    public String toString() {
        return String.format("economy class price: %f\nbusiness class price: %f\nfirst class price: %f", this.economyClassPrice, this.businessClassPrice, this.firstClassPrice);
    }

}
