package testerJUNIT;
import com.example.decathlon.common.CalcTrackAndField;
import com.example.decathlon.common.InputName;
import org.junit.jupiter.api.Test; // JUnit 5 (Jupiter)

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;


class TinaTestar {
    private InputName inputName;
    private String[] testNames = {
            "Anna", "Bertil", "Cecilia", "David", "Erik", "Filippa", "Gustav", "Helena", "Ivar", "Johanna",
            "Karl", "Linn", "Magnus", "Nora", "Oskar", "Petra", "Quinn", "Rasmus", "Sara", "Tomas",
            "Ulrika", "Viktor", "Wendy", "Xander", "Ylva", "Zelda", "Alice", "Bengt", "Carin", "Daniel",
            "Elin", "Fredrik", "Gunilla", "Hans", "Ingrid", "Jakob", "Karin", "Lars", "Maria", "Nils",
            "Olivia"
    };

    @Test
    void testing() {
        CalcTrackAndField calc = new CalcTrackAndField();
        //A, B och C tas från tabellen, distance och time är deltagarens prestation
        //tex: deltagaren gör 100m sprint på 15 sekunder -> time = 15
        //sedan mixas resultattiden in med lite siffror baserade på statistik
        //och vi får ett resultat, här 185 poäng

        double a = 25.4347, b = 18, c = 1.81, time = 15.00;
        int result = calc.calculateTrack(a, b, c, time);

        assertEquals(185, result);
        System.out.println("Testet gick igenom för Tina!");
    }

    //Krav 5.2 WEBB UI ska fungera med upp till 40 deltagare, annars ska ett
    //felmeddelande visas
    @Test
    void adding41Competitors(){

        inputName = new InputName();
        String result = "";

        for (int i = 0; i < testNames.length; i++){

            String singleInput = testNames[i] + "\n";

            //Säg åt scan att läsa in min text ist för manuell inmatning
            System.setIn(new ByteArrayInputStream(singleInput.getBytes()));

            result = inputName.addCompetitor();
            System.out.println(testNames[i]);
        }

        assertEquals("Olivia", result, "Result isn't matching the input");
        System.out.println("Testet lyckades! Inmatat: " + testNames[40] + ", Returnerat: " + result);
        System.setIn(System.in);
    }
}
