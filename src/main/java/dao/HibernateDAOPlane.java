package dao;

import model.Plane;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateDAOPlane implements DAO<Plane, Integer> {

    private Session session;

    public HibernateDAOPlane(Session session) {this.session=session;}

    @Override
    public void persist(Plane entity) {
        Transaction tx = session.beginTransaction();
        session.save(entity);
        tx.commit();
    }

    @Override
    public void update(Plane entity) {
        Transaction tx = session.beginTransaction();
        session.update(entity);
        tx.commit();
    }

    @Override
    public Plane findById(Integer ID) {
        Transaction tx = session.beginTransaction();
        Plane Plane = session.get(Plane.class, ID);
        tx.commit();
        return Plane;
    }

    @Override
    public void delete(Plane entity) {
        Transaction tx = session.beginTransaction();
        session.delete(entity);
        tx.commit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Plane> findAll() {
        Transaction tx = session.beginTransaction();
        List<Plane> planes = (List<Plane>) session.createQuery("from Plane ").getResultList();
        tx.commit();
        return planes;
    }

    @Override
    public void deleteAll() {
        Transaction tx = session.beginTransaction();
        List<Plane> Planes = findAll();
        for (Plane plane : Planes) {
            delete(plane);
        }
        tx.commit();
    }
}
