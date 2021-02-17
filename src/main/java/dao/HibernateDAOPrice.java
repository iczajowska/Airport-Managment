package dao;

import model.Price;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateDAOPrice implements DAO<Price, Integer> {

    private Session session;
    public HibernateDAOPrice(Session session) {this.session=session;}

    @Override
    public void persist(Price entity) {
        Transaction tx = session.beginTransaction();
        session.save(entity);
        tx.commit();
    }

    @Override
    public void update(Price entity) {
        Transaction tx = session.beginTransaction();
        session.update(entity);
        tx.commit();
    }

    @Override
    public Price findById(Integer ID) {
        Transaction tx = session.beginTransaction();
        Price Price = session.get(Price.class, ID);
        tx.commit();
        return Price;
    }

    @Override
    public void delete(Price entity) {
        Transaction tx = session.beginTransaction();
        session.delete(entity);
        tx.commit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Price> findAll() {
        Transaction tx = session.beginTransaction();
        List<Price> prices = (List<Price>) session.createQuery("from Price ").getResultList();
        tx.commit();
        return prices;
    }

    @Override
    public void deleteAll() {
        Transaction tx = session.beginTransaction();
        List<Price> Prices = findAll();
        for (Price price : Prices) {
            delete(price);
        }
        tx.commit();
    }
}
