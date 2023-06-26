package edu.upenn.cis573.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.Map;

public class DataManagerGetFundNameTest {

    @Test(expected = IllegalStateException.class)
    public void testNullWebClient()  {
        DataManager dm = new DataManager(null);
        dm.getFundName("123");
    }


    @Test
    public void testSuccess() {

        DataManager dm = new DataManager(new WebClient("10", 3000) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"Snoopy\"}";
            }
        });

        String name = dm.getFundName("12345");
        assertNotNull(name);
        assertEquals("Snoopy", name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullId() {
        DataManager dm = new DataManager(new WebClient("10", 3000));
        dm.getFundName(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsuccessfulStatus() {
        DataManager dm = new DataManager(new WebClient("10", 3000) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\",\"data\":\"Snoopy\"}";
            }
        });
        dm.getFundName("12345");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNullResponseFromServer() {
        DataManager dm = new DataManager(new WebClient("10", 3000) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        dm.getFundName("12345");
    }

    @Test(expected = IllegalStateException.class)
    public void testMalformedJson() {
        DataManager dm = new DataManager(new WebClient("10", 3000) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data1\":\"Snoopy\"}";
            }
        });
        dm.getFundName("12345");
    }

    @Test
    public void testCache() {
        DataManager dm = new DataManager(new WebClient("10", 3000) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"Snoopy\"}";
            }
        });

        String name = dm.getFundName("12345");
        assertNotNull(name);
        assertEquals("Snoopy", name);
        assertEquals("Snoopy", dm.getFundName("12345"));
    }
}
