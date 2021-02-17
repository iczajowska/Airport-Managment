package service;

import org.hibernate.Session;
import dao.DAO;
import dao.HibernateDAOAccount;
import model.Account;

import java.util.List;

public class AccountService {
    private static DAO<Account, Integer> accountDAO;
    private final PersonService personService;

    public AccountService(Session session, PersonService personService){
        this.personService = personService;
        accountDAO = new HibernateDAOAccount(session);
    }

    public void persist(Account account) {
        personService.persist(account.getPerson());
        accountDAO.persist(account);
    }

    public void update(Account account) {
        accountDAO.update(account);
    }

    public Account findById(Integer ID) {
        return accountDAO.findById(ID);
    }

    public void delete(Account account) {
        accountDAO.delete(account);
    }

    public List<Account> findAll() {
        return accountDAO.findAll();
    }

    public void deleteAll() {
        accountDAO.deleteAll();
    }



}
