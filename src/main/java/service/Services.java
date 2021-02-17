package service;

import org.hibernate.Session;

public class Services {
    private final AccountService accountService;
    private final AirportService airportService;
    private final CompanyService companyService;
    private final FlightService flightService;
    private final PersonService personService;
    private final PlaneService planeService;
    private final PriceService priceService;
    private final ReservationService reservationService;
    private final SeatService seatService;

    public Services (Session session) {

        this.personService = new PersonService(session);
        this.accountService = new AccountService(session, personService);
        this.companyService = new CompanyService(session);
        this.planeService = new PlaneService(session);
        this.seatService = new SeatService(session);
        this.flightService = new FlightService(session);
        this.reservationService = new ReservationService(session, accountService, flightService, seatService);
        this.airportService = new AirportService(session);
        this.priceService = new PriceService(session);
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public AirportService getAirportService() {
        return airportService;
    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public FlightService getFlightService() {
        return flightService;
    }

    public PersonService getPersonService() {
        return personService;
    }

    public PlaneService getPlaneService() {
        return planeService;
    }

    public PriceService getPriceService() {
        return priceService;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }

    public SeatService getSeatService() {
        return seatService;
    }
}
