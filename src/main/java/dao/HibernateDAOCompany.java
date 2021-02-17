package dao;

import model.Company;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateDAOCompany implements DAO<Company, Integer> {

    private Session session;

    public HibernateDAOCompany(Session session) {this.session=session;}

    @Override
    public void persist(Company entity) {
        Transaction tx = session.beginTransaction();
        session.save(entity);
        tx.commit();
    }

    @Override
    public void update(Company entity) {
        Transaction tx = session.beginTransaction();
        session.update(entity);
        tx.commit();
    }

    @Override
    public Company findById(Integer ID) {
        Transaction tx = session.beginTransaction();
        Company Company = session.get(Company.class, ID);
        tx.commit();
        return Company;
    }

    @Override
    public void delete(Company entity) {
        Transaction tx = session.beginTransaction();
        session.delete(entity);
        tx.commit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Company> findAll() {
        Transaction tx = session.beginTransaction();
        List<Company> companys = (List<Company>) session.createQuery("from Company ").getResultList();
        tx.commit();
        return companys;
    }

    @Override
    public void deleteAll() {
        Transaction tx = session.beginTransaction();
        List<Company> Companies = findAll();
        for (Company company : Companies) {
            delete(company);
        }
        tx.commit();
    }
}
