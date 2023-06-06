import static org.junit.Assert.*;
import org.junit.Test;

public class Donation_getDate_Test {

    @Test
    public void testRandomDate() {
        Donation donation = new Donation("001", "TestCompany", 100, "2021-06-18T04:21:04.807Z");
        String result = donation.getDate();
        String expected = "June 18, 2021";
        assertEquals(expected, result);
    }

}