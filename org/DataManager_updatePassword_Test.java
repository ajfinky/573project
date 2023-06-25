import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class DataManager_updatePassword_Test {

	private DataManager dm;

	@Test(expected=IllegalStateException.class)
	public void testUpdatePassword_WebClientIsNull() {

		dm = new DataManager(null);
		dm.updatePassword("orgId", "password");
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUpdatePassword_OrgIdIsNull() {

		dm = new DataManager(new WebClient("localhost", 3001));
		dm.updatePassword(null, "password");
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUpdatePassword_PasswordIsNull() {

		dm = new DataManager(new WebClient("localhost", 3001));
		dm.updatePassword("orgId", null);
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void testUpdatePassword_StatusError() {

		dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"error\",\"error\":\"An unexpected database error occurred\"}";
			}
		});
		dm.updatePassword("orgId", "password");
		
	} 
	
	@Test(expected=IllegalStateException.class)
	public void testUpdatePassword_OtherError() {

		dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return  "I AM NOT JSON!";
			}
		});
		dm.updatePassword("orgId", "password");
		
	} 
	
	@Test
	public void testUpdatePassword_Sucessful() {

		dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"password\":\"password\",\"data\":{\"_id\":\"orgId\",\"name\":\"new org\",\"description\":\"this is the new org\",\"funds\":[]}}";
			}
		});
	
		String newPassword = dm.updatePassword("orgId", "password2");
		
		assertEquals(newPassword, "password2");
		
	}

}
