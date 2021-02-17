package dao;

import model.Account;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateDAOAccount implements DAO<Account, Integer> {

    private Session session;

    public HibernateDAOAccount (Session session) {this.session=session;}

    @Override
    public void persist(Account entity) {
        Transaction tx = session.beginTransaction();
        session.save(entity);
        tx.commit();
    }

    @Override
    public void update(Account entity) {
        Transaction tx = session.beginTransaction();
        session.update(entity);
        tx.commit();
    }

    @Override
    public Account findById(Integer ID) {
        Transaction tx = session.beginTransaction();
        Account account = session.get(Account.class, ID);
        tx.commit();
        return account;
    }

    @Override
    public void delete(Account entity) {
        Transaction tx = session.beginTransaction();
        session.delete(entity);
        tx.commit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Account> findAll() {
        Transaction tx = session.beginTransaction();
        List<Account> accounts = (List<Account>) session.createQuery("from Account ").getResultList();
        tx.commit();
        return accounts;
    }

    @Override
    public void deleteAll() {
        Transaction tx = session.beginTransaction();
        List<Account> accounts = findAll();
        for (Account acc : accounts) {
            delete(acc);
        }
        tx.commit();
    }
}
