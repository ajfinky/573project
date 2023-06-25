import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class DataManager_updateAccount_Test {
	
	private DataManager dm;

	@Test(expected=IllegalStateException.class)
	public void testUpdateAccount_WebClientIsNull() {

		Organization org = new Organization("id", "name", "description");
		dm = new DataManager(null);
		dm.updateAccount(org, "name", "description");
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUpdateAccount_OrgIsNull() {

		dm = new DataManager(new WebClient("localhost", 3001));
		dm.updateAccount(null, "name", "description");
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUpdateAccount_OrgIdIsNull() {

		Organization org = new Organization(null, "name", "description");
		dm = new DataManager(new WebClient("localhost", 3001));
		dm.updateAccount(org, "name", "description");
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUpdateAccount_NameIsNull() {

		Organization org = new Organization("id", "name", "description");
		dm = new DataManager(new WebClient("localhost", 3001));
		dm.updateAccount(org, null, "description");
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUpdateAccount_DescriptionIsNull() {

		Organization org = new Organization("id", "name", "description");
		dm = new DataManager(new WebClient("localhost", 3001));
		dm.updateAccount(org, "name", null);
		
	} 
	
	@Test(expected=IllegalStateException.class)
	public void testUpdateAccount_StatusError() {

		Organization org = new Organization("id", "name", "description");
		dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"error\",\"error\":\"An unexpected database error occurred\"}";
			}
		});
		dm.updateAccount(org, "name2", "description2"); 
		
		
	} 
	
	@Test(expected=IllegalStateException.class)
	public void testUpdateAccount_OtherError() {

		Organization org = new Organization("id", "name", "description");
		dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "I AM NOT JSON!"; 
			}
		});
		dm.updateAccount(org, "name2", "description2");
		
	} 
	
	@Test
	public void testUpdateAccount_Sucessful() {

		Organization org = new Organization("id", "name", "description");
		dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"name\":\"new org\",\"description\":\"this is the new org\",\"funds\":[]}}";
			}
		});
	
		dm.updateAccount(org, "name2", "description2");
		
		assertEquals(org.getName(), "name2");
		assertEquals(org.getDescription(), "description2");
		
	}  
	

}
