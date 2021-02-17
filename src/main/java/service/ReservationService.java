package service;

import org.hibernate.Session;
import dao.DAO;
import dao.HibernateDAOReservation;
import model.Reservation;

import java.util.List;

public class ReservationService {
    private static DAO<Reservation, Integer> reservationDAO;
    private final AccountService accountService;
    private final FlightService flightService;
    private final SeatService seatService;

    public ReservationService(Session session, AccountService accountService, FlightService flightService, SeatService seatService){
        reservationDAO = new HibernateDAOReservation(session);
        this.accountService = accountService;
        this.flightService = flightService;
        this.seatService=seatService;
    }

    public void persist(Reservation reservation) {
        reservation.getAccount().addReservation(reservation);
        reservation.getFlight().addReservation(reservation);
        reservation.getSeat().setBooked(true);

        accountService.update(reservation.getAccount());
        flightService.update(reservation.getFlight());
        seatService.update(reservation.getSeat());
        reservationDAO.persist(reservation);
    }

    public void update(Reservation reservation) {
        reservationDAO.update(reservation);
    }

    public Reservation findById(Integer ID) {
        return reservationDAO.findById(ID);
    }

    public void delete(Reservation reservation) {
        reservation.getAccount().removeReservation(reservation);
        reservation.getFlight().removeReservation(reservation);
        reservation.getSeat().setBooked(false);

        accountService.update(reservation.getAccount());
        flightService.update(reservation.getFlight());
        seatService.update(reservation.getSeat());
        reservationDAO.delete(reservation);
    }

    public List<Reservation> findAll() {
        return reservationDAO.findAll();
    }

    public void deleteAll() {
        reservationDAO.deleteAll();
    }



}
