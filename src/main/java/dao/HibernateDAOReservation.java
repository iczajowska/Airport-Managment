package dao;

import model.Reservation;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateDAOReservation implements DAO<Reservation, Integer> {

    private Session session;

    public HibernateDAOReservation(Session session) {this.session=session;}

    @Override
    public void persist(Reservation entity) {
        Transaction tx = session.beginTransaction();
        session.save(entity);
        tx.commit();
    }

    @Override
    public void update(Reservation entity) {
        Transaction tx = session.beginTransaction();
        session.update(entity);
        tx.commit();
    }

    @Override
    public Reservation findById(Integer ID) {
        Transaction tx = session.beginTransaction();
        Reservation Reservation = session.get(Reservation.class, ID);
        tx.commit();
        return Reservation;
    }

    @Override
    public void delete(Reservation entity) {
        Transaction tx = session.beginTransaction();
        session.delete(entity);
        tx.commit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Reservation> findAll() {
        Transaction tx = session.beginTransaction();
        List<Reservation> reservations = (List<Reservation>) session.createQuery("from Reservation ").getResultList();
        tx.commit();
        return reservations;
    }

    @Override
    public void deleteAll() {
        Transaction tx = session.beginTransaction();
        List<Reservation> Reservations = findAll();
        for (Reservation reservation : Reservations) {
            delete(reservation);
        }
        tx.commit();
    }
}
