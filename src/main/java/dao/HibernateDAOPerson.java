package dao;

import model.Person;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateDAOPerson implements DAO<Person, Integer> {

    private Session session;
    public HibernateDAOPerson(Session session) {this.session=session;}

    @Override
    public void persist(Person entity) {
        Transaction tx = session.beginTransaction();
        session.save(entity);
        tx.commit();
    }

    @Override
    public void update(Person entity) {
        Transaction tx = session.beginTransaction();
        session.update(entity);
        tx.commit();
    }

    @Override
    public Person findById(Integer ID) {
        Transaction tx = session.beginTransaction();
        Person Person = session.get(Person.class, ID);
        tx.commit();
        return Person;
    }

    @Override
    public void delete(Person entity) {
        Transaction tx = session.beginTransaction();
        session.delete(entity);
        tx.commit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Person> findAll() {
        Transaction tx = session.beginTransaction();
        List<Person> persons = (List<Person>) session.createQuery("from Person ").getResultList();
        tx.commit();
        return persons;
    }

    @Override
    public void deleteAll() {
        Transaction tx = session.beginTransaction();
        List<Person> Persons = findAll();
        for (Person person : Persons) {
            delete(person);
        }
        tx.commit();
    }
}
