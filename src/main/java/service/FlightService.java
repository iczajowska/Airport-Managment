package service;

import org.hibernate.Session;
import dao.DAO;
import dao.HibernateDAOFlight;
import model.Flight;

import java.util.List;

public class FlightService {
    private static DAO<Flight, Integer> flightDAO;

    public FlightService(Session session){
        flightDAO = new HibernateDAOFlight(session);
    }

    public void persist(Flight flight) {
        flightDAO.persist(flight);
    }

    public void update(Flight flight) {
        flightDAO.update(flight);
    }

    public Flight findById(Integer ID) {
        return flightDAO.findById(ID);
    }

    public void delete(Flight flight) {
        flightDAO.delete(flight);
    }

    public List<Flight> findAll() {
        return flightDAO.findAll();
    }

    public void deleteAll() {
        flightDAO.deleteAll();
    }

}
