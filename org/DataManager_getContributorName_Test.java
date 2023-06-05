import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class DataManager_getContributorName_Test {

	@Test
	public void testSuccessfulCreation() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":\"Grace\"}"; 
			}
		});
		
		String name = dm.getContributorName("12345");
		
		assertNotNull(name);
		assertEquals("Grace", name);
		
		// 1.2 POTENTIAL BUG?
		// Any ID will get the name Grace. 
		// Should makeRequest return data with both id and name
		// and then make sure argument id matches with JSON id before returning the name?
		name = dm.getContributorName("6789");
		assertNotNull(name);
		assertEquals("Grace", name);
	}
	
	@Test
	public void testUnuccessfulCreation() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"failure\",\"data\":\"Grace\"}"; 
			}
		});
		
		String name = dm.getContributorName("12345");
		
		assertNull(name);
	}
	
	@Test
	public void testException() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"statu\":\"success\",\"data\":\"Grace\"}"; 
			}
		});
		
		String name = dm.getContributorName("12345");
		
		assertNull(name);
	}
}
