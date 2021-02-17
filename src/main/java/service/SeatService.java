package service;

import org.hibernate.Session;
import dao.DAO;
import dao.HibernateDAOSeat;
import model.Seat;

import java.util.List;

public class SeatService {
    private static DAO<Seat, Integer> seatDAO;

    public SeatService(Session session){
        seatDAO = new HibernateDAOSeat(session);
    }

    public void persist(Seat seat) {
        seatDAO.persist(seat);
    }

    public void update(Seat seat) {
        seatDAO.update(seat);
    }

    public Seat findById(Integer ID) {
        return seatDAO.findById(ID);
    }

    public void delete(Seat seat) {
        seatDAO.delete(seat);
    }

    public List<Seat> findAll() {
        return seatDAO.findAll();
    }

    public void deleteAll() {
        seatDAO.deleteAll();
    }

}
