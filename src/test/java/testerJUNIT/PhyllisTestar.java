package testerJUNIT;

import com.example.decathlon.common.CalcTrackAndField;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class PhyllisTestar {

    @Test
    void testPoangLopning() {
        //1.Förberedelser
        CalcTrackAndField calcTrackAndField = new CalcTrackAndField();

        //2. Värden för 100 meter (A, B, C och tiden 10.395 sek)
        double a = 25.4347;
        double b = 18.0;
        double c = 1.81;
        double tid = 10.395;

        //3. Utför beräkningen
        int resultat = calcTrackAndField.calculateTrack(a, b, c, tid);

        //4. Kontrollera att det blev 1000poäng
        assertEquals(1000, resultat);
    }

    @Test
    void testPerfektLopning(){
        CalcTrackAndField calc = new CalcTrackAndField();

        // 10.395 sek ska ge exakt 1000 poäng
        int result = calc.calculateTrack(25.4347, 18.0, 1.81, 10.395);

        assertEquals(1000, result);
        System.out.println("1000-poängs testet gick igenom!🦄 ");
    }

    @Test
    void testLangdhopp(){
        CalcTrackAndField calc = new CalcTrackAndField();

        // Parametrar för Längdhopp (Decathlon)
        // Ett hopp på 776 cm ska ge exakt 1000 poäng
        double a = 0.14354;
        double b = 220.0;
        double c = 1.4;
        double distans = 776.0;

        int result = calc.calculateField(a, b, c, distans);

        assertEquals(1000, result);
        System.out.println("Längdhoppstestet gick också igenom! 🤩👌🏾");

    }
}
