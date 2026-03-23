package testerJUNIT;

import com.example.decathlon.common.CalcTrackAndField;
import com.example.decathlon.common.InputName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TinaTestar {

    //tester av common-klasserna

    @Test
    void testingCalculateTrack(){
        CalcTrackAndField calc = new CalcTrackAndField();
        //A, B och C tas från tabellen, distance och time är deltagarens prestation
        //tex: deltagaren gör 100m sprint på 15 sekunder -> time = 15
        //sedan mixas resultattiden in med lite siffror baserade på statistik
        //och vi får ett resultat, här 185 poäng
        //100M Sprint Decathlon
        double a = 25.4347, b = 18, c = 1.81, time = 15.00;
        int result = calc.calculateTrack(a, b, c, time);

        assertEquals(185, result);
        System.out.println("Testet gick igenom för Tina!");
    }

    @Test
    void testingCalculateField(){
        //Discus Throw Decathlon
        CalcTrackAndField calc = new CalcTrackAndField();
        double a = 12.91, b = 4, c = 1.1, distance = 70;
        int result = calc.calculateField(a, b, c, distance);
        assertEquals(1295, result);
    }

    @Test
    void testingAddCompetitor(){
        //Körs förevigt då while-loopen aldrig får en regex-matchning
        InputName inputName = new InputName();
        String compName = "Bob Dylan";
        inputName.addCompetitor();

        assertEquals("Bob Dylan", compName);
    }

    
}
