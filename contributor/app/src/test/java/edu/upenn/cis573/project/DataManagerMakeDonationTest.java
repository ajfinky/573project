package edu.upenn.cis573.project;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Map;

public class DataManagerMakeDonationTest {

    @Test(expected = IllegalStateException.class)
    public void testNullWebClient() {
        DataManager dm = new DataManager(null);
        dm.makeDonation("123", "123", "123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullContributorId() {
        DataManager dm = new DataManager(new WebClient("10", 3000));
        dm.makeDonation(null, "123", "10");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFundId() {
        DataManager dm = new DataManager(new WebClient("10", 3000));
        dm.makeDonation("123", null, "10");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAmount() {
        DataManager dm = new DataManager(new WebClient("10", 3000));
        dm.makeDonation("123", "123", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonNumericAmount() {
        DataManager dm = new DataManager(new WebClient("10", 3000));
        dm.makeDonation("123", "123", "test");
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsuccessfulStatus() {
        DataManager dm = new DataManager(new WebClient("10", 3000) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"unsuccessful\",\"data\":\"Snoopy\"}";
            }
        });
        dm.makeDonation("123", "123", "123");
    }

    @Test
    public void testSuccessfulStatus() {
        DataManager dm = new DataManager(new WebClient("10", 3000) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"Snoopy\"}";
            }
        });
        assertTrue(dm.makeDonation("123", "123", "123"));
    }

    @Test(expected = IllegalStateException.class)
    public void testMalformedJson() {
        DataManager dm = new DataManager(new WebClient("10", 3000) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status1\":\"success\",\"data1\":\"Snoopy\"}";
            }
        });
        dm.makeDonation("12345", "123", "123");
    }
}