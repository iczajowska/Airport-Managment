package service;

import org.hibernate.Session;
import dao.DAO;
import dao.HibernateDAOPrice;
import model.Price;

import java.util.List;

public class PriceService {
    private static DAO<Price, Integer> priceDAO;

    public PriceService(Session session){
        priceDAO = new HibernateDAOPrice(session);
    }

    public void persist(Price price) {
        priceDAO.persist(price);
    }

    public void update(Price price) {
        priceDAO.update(price);
    }

    public Price findById(Integer ID) {
        return priceDAO.findById(ID);
    }

    public void delete(Price price) {
        priceDAO.delete(price);
    }

    public List<Price> findAll() {
        return priceDAO.findAll();
    }

    public void deleteAll() {
        priceDAO.deleteAll();
    }

}
