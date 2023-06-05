package edu.upenn.cis573.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataManagerAttemptLoginTest {

    @Test
    public void testNullLogin() {
        DataManager dm = new DataManager(new WebClient(null, 0));
        assertNull(dm.attemptLogin(null, "12345"));
    }

    @Test
    public void testNullPassword() {
        DataManager dm = new DataManager(new WebClient(null, 0));
        assertNull(dm.attemptLogin("12345", null));
    }

    @Test
    public void testUnsuccessfulStatus() {
        DataManager dm = new DataManager(new WebClient(null, 0) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                String id = "123";
                String name = "Snoopy";

                return "{\"status\": \"unsuccessful\", " +
                        "\"data\": {\"_id\": \"" + id + "\", \"name\": \"" + name + "\"}}";
            }
        });
        assertNull(dm.attemptLogin("1234", "1234"));
    }

    @Test
    public void testWrongLoginPassword() {
        DataManager dm = new DataManager(new WebClient(null, 0) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        assertNull(dm.attemptLogin("1234", "1234"));
    }

    @Test
    public void testSuccessfulLogin() {
        String id = "123";
        String name = "Snoopy";
        String email = "snoopy@email.com";
        String creditCardNumber = "1234";
        String creditCardCVV = "123";
        int creditCardExpiryMonth = 1;
        int creditCardExpiryYear = 12;
        String creditCardPostCode = "19104";
        String donations = "[{\"fund\": \"1\", \"date\": \"01/02/12\", \"amount\": 10}," +
                "{\"fund\": \"2\", \"date\": \"02/03/12\", \"amount\": 20}," +
                "{\"fund\": \"3\", \"date\": \"03/04/12\", \"amount\": 15}]";

        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\": \"success\", " +
                        "\"data\": {\"_id\": \"" + id + "\", " +
                        "\"name\": \"" + name + "\", " +
                        "\"email\": \"" + email + "\", " +
                        "\"creditCardNumber\": \"" + creditCardNumber + "\", " +
                        "\"creditCardCVV\": \"" + creditCardCVV + "\", " +
                        "\"creditCardExpiryMonth\": " + creditCardExpiryMonth + ", " +
                        "\"creditCardExpiryYear\": " + creditCardExpiryYear + ", " +
                        "\"creditCardPostCode\": \"" + creditCardPostCode + "\", " +
                        "\"donations\": " + donations + "}}";
            }
        }) {
            @Override
            public String getFundName(String id) {
                switch (id) {
                    case "1":
                        return "WHO";
                    case "2":
                        return "UNICEF";
                    case "3":
                        return "RED CROSS";
                    default:
                        return null;
                }
            }
        };

        Contributor contributor = new Contributor(id, name, email,
                creditCardNumber, creditCardCVV,
                Integer.toString(creditCardExpiryMonth), Integer.toString(creditCardExpiryYear), creditCardPostCode);

        List<Donation> donationList = new LinkedList<>();
        donationList.add(new Donation("WHO", "Snoopy", 10, "01/02/12"));
        donationList.add(new Donation("UNICEF", "Snoopy", 20, "02/03/12"));
        donationList.add(new Donation("RED CROSS", "Snoopy", 15, "03/04/12"));

        contributor.setDonations(donationList);
        Contributor actualContributor = dm.attemptLogin("123", "123");

        assertNotNull(actualContributor);
        assertEquals(contributor, actualContributor);
    }
}
