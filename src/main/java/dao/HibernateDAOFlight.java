package dao;

import model.Flight;
import org.hibernate.Session;
import org.hibernate.Transaction;


import java.util.List;

public class HibernateDAOFlight implements DAO<Flight, Integer> {

    private Session session;

    public HibernateDAOFlight(Session session) {this.session=session;}

    @Override
    public void persist(Flight entity) {
        Transaction tx = session.beginTransaction();
        session.save(entity);
        tx.commit();
    }

    @Override
    public void update(Flight entity) {
        Transaction tx = session.beginTransaction();
        session.update(entity);
        tx.commit();
    }

    @Override
    public Flight findById(Integer ID) {
        Transaction tx = session.beginTransaction();
        Flight Flight = session.get(Flight.class, ID);
        tx.commit();
        return Flight;
    }

    @Override
    public void delete(Flight entity) {
        Transaction tx = session.beginTransaction();
        session.delete(entity);
        tx.commit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Flight> findAll() {
        Transaction tx = session.beginTransaction();
        List<Flight> flights = (List<Flight>) session.createQuery("from Flight ").getResultList();
        tx.commit();
        return flights;
    }

    @Override
    public void deleteAll() {
        Transaction tx = session.beginTransaction();
        List<Flight> Flights = findAll();
        for (Flight flight : Flights) {
            delete(flight);
        }
        tx.commit();
    }
}
