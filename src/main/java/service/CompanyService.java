package service;

import org.hibernate.Session;
import dao.DAO;
import dao.HibernateDAOCompany;
import model.Company;

import java.util.List;

public class CompanyService {
    private static DAO<Company, Integer> companyDAO;

    public CompanyService(Session session){
        companyDAO = new HibernateDAOCompany(session);
    }

    public void persist(Company company) {
        companyDAO.persist(company);
    }

    public void update(Company company) {
        companyDAO.update(company);
    }

    public Company findById(Integer ID) {
        return companyDAO.findById(ID);
    }

    public void delete(Company company) {
        companyDAO.delete(company);
    }

    public List<Company> findAll() {
        return companyDAO.findAll();
    }

    public void deleteAll() {
        companyDAO.deleteAll();
    }

}
