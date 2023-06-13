package edu.upenn.cis573.project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataManager {

    private final WebClient client;
    private final HashMap<String, String> cacheFundName = new HashMap<>();

    public DataManager(WebClient client) {
        this.client = client;
    }


    /**
     * Attempt to log in to the Contributor account using the specified login and password.
     * This method uses the /findContributorByLoginAndPassword endpoint in the API
     * @return the Contributor object if successfully logged in, null otherwise
     */
    public Contributor attemptLogin(String login, String password) {
        if (client == null) {
            throw new IllegalStateException("WebClient should not be null");
        }

        if (login == null || password == null) {
            throw new IllegalArgumentException("Login/Password should not be null");
        }

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("login", login);
            map.put("password", password);
            String response = client.makeRequest("/findContributorByLoginAndPassword", map);

            JSONObject json = new JSONObject(response);
            String status = (String)json.get("status");

            if (!status.equals("success")) {
                throw new IllegalStateException();
            }

            JSONObject data = (JSONObject)json.get("data");
            String id = (String)data.get("_id");
            String name = (String)data.get("name");
            String email = (String)data.get("email");
            String creditCardNumber = (String)data.get("creditCardNumber");
            String creditCardCVV = (String)data.get("creditCardCVV");
            String creditCardExpiryMonth = ((Integer)data.get("creditCardExpiryMonth")).toString();
            String creditCardExpiryYear = ((Integer)data.get("creditCardExpiryYear")).toString();
            String creditCardPostCode = (String)data.get("creditCardPostCode");

            Contributor contributor = new Contributor(id, name, email, creditCardNumber,
                    creditCardCVV, creditCardExpiryMonth, creditCardExpiryYear, creditCardPostCode);

            List<Donation> donationList = new LinkedList<>();

            JSONArray donations = (JSONArray)data.get("donations");

            for (int i = 0; i < donations.length(); i++) {

                JSONObject jsonDonation = donations.getJSONObject(i);

                String fundId = (String)jsonDonation.get("fund");
                String fund = cacheFundName.getOrDefault(fundId, null);

                if (fund == null) {
                    fund = getFundName(fundId);
                    cacheFundName.put(fundId, fund);
                }

                String date = (String)jsonDonation.get("date");
                long amount = (Integer)jsonDonation.get("amount");

                Donation donation = new Donation(fund, name, amount, date);
                donationList.add(donation);

            }

            contributor.setDonations(donationList);

            return contributor;

        } catch(JSONException e) {
            throw new IllegalStateException("WebClient returned malformed JSON");
        } catch (Exception e) {
           throw new IllegalStateException("An unexpected error occurred during login");
        }
    }

    /**
     * Get the name of the fund with the specified ID using the /findFundNameById endpoint
     * @return the name of the fund if found, "Unknown fund" if not found, null if an error occurs
     */
    public String getFundName(String id) {
        if (client == null) {
            throw new IllegalStateException("WebClient should not be null");
        }

        if (id == null) {
            throw new IllegalArgumentException("Id should not be null");
        }

        if (cacheFundName.containsKey(id)) {
            return cacheFundName.get(id);
        }

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            String response = client.makeRequest("/findFundNameById", map);

            JSONObject json = new JSONObject(response);
            String status = (String)json.get("status");

            if (!status.equals("success")) {
                throw new IllegalStateException();
            }

            String name = (String)json.get("data");
            cacheFundName.put(id, name);
            return name;

        } catch (JSONException e) {
            throw new IllegalStateException("WebClient returned malformed JSON");
        } catch (Exception e) {
            throw new IllegalStateException("An unexpected error occurred during getting fund name");
        }
    }

    /**
     * Get information about all of the organizations and their funds.
     * This method uses the /allOrgs endpoint in the API
     * @return a List of Organization objects if successful, null otherwise
     */
    public List<Organization> getAllOrganizations() {
        if (client == null) {
            throw new IllegalStateException("WebClient should not be null");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            String response = client.makeRequest("/allOrgs", map);

            JSONObject json = new JSONObject(response);
            String status = (String)json.get("status");

            if (!status.equals("success")) {
                throw new IllegalStateException();
            }

            List<Organization> organizations = new LinkedList<>();

            JSONArray data = (JSONArray)json.get("data");

            for (int i = 0; i < data.length(); i++) {

                JSONObject obj = data.getJSONObject(i);

                String id = (String)obj.get("_id");
                String name = (String)obj.get("name");

                Organization org = new Organization(id, name);

                List<Fund> fundList = new LinkedList<>();

                JSONArray array = (JSONArray)obj.get("funds");

                for (int j = 0; j < array.length(); j++) {

                    JSONObject fundObj = array.getJSONObject(j);

                    id = (String)fundObj.get("_id");
                    name = (String)fundObj.get("name");
                    long target = (Integer)fundObj.get("target");
                    long totalDonations = (Integer)fundObj.get("totalDonations");

                    Fund fund = new Fund(id, name, target, totalDonations);

                    fundList.add(fund);

                }

                org.setFunds(fundList);

                organizations.add(org);

            }

            return organizations;

        } catch(JSONException e) {
            throw new IllegalStateException("WebClient returned malformed JSON");
        } catch (Exception e) {
            throw new IllegalStateException("An unexpected error occurred during getting fund name");
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

        try {
            Double.parseDouble(amount); // Check if amount is numeric

            Map<String, Object> map = new HashMap<>();
            map.put("contributor", contributorId);
            map.put("fund", fundId);
            map.put("amount", amount);
            String response = client.makeRequest("/makeDonation", map);

            JSONObject json = new JSONObject(response);
            String status = json.getString("status");

            if (!status.equals("success")) {
                throw new IllegalStateException();
            }

            return true;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount must be numeric");
        } catch (JSONException e) {
            throw new IllegalStateException("WebClient returned malformed JSON");
        } catch (Exception e) {
            throw new IllegalStateException("An unexpected error occurred during making a donation");
        }
    }

}
