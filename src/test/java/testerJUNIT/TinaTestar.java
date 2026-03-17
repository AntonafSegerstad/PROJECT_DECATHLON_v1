package testerJUNIT;
import com.example.decathlon.common.CalcTrackAndField;
import org.junit.jupiter.api.Test; // JUnit 5 (Jupiter)
import static org.junit.jupiter.api.Assertions.*;


class TinaTestar {

    @Test
    void testing(){
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
}
