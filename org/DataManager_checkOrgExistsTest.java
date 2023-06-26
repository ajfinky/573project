import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
public class DataManager_checkOrgExistsTest {
	
    @Test(expected = IllegalStateException.class)
    public void testNullWebClient() {
        // Arrange
        String login = "thelogin";
        DataManager dm = new DataManager(null);
    	dm.checkOrgExists(login);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullArguments() {
        // Arrange
        String login = null;
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
    	dm.checkOrgExists(login);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testWebClientReturnsError() {
    	
        // Arrange
        String login = "newestlogin";
        
    	
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"error\"}";
			}
			
		});
    	dm.checkOrgExists(login);
    }
    
    @Test
    public void testLoginFailed() {
    	
        // Arrange
        String login = "newestlogin";
        
    	
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"login failed\"}";
			}
			
		});
    	assertFalse(dm.checkOrgExists(login));
    }
    
    @Test
    public void testLoginSuccess() {
    	
        // Arrange
        String login = "newestlogin";
        
    	
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\"}";
			}
			
		});
    	assertTrue(dm.checkOrgExists(login));
    }


}
