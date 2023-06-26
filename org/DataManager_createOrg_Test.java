import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class DataManager_createOrg_Test {

    
    @Test
    public void testSuccessfulOrgCreation() {    	
        // Arrange
        String login = "mylogin";
        String password = "mypassword";
        String name = "My Organization";
        String description = "Organization description";
        
        
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				
				if (resource == "/findOrgByLogin") {
					return "{\"status\":\"login failed\"}";
				}
				return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"login\":\"mylogin\",\"password\":\"mypassword\",\"name\":\"My Organization\",\"description\":\"Organization description\",\"funds\":[],\"__v\":0}}";
			}
		});
		
		boolean pass = dm.createOrg(login, password, name, description);
		
        // Assert
		assertTrue(pass);
        
        
    }
    
    @Test(expected = IllegalStateException.class)
    public void testNullWebClient() {
        // Arrange
        String login = "thelogin";
        String password = "mypassword";
        String name = "My Organization";
        String description = "Organization description";
        DataManager dm = new DataManager(null);
    	dm.createOrg(login, password, name, description);
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullArguments() {
        // Arrange
        String login = null;
        String password = "mypassword";
        String name = "My Organization";
        String description = "Organization description";
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
    	dm.createOrg(login, password, name, description);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOrg_LoginAlreadyUsed() {
    	
        // Arrange
        String login = "mylogin";
        String password = "mypassword";
        String name = "My Organization";
        String description = "Organization description";
        
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\"}";
			}
			
		});

        
		dm.createOrg(login, password, name, description);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCreateOrg_WebClientReturnsError() {
    	
        // Arrange
        String login = "newestlogin";
        String password = "mypassword";
        String name = "My Organization";
        String description = "Organization description";
        
    	
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"error\"}";
			}
			
		});
		dm.createOrg(login, password, name, description);
    }
    
    @Test
    public void testFalseReturn() {
        // Arrange
        String login = "newestestlogin";
        String password = "mypassword";
        String name = "My Organization";
        String description = "Organization description";
        
    	
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				
				if (resource == "/findOrgByLogin") {
					return "{\"status\":\"login failed\"}";
				}
				return "{\"status\":\"wrong\"}";
			}
			
		});
		
		assertFalse(dm.createOrg(login, password, name, description));
    }
}
    
    // Add more test cases to cover other scenarios
    

