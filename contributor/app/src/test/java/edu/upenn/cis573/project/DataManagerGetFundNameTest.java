package edu.upenn.cis573.project;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataManagerGetFundNameTest {

    @Test
    public void testSuccess() {

        DataManager dm = new DataManager(new WebClient(null, 0) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"Snoopy\"}";
            }
        });

        String name = dm.getFundName("12345");
        assertNotNull(name);
        assertEquals("Snoopy", name);
    }

    @Test
    public void testNullId() {
        DataManager dm = new DataManager(new WebClient(null, 0));
        assertNull(dm.getFundName(null));
    }

    @Test
    public void testUnsuccessfulStatus() {
        DataManager dm = new DataManager(new WebClient(null, 0) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"unsuccessful\",\"data\":\"Snoopy\"}";
            }
        });
        String name = dm.getFundName("12345");
        assertNotNull(name);
        assertEquals("Unknown Fund", name);
    }

    @Test
    public void testGetNullResponseFromServer() {
        DataManager dm = new DataManager(new WebClient(null, 0) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        assertNull(dm.getFundName(null));
    }

    @Test
    public void testWrongJsonFormat() {
        DataManager dm = new DataManager(new WebClient(null, 0) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"data\":\"Snoopy\"}";
            }
        });
        assertNull(dm.getFundName(null));
    }
}
