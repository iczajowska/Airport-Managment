import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import controller.AccountAppController;
import model.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class AccountApp extends Application {

	private Stage primaryStage;
	
	private AccountAppController appController;

	private static final SessionFactory ourSessionFactory;

	static {
		try {
			Configuration configuration = new Configuration();
			configuration.configure();

			ourSessionFactory = configuration.buildSessionFactory();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Session getSession() throws HibernateException {
		return ourSessionFactory.openSession();
	}

	@Override
	public void start(Stage primaryStage) {
		Session session = getSession();
//		Transaction tx = session.beginTransaction();
//
//		Person person1 = new Person("aaa", "sdsas", "1421442", TypeDocument.PASSPORT);
//		session.save(person1);
//		Account account1 = new Account(person1, "21657262135256", "abc@abc.com", "abc");
//		ArrayList<Authorization> clientAUTH = new ArrayList<>();
//		clientAUTH.add(Authorization.ADD_DELETE_EDIT_RESERVATION);
//		account1.setAuthorizations(clientAUTH);
//
//		Person person2 = new Person("Admin", "AAA", "1421442", TypeDocument.PASSPORT);
//		session.save(person2);
//		Account account2 = new Account(person2, "21657262135256", "admin@admin.com", "admin");
//		ArrayList<Authorization> clientAUTH2 = new ArrayList<>();
//		clientAUTH2.add(Authorization.ADD_DELETE_EDIT_COMPANY);
//		account2.setAuthorizations(clientAUTH2);
//
//		session.save(account1);
//		session.save(account2);
//
//		Company company = new Company("WizzAir", "ABCBCB", "100/10", "London", "England", "12-511", "1234567890", "123456789");
//
//		session.save(company);
//		Plane plane1 = new Plane("BoeingX1", company, 50, 20, 10);
//		Plane plane2 = new Plane("BoeingXX", company, 40, 20, 10);
//		session.save(plane2);
//		session.save(plane1);
//		company.addPlane(plane1);
//		company.addPlane(plane2);
//
//		Airport airport1 = new Airport("Lotnisko Balice", 1, "Krakow", "KRK");
//		Airport airport2 = new Airport("Barcelona lotnisko", 0, "Barcelona", "BAR");
//
//		session.save(airport1);
//		session.save(airport2);
//
//		LocalDateTime date1 = LocalDateTime.of(2020,
//				Month.DECEMBER, 29, 19, 30, 0);
//		LocalDateTime date2 = LocalDateTime.of(2020,
//				Month.DECEMBER, 30, 1, 00, 0);
//		LocalDateTime date3 = LocalDateTime.of(2020,
//				Month.DECEMBER, 10, 19, 30, 0);
//		LocalDateTime date4 = LocalDateTime.of(2020,
//				Month.DECEMBER, 11, 19, 30, 0);
//		LocalDateTime date5 = LocalDateTime.of(2020,
//				Month.DECEMBER, 29, 20, 30, 0);
//		LocalDateTime date6 = LocalDateTime.of(2020,
//				Month.DECEMBER, 29, 22, 30, 0);
//
//		Price price = new Price(50, 150, 220);
//		Price price2 = new Price(40, 100, 220);
//		Price price3 = new Price(70, 200, 320);
//
//		session.save(price);
//		session.save(price2);
//		session.save(price3);
//		Flight flight = new Flight("FR12311", plane1, airport1, airport2, date1, date2, price);
//		Flight flight2 = new Flight("FR21321", plane2, airport2, airport1, date5, date6, price2);
//		Flight flight3 = new Flight("FR12333", plane1, airport2, airport1, date3, date4, price3);
//		session.save(flight);
//		session.save(flight2);
//		session.save(flight3);
//		tx.commit();
//		session.close();
//		System.out.println("KONIEC");

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Ponczusie App");


		this.appController = new AccountAppController(primaryStage, session);
		this.appController.initLoginLayout();
	}
	@Override
	public void stop(){
		//ourSessionFactory.close();
		Platform.exit();
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}


}
