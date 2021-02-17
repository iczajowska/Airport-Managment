package service;

import org.hibernate.Session;
import dao.DAO;
import dao.HibernateDAOAirport;
import model.Airport;

import java.util.List;

public class AirportService {
    private static DAO<Airport, Integer> airportDAO;

    public AirportService(Session session){
        airportDAO = new HibernateDAOAirport(session);
    }

    public void persist(Airport airport) {
        airportDAO.persist(airport);
    }

    public void update(Airport airport) {
        airportDAO.update(airport);
    }

    public Airport findById(Integer ID) {
        return airportDAO.findById(ID);
    }

    public void delete(Airport airport) {
        airportDAO.delete(airport);
    }

    public List<Airport> findAll() {
        return airportDAO.findAll();
    }

    public void deleteAll() {
        airportDAO.deleteAll();
    }

}
