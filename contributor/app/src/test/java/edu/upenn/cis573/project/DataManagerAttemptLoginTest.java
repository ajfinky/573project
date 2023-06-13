package edu.upenn.cis573.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataManagerAttemptLoginTest {

    @Test(expected = IllegalStateException.class)
    public void testNullWebClient() {
        DataManager dm = new DataManager(null);
        dm.attemptLogin("123", "123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLogin() {
        DataManager dm = new DataManager(new WebClient("10", 3000));
        dm.attemptLogin(null, "12345");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPassword() {
        DataManager dm = new DataManager(new WebClient("10", 3000));
        dm.attemptLogin("12345", null);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsuccessfulStatus() {
        DataManager dm = new DataManager(new WebClient("10", 3000) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                String id = "123";
                String name = "Snoopy";

                return "{\"status\": \"error\", " +
                        "\"data\": {\"_id\": \"" + id + "\", \"name\": \"" + name + "\"}}";
            }
        });
        dm.attemptLogin("1234", "1234");
    }

    @Test(expected = IllegalStateException.class)
    public void testWrongLoginPassword() {
        DataManager dm = new DataManager(new WebClient("10", 3000) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        dm.attemptLogin("1234", "1234");
    }

    @Test(expected = IllegalStateException.class)
    public void testMalformedJson() {
        DataManager dm = new DataManager(new WebClient("10", 3000) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                String id = "123";
                String name = "Snoopy";

                return "{\"status\": \"success\", " +
                        "\"data\": {\"_id\": \"" + id + "\", \"name\": \"" + name + "\"}}";
            }
        });
        dm.attemptLogin("1234", "1234");
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

        DataManager dm = new DataManager(new WebClient("10", 3000) {
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
