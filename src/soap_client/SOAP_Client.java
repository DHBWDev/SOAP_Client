package soap_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

public class SOAP_Client {

    public static void main(String[] args) throws IOException, DatatypeConfigurationException {

        Boolean again = true;

        while (again) {
            System.out.println();
            System.out.println("*********");
            System.out.println("Hauptmenü");
            System.out.println("*********");
            System.out.println();
            System.out.println("[K] Kunde anlegen");
            System.out.println("[F] Fahrzeug anlegen");
            System.out.println("[A] Fahrzeug ausleihen");
            System.out.println("[L] Leihverträge auflisten");
            System.out.println("[E] Ende");
            System.out.println();
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
        System.out.println();
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
        System.out.println();
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

    public static void fahrzeugAusleihen() throws IOException, DatatypeConfigurationException {

        WebService_Service wsService = new WebService_Service();
        WebService ws = wsService.getWebServicePort();
        List<Car> cars = ws.findAllCars();
        System.out.println();
        System.out.println("Folgende Fahrzeuge stehen zur Verfügung:");

        for (Car car : cars) {

            System.out.println(
                    car.getModel() + ", "
                    + car.getProducer() + ", "
                    + car.getConstrutionYear() + ", "
                    + car.getId()
            );

        }
        System.out.println();
        long kundennummer = readLong("Kundennummer:");
        long carId = readLong("FahrzeugID:");

        DatatypeFactory dtf = DatatypeFactory.newInstance();

        boolean correct = true;
        XMLGregorianCalendar startTimeFrom = null, endeTimeFrom = null;

        while (correct) {

            correct = false;

            try {

                String von = readString("Abholdatum (yyyy-mm-dd)");
                startTimeFrom = dtf.newXMLGregorianCalendar(von + "T00:00:00");

            } catch (IllegalArgumentException ex) {

                System.out.println("Falsches Datumsformat! Bitte probieren Sie es nochmal.");
                correct = true;

            }

        }

        correct = true;

        while (correct) {

            correct = false;

            try {

                String bis = readString("Rückgabedatum (yyyy-mm-dd)");
                endeTimeFrom = dtf.newXMLGregorianCalendar(bis + "T23:59:59");

            } catch (IllegalArgumentException ex) {

                System.out.println("Falsches Datumsformat! Bitte probieren Sie es nochmal.");
                correct = true;

            }

        }

        try {
            Contract contract = ws.saveNewContract(startTimeFrom, endeTimeFrom, kundennummer, carId);
            System.out.println();
            System.out.println("Alles klar! Leihvertrag mit der ID " + contract.getId() + " wurde angelegt.");
            System.out.println();

        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }

    }

    public static void leihverträgeAuflisten() throws IOException {

        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
        WebService_Service wsService = new WebService_Service();
        WebService ws = wsService.getWebServicePort();
        System.out.println();

        try {
            List<Contract> contracts = ws.findContractsByCustomerId(readLong("Kundennummer:"));
            System.out.println("Folgende Verträge wurden abgeschlossen:");

            for (Contract contract : contracts) {

                System.out.println(
                        "Nummer "
                        + contract.getId() + ": "
                        + contract.getCar().getProducer() + " "
                        + contract.getCar().getModel() + " von "
                        + fmt.format(contract.getStartDate().toGregorianCalendar().getTime()) + " bis "
                        + fmt.format(contract.getDueDate().toGregorianCalendar().getTime())
                );

            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

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
                System.out.println();
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
                System.out.println();
                System.out.println("Hoppla Sie haben sich wahrscheinlich vertippt... Probieren Sie's nochmal...");
            }
        }

        return inint;
    }

}
