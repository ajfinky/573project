import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;

public class DataManager_makeDonation_Test {

    @Test
    public void testSuccessfulDonation() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"Bill Joe\"}";
            }
        });
        Donation d = dm.makeDonation("123", "001", "100");
        assertEquals("Bill Joe", d.getContributorName());
        assertEquals("001", d.getFundId());
        assertEquals(100, d.getAmount());
        assertEquals("June 26, 2023", d.getDate());
    }

    @Test(expected = IllegalStateException.class)
    public void testClientIsNull() {
        DataManager dm = new DataManager(null);
        dm.makeDonation("123", "001", "100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContributorIdIsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"Bill Joe\"}";
            }
        });
        dm.makeDonation(null, "001", "100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFundIdIsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"Bill Joe\"}";
            }
        });
        dm.makeDonation("123", null, "100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAmountIsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"Bill Joe\"}";
            }
        });
        dm.makeDonation("123", "001", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContributorIdIsEmpty() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"Bill Joe\"}";
            }
        });
        dm.makeDonation("", "001", "100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFundIdIsEmpty() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"Bill Joe\"}";
            }
        });
        dm.makeDonation("123", "", "100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAmountIsEmpty() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"Bill Joe\"}";
            }
        });
        dm.makeDonation("123", "001", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidContributorId() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"failure\",\"data\":\"Bill Joe\"}";
            }
        });
        dm.makeDonation("123", "001", "100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAmount() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"Bill Joe\"}";
            }
        });
        dm.makeDonation("123", "001", "abc");
    }

    @Test(expected = IllegalStateException.class)
    public void testDonationFailed() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource.equals("/makeDonation")) return "{\"status\":\"failure\",\"data\":\"Bill Joe\"}";
                else return "{\"status\":\"success\",\"data\":\"Bill Joe\"}";
            }
        });
        dm.makeDonation("123", "001", "100");
    }
}
