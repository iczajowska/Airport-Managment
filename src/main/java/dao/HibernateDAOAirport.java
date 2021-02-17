package dao;

import model.Airport;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateDAOAirport implements DAO<Airport, Integer> {

    private Session session;

    public HibernateDAOAirport(Session session) {this.session=session;}

    @Override
    public void persist(Airport entity) {
        Transaction tx = session.beginTransaction();
        session.save(entity);
        tx.commit();
    }

    @Override
    public void update(Airport entity) {
        Transaction tx = session.beginTransaction();
        session.update(entity);
        tx.commit();
    }

    @Override
    public Airport findById(Integer ID) {
        Transaction tx = session.beginTransaction();
        Airport Airport = session.get(Airport.class, ID);
        tx.commit();
        return Airport;
    }

    @Override
    public void delete(Airport entity) {
        Transaction tx = session.beginTransaction();
        session.delete(entity);
        tx.commit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Airport> findAll() {
        Transaction tx = session.beginTransaction();
        List<Airport> airports = (List<Airport>) session.createQuery("from Airport ").getResultList();
        tx.commit();
        return airports;
    }

    @Override
    public void deleteAll() {
//        Session session = getSession();
        Transaction tx = session.beginTransaction();
        List<Airport> Airports = findAll();
        for (Airport airport : Airports) {
            delete(airport);
        }
        tx.commit();
    }
}
