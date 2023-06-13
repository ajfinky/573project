import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class DataManager_attemptLogin_Test {

	@Test
	public void testReturnNull() {
		
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"failure\"}"; 
			}
		});
		
		Organization org = dm.attemptLogin("12345", "password");
		assertNull(org);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalLogin() {
		
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\"}"; 
			}
		});
		
		dm.attemptLogin(null, "password");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalPassword() {
		
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\"}"; 
			}
		});
		
		dm.attemptLogin("12345", null);
	} 
	
	@Test
	public void testValidLoginNoFunds() {
		
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"name\":\"new org\",\"description\":\"this is the new org\",\"funds\":[]}}";
			}
		});
		
		Organization org = dm.attemptLogin("12345", "password");
		assertNotNull(org);
		assertEquals("12345", org.getId());
		assertEquals("new org", org.getName());
		assertEquals("this is the new org", org.getDescription());
		
		
	}
	
	@Test
	public void testValidLoginWithFunds() {
		
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"name\":\"new org\",\"description\":\"this is the new org\",\"org\":\"5678\",\"funds\":[{\"_id\":\"11111\",\"name\":\"new fund\",\"description\":\"this is the new fund\",\"target\":10000, \"donations\":[]}]}}";
			}
		});
		
		Organization org = dm.attemptLogin("12345", "password");
		assertNotNull(org);
		assertEquals("12345", org.getId());
		assertEquals("new org", org.getName());
		assertEquals("this is the new org", org.getDescription());
		assertEquals(1, org.getFunds().size());
		
		
	}
	
	@Test
	public void testValidLoginWithFundsAndDonations() {
		
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"name\":\"new org\",\"description\":\"this is the new org\",\"org\":\"5678\",\"funds\":[{\"_id\":\"11111\",\"name\":\"new fund\",\"description\":\"this is the new fund\",\"target\":10000, \"donations\":[{\"contributor\":\"12345\",\"amount\":100,\"date\":\"2021-06-18T04:21:04.807Z\"}]}]}}";
			}
		});
		
		Organization org = dm.attemptLogin("12345", "password");
		assertNotNull(org);
		assertEquals("12345", org.getId());
		assertEquals("new org", org.getName());
		assertEquals("this is the new org", org.getDescription());
		assertEquals(1, org.getFunds().size());
		assertEquals(1, org.getFunds().get(0).getDonations().size());
		
		
	}

}
