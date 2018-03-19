package soap_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SOAP_Client {

    public static void main(String[] args) throws IOException {

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

        String vorname = readString("Vorname:");
        String nachname = readString("Nachname:");
        String strasse = readString("Straße:");
        int hausnummer = readInt("Hausnummer:");
        int plz = readInt("Postleitzahl:");
        String ort = readString("Ort:");
        String land = readString("Land");

    }

    public static void fahrzeugAnlegen() throws IOException {

        String hersteller = readString("Hersteller:");
        String modell = readString("Modell:");
        int baujahr = readInt("Baujahr:");

    }

    public static void fahrzeugAusleihen() throws IOException {

        int kundennummer = readInt("Kundennummer:");
        String von = readString("Abholdatum (yyyy-mm-dd)");
        String bis = readString("Rückgabedatum (yyyy-mm-dd)");

    }

    public static void leihverträgeAuflisten() throws IOException {

        System.out.println("Hier könnte Ihre Werbung stehen...");

    }

    public static String readString(String anzeige) throws IOException {

        System.out.print(anzeige);
        BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));
        String input = fromKeyboard.readLine();

        return input;
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
