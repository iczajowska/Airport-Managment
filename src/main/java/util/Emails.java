package util;

import javafx.util.converter.LocalDateTimeStringConverter;
import model.Flight;
import service.Services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Emails implements Runnable{

    private final Services services;
    private final SendEmail sendEmail;

    public Emails(Services services){
        this.services = services;
        this.sendEmail = new SendEmail();
    }

    public void run(){
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTimeStringConverter converter = new LocalDateTimeStringConverter(formatter, formatter);

        ArrayList<Flight> flights = (ArrayList<Flight>) services.getFlightService().findAll().stream()
                .filter(f -> f.getDepartureTime().getDayOfYear() == (1+LocalDateTime.now().getDayOfYear())).collect(Collectors.toList());
        for(Flight f : flights){
            ArrayList<String> emailsList = (ArrayList<String>) f.getReservations().stream()
                    .map(r -> r.getAccount().getEmail()).collect(Collectors.toList());
            String emailListString = emailsList.toString().replace("[","").replace("]","");
            String messageText = String.format("Upcoming flight: %s\nDeparture: %s, %s\nArrival: %s, %s",
                    f.getFlightNumber() ,converter.toString(f.getDepartureTime()),f.getDepartureAirport().getAirport_code(),
                    converter.toString(f.getArrivalTime()), f.getArrivalAirport().getAirport_code());

            if(emailListString != ""){
                this.sendEmail.sendEmail(emailListString,messageText);
            }

        }
        System.out.println("Daily mails have been send!");

        try {
            TimeUnit.DAYS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
