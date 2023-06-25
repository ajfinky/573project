
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataManager { 

	private final WebClient client;
	
	private final Map<String, String> names = new HashMap<>();

	public DataManager(WebClient client) {
		this.client = client; 
	}

	/**
	 * Attempt to log the user into an Organization account using the login and password.
	 * This method uses the /findOrgByLoginAndPassword endpoint in the API
	 * @return an Organization object if successful; null if unsuccessful
	 */
	public Organization attemptLogin(String login, String password) {
		
		if (login == null || password == null) {
			throw new IllegalArgumentException("Argument is null.");
		}

		try {
			Map<String, Object> map = new HashMap<>();
			map.put("login", login);
			map.put("password", password);
			String response = client.makeRequest("/findOrgByLoginAndPassword", map);
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String)json.get("status");
			
			if (status.equals("error")) {
				throw new IllegalStateException("Web client returns error");
			}


			if (status.equals("success")) {
				JSONObject data = (JSONObject)json.get("data");
				String fundId = (String)data.get("_id");
				String name = (String)data.get("name");
				String description = (String)data.get("description"); 
				Organization org = new Organization(fundId, name, description);  

				JSONArray funds = (JSONArray)data.get("funds");
				Iterator it = funds.iterator();
				while(it.hasNext()){
					JSONObject fund = (JSONObject) it.next(); 
					fundId = (String)fund.get("_id");
					name = (String)fund.get("name");
					description = (String)fund.get("description");
					long target = (Long)fund.get("target");

					Fund newFund = new Fund(fundId, name, description, target);

					JSONArray donations = (JSONArray)fund.get("donations");
					List<Donation> donationList = new LinkedList<>();
					Iterator it2 = donations.iterator();
					while(it2.hasNext()){
						JSONObject donation = (JSONObject) it2.next();
						String contributorId = (String)donation.get("contributor");
						String contributorName = getContributorName(contributorId);
						long amount = (Long)donation.get("amount");
						String date = (String)donation.get("date");
						donationList.add(new Donation(fundId, contributorName, amount, date)); 
					}

					newFund.setDonations(donationList);

					org.addFund(newFund);

				}

				return org;
			}
			else return null;
		}
		catch (Exception e) {
			//e.printStackTrace();
			throw new IllegalStateException("Error during login attempt.");
		}
	}

	/**
	 * Look up the name of the contributor with the specified ID.
	 * This method uses the /findContributorNameById endpoint in the API.
	 * @return the name of the contributor on success; null if no contributor is found
	 */
	public String getContributorName(String id) {
		
		if (this.client == null) {
			throw new IllegalStateException("WebClient is null.");
		}
		
		if (id == null) {
			throw new IllegalArgumentException("Illegal arguments passed.");
		}

		if (names.containsKey(id)) {
			return names.get(id);
		}

		try {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			String response = client.makeRequest("/findContributorNameById", map);

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String)json.get("status");

			if (status.equals("success")) {
				String name = (String)json.get("name");
				names.put(id, name);
				return name;
			}
			
			if (status.equals("error")) {
				throw new IllegalStateException("Web client returns error");
			}
			
			else return null;


		}
		catch (Exception e) {
				throw new IllegalStateException("Web client returns error"); 
		}	
	}

	/**
	 * This method creates a new fund in the database using the /createFund endpoint in the API
	 * @return a new Fund object if successful; null if unsuccessful
	 */
	public Fund createFund(String orgId, String name, String description, long target) {
		
		if (this.client == null) {
			throw new IllegalStateException("WebClient is null.");
		}
		
		if (orgId == null || name == null || description == null) {
			throw new IllegalArgumentException("Illegal arguments passed.");
		}

		try {

			Map<String, Object> map = new HashMap<>();
			map.put("orgId", orgId);
			map.put("name", name);
			map.put("description", description);
			map.put("target", target);
			String response = client.makeRequest("/createFund", map);

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String)json.get("status");

			if (status.equals("success")) {
				JSONObject fund = (JSONObject)json.get("data");
				String fundId = (String)fund.get("_id");
				return new Fund(fundId, name, description, target);
			}
			
			if (status.equals("error")) {
				throw new IllegalStateException("Web client returns error"); 
			}
			
			else return null;

		}
		catch (Exception e) {
			//e.printStackTrace(); 
			throw new IllegalStateException("Web client returns error"); 
		}	
	}

	public Organization updateAccount(Organization org, String name, String description) {
		
		if (this.client == null) {
			throw new IllegalStateException("WebClient is null.");
		}
		
		if (org == null || name == null || description == null || org.getId() == null) {
			throw new IllegalArgumentException("Illegal arguments passed.");
		}
		
		
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("orgId", org.getId());
			map.put("name", name);
			map.put("description", description);
			String response = client.makeRequest("/updateOrg", map);
			//System.out.println(response);
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String)json.get("status");
			
			if (status.equals("error")) {
				throw new IllegalStateException("Web client returns error");
			} else {
				org.setName(name);
				org.setDescription(description);
			}
			
		} catch (Exception e) {
			throw new IllegalStateException("Web client returns error"); 
		}	
		
		return org; 
		
	}
	
	public String updatePassword(String orgId, String password) {
		
		if (this.client == null) {
			throw new IllegalStateException("WebClient is null.");
		}
		
		if (password == null || orgId == null) {
			throw new IllegalArgumentException("Illegal arguments passed.");
		}
		
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("orgId", orgId);
			map.put("password", password);
			String response = client.makeRequest("/updateOrgPassword", map);
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String)json.get("status");
			
			if (status.equals("error")) {
				throw new IllegalStateException("Web client returns error");
			} 
			
			return password;
	
		} catch (Exception e) {
			
			throw new IllegalStateException("Web client returns error"); 
			
		}	 
		
		
	}

	/**
	 * Make a donation to the specified fund for the specified amount.
	 * This method uses the /makeDonation endpoint in the API
	 * @return true if successful, false otherwise
	 */
	public boolean makeDonation(String contributorId, String fundId, String amount) {
		if (client == null) {
			throw new IllegalStateException("WebClient should not be null");
		}

		if (contributorId == null || fundId == null || amount == null) {
			throw new IllegalArgumentException("ContributorId/FundId/Amount should not be null");
		}

		if (contributorId.length() == 0 || fundId.length() == 0 || amount.length() == 0) {
			throw new IllegalArgumentException("ContributorId/FundId/Amount should not be empty");
		}

		if (getContributorName(contributorId) == null) {
			throw new IllegalArgumentException("ContributorId is invalid.");
		}

		try {
			double a = Double.parseDouble(amount);
			if (a < 0.0) throw new NumberFormatException();

			Map<String, Object> map = new HashMap<>();
			map.put("contributor", contributorId);
			map.put("fund", fundId);
			map.put("amount", amount);
			String response = client.makeRequest("/makeDonation", map);


			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			String status = (String) json.get("status");

			if (!status.equals("success")) {
				throw new IllegalStateException();
			}

			return true;

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Amount must be numeric and non-negative");
		} catch (Exception e) {
			throw new IllegalStateException("An unexpected error occurred during making a donation");
		}
	}



}
