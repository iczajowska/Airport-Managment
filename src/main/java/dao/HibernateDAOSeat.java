package dao;

import model.Seat;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateDAOSeat implements DAO<Seat, Integer> {

    private Session session;

    public HibernateDAOSeat(Session session) {this.session=session;}

    @Override
    public void persist(Seat entity) {
        Transaction tx = session.beginTransaction();
        session.save(entity);
        tx.commit();
    }

    @Override
    public void update(Seat entity) {
        Transaction tx = session.beginTransaction();
        session.update(entity);
        tx.commit();
    }

    @Override
    public Seat findById(Integer ID) {
        Transaction tx = session.beginTransaction();
        Seat Seat = session.get(Seat.class, ID);
        tx.commit();
        return Seat;
    }

    @Override
    public void delete(Seat entity) {
        Transaction tx = session.beginTransaction();
        session.delete(entity);
        tx.commit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Seat> findAll() {
        Transaction tx = session.beginTransaction();
        List<Seat> seats = (List<Seat>) session.createQuery("from Seat ").getResultList();
        tx.commit();
        return seats;
    }

    @Override
    public void deleteAll() {
        Transaction tx = session.beginTransaction();
        List<Seat> Seats = findAll();
        for (Seat seat : Seats) {
            delete(seat);
        }
        tx.commit();
    }
}
