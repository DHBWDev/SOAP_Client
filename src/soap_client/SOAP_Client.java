package soap_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;


public class SOAP_Client {

    public static void main(String[] args) throws IOException, DatatypeConfigurationException {
        
        
        
        Boolean again = true;

        while (again) {

            System.out.println("*********");
            System.out.println("Hauptmenü");
            System.out.println("*********");
            System.out.println();
            System.out.println("[K] Kunde anlegen");
            System.out.println("[F] Fahrzeug anlegen");
            System.out.println("[A] Fahrzeug ausleihen");
            System.out.println("[L] Leihverträge auflisten");
            System.out.println("[E] Ende");

            String menuinstr = readString("Deine Auswahl: ");

            switch (menuinstr) {
                case "K": {

                    kundeAnlegen();
                    break;

                }

                case "F": {

                    fahrzeugAnlegen();
                    break;

                }

                case "A": {

                    fahrzeugAusleihen();
                    break;

                }

                case "L": {

                    leihverträgeAuflisten();
                    break;

                }

                case "E": {

                    again = false;
                    break;

                }

                default: {

                    System.out.println("Hoppla Sie haben sich wahrscheinlich vertippt... Probieren Sie's nochmal...");
                    break;

                }

            }

        }

    }

    public static void kundeAnlegen() throws IOException {
        
        WebService_Service wsService = new WebService_Service();
        WebService ws = wsService.getWebServicePort();

        Customer newCustomer = new Customer();
        newCustomer.setFirstName(readString("Vorname: "));
        newCustomer.setName(readString("Nachname: "));
        newCustomer.setStreet(readString("Straße: "));
        newCustomer.setHouseNumber(readString("Hausnummer: "));
        newCustomer.setPostCode(readString("Postleitzahl: "));
        newCustomer.setPlace(readString("Ort: "));
        newCustomer.setCountry(readString("Land: "));
        
        Holder<Customer> customer = new Holder<Customer>(newCustomer);
        
        ws.saveNewCustomer(customer);
        
        System.out.println("Sie haben die Kundennummer: " + customer.value.getId());
        

    }

    public static void fahrzeugAnlegen() throws IOException {
        
        WebService_Service wsService = new WebService_Service();
        WebService ws = wsService.getWebServicePort();

        String hersteller = readString("Hersteller:");
        String modell = readString("Modell:");
        int baujahr = readInt("Baujahr:");
        
        Car newCar = new Car();
        newCar.setConstrutionYear(baujahr);
        newCar.setModel(modell);
        newCar.setProducer(hersteller);
        
        Holder<Car> car = new Holder<Car>(newCar);
        
        ws.saveNewCar(car);

    }

    public static void fahrzeugAusleihen() throws IOException, DatatypeConfigurationException{
        
        WebService_Service wsService = new WebService_Service();
        WebService ws = wsService.getWebServicePort();
        List<Car> cars = ws.findAllCars();
        
        System.out.println("Folgende Fahrzeuge stehen zur Verfügung:");
        
        for(Car car: cars){
            
            System.out.println(
                    car.getModel() + ", " + 
                    car.getProducer() + ", " +
                    car.getConstrutionYear() + ", " +
                    car.getId()
            );

        }
        

        long kundennummer = readLong("Kundennummer:");
        long carId = readLong("FahrzeugID:");
        String von = readString("Abholdatum (yyyy-mm-dd)");
        String bis = readString("Rückgabedatum (yyyy-mm-dd)");
        
        DatatypeFactory dtf = DatatypeFactory.newInstance();
        XMLGregorianCalendar startTimeFrom = dtf.newXMLGregorianCalendar(von);
        XMLGregorianCalendar endeTimeFrom = dtf.newXMLGregorianCalendar(bis);
        
        try {
            ws.saveNewContract(startTimeFrom, endeTimeFrom, kundennummer, carId);
        } catch (CarIsNotAvailableException_Exception ex) {
            
            System.out.println("Fahrzeug ist nicht verfügbar");
        }

    }

    public static void leihverträgeAuflisten() throws IOException {
        
        WebService_Service wsService = new WebService_Service();
        WebService ws = wsService.getWebServicePort();
        List<Contract> contracts = ws.findContractsByCustomerId(readLong("Kundennummer:"));
        
        System.out.println("Folgende Fahrzeuge stehen zur Verfügung:");
        
        
        
        

        System.out.println("Hier könnte Ihre Werbung stehen...");

    }

    public static String readString(String anzeige) throws IOException {

        System.out.print(anzeige);
        BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));
        String input = fromKeyboard.readLine();

        return input;
    }

    public static long readLong(String anzeige) throws IOException {

        Boolean again = true;
        long inint = 0;

        while (again) {
            again = false;
            System.out.print(anzeige);
            BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));
            String input;

            input = fromKeyboard.readLine();

            try {
                inint = Long.parseLong(input);
            } catch (NumberFormatException e) {
                again = true;
                System.out.println("Hoppla Sie haben sich wahrscheinlich vertippt... Probieren Sie's nochmal...");
            }
        }

        return inint;
    }
    
    public static int readInt(String anzeige) throws IOException {

        Boolean again = true;
        int inint = 0;

        while (again) {
            again = false;
            System.out.print(anzeige);
            BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));
            String input;

            input = fromKeyboard.readLine();

            try {
                inint = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                again = true;
                System.out.println("Hoppla Sie haben sich wahrscheinlich vertippt... Probieren Sie's nochmal...");
            }
        }

        return inint;
    }

}
