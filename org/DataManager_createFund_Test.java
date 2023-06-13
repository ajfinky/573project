import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Map;

import org.junit.Test;

public class DataManager_createFund_Test {
	
	/*
	 * This is a test class for the DataManager.createFund method.
	 * Add more tests here for this method as needed.
	 * 
	 * When writing tests for other methods, be sure to put them into separate
	 * JUnit test classes.
	 */

	@Test
	public void testSuccessfulCreation() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"name\":\"new fund\",\"description\":\"this is the new fund\",\"target\":10000,\"org\":\"5678\",\"donations\":[],\"__v\":0}}";
			}
		});
		
		Fund f = dm.createFund("12345", "new fund", "this is the new fund", 10000);
		
		assertNotNull(f);
		assertEquals("this is the new fund", f.getDescription());
		assertEquals("12345", f.getId());
		assertEquals("new fund", f.getName());
		assertEquals(10000, f.getTarget());
	}
	
	@Test
	public void testUnsuccessfulCreation() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"failure\",\"data\":{\"_id\":\"12345\",\"name\":\"new fund\",\"description\":\"this is the new fund\",\"target\":10000,\"org\":\"5678\",\"donations\":[],\"__v\":0}}";
			}
		});
		
		Fund f = dm.createFund("12345", "new fund", "this is the new fund", 10000);
		
		assertNull(f);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testException() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"statu\":\"failure\",\"data\":{\"_id\":\"12345\",\"name\":\"new fund\",\"description\":\"this is the new fund\",\"target\":10000,\"org\":\"5678\",\"donations\":[],\"__v\":0}}";
			}
		});
		
		dm.createFund("12345", "new fund", "this is the new fund", 10000);
		
	}
	
	// 1.2 TESTING POTENTIAL BUGS 
	// Relies on ID in JSON, and name, description, and target in Arguments
	// Should we make sure it all matches up? 
	@Test
	public void testMismatchArguments() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"name\":\"new fund\",\"description\":\"this is the new fund\",\"target\":10000,\"org\":\"5678\",\"donations\":[],\"__v\":0}}";
			}
		});
		
		Fund f = dm.createFund("11111", "arg new fund", "arg this is the new fund", 10001);
		
		assertNotNull(f);
		
		// Relies on JSON for ID
		assertEquals("12345", f.getId());
		assertNotEquals("11111", f.getId());
		
		// Relies on arguments for description, name, target
		assertEquals("arg this is the new fund", f.getDescription());
		assertEquals("arg new fund", f.getName());
		assertEquals(10001, f.getTarget());
	}
	
}

