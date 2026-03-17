package testerJUNIT;

import com.example.decathlon.common.CalcTrackAndField;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KimTestar {
    @Test
    void testing(){
        CalcTrackAndField calc = new CalcTrackAndField();

        double a = 25.4347, b = 18, c = 1.81, time = 15.00;
        int result = calc.calculateTrack(a, b, c, time);

        assertEquals(185, result);
        System.out.println("Testet gick igenom för Kim!");
    }
}
