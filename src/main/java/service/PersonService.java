package service;

import org.hibernate.Session;
import dao.DAO;
import dao.HibernateDAOPerson;
import model.Person;

import java.util.List;

public class PersonService {
    private static DAO<Person, Integer> personDAO;

    public PersonService(Session session){
        personDAO = new HibernateDAOPerson(session);
    }

    public void persist(Person person) {
        personDAO.persist(person);
    }

    public void update(Person person) {
        personDAO.update(person);
    }

    public Person findById(Integer ID) {
        return personDAO.findById(ID);
    }

    public void delete(Person person) {
        personDAO.delete(person);
    }

    public List<Person> findAll() {
        return personDAO.findAll();
    }

    public void deleteAll() {
        personDAO.deleteAll();
    }

}
