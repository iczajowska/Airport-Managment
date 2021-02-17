package service;

import org.hibernate.Session;
import dao.DAO;
import dao.HibernateDAOPlane;
import javafx.collections.FXCollections;
import model.Company;
import model.Plane;

import java.util.List;

public class PlaneService {
    private static DAO<Plane, Integer> planeDAO;

    public PlaneService(Session session){
        planeDAO = new HibernateDAOPlane(session);
    }

    public void persist(Plane plane) {
        planeDAO.persist(plane);
    }

    public void update(Plane plane) {
        planeDAO.update(plane);
    }

    public Plane findById(Integer ID) {
        return planeDAO.findById(ID);
    }

    public void delete(Plane plane) {
        planeDAO.delete(plane);
    }

    public List<Plane> findAll() {
        return planeDAO.findAll();
    }

    public void deleteAll() {
        planeDAO.deleteAll();
    }

}
