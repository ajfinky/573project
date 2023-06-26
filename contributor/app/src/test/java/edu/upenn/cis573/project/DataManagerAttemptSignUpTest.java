package edu.upenn.cis573.project;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.Map;

public class DataManagerAttemptSignUpTest {

    // Test when WebClient returns malformed JSON
    @Test(expected = IllegalStateException.class)
    public void testAttemptSignUp_WebClientReturnsMalformedJSON()  {
        DataManager dm = new DataManager(new WebClient("10.0.2.2", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "I AM NOT JSON!";
            }
        });
        dm.attemptSignUp("John", "john123", "pass123", "john@example.com",
                "1234123412341234", "123", "12", "2023", "12345");
        fail("DataManager.attemptSignUp does not throw IllegalStateException when WebClient returns malformed JSON");
    }

    // Test when WebClient is null
    @Test(expected = IllegalStateException.class)
    public void testAttemptSignUp_WebClientIsNull() {
        DataManager dm = new DataManager(null);
        dm.attemptSignUp("John", "john123", "pass123", "john@example.com",
                "1234123412341234", "123", "12", "2023", "12345");
        fail("DataManager.attemptSignUp does not throw IllegalStateException when WebClient is null");
    }

    // Test when any of the input fields are null
    @Test(expected = IllegalArgumentException.class)
    public void testAttemptSignUp_NullFields()  {
        DataManager dm = new DataManager(new WebClient("10.0.2.2", 3001));
        dm.attemptSignUp(null, "john123", "pass123", "john@example.com",
                "1234123412341234", "123", "12", "2023", "12345");
        fail("DataManager.attemptSignUp does not throw IllegalArgumentException when fields are null");
    }

    // Test when login name is already taken
    @Test(expected = IllegalArgumentException.class)
    public void testAttemptSignUp_DuplicateLogin()  {
        DataManager dm = new DataManager(new WebClient("10.0.2.2", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource.equals("/findContributorByName")) {
                    return "{\"status\": \"success\", \"data\": \"john123\"}";
                }
                return "{\"status\": \"success\"}";
            }
        });
        dm.attemptSignUp("John", "john123", "pass123", "john@example.com",
                "1234123412341234", "123", "12", "2023", "12345");
        fail("DataManager.attemptSignUp does not throw IllegalArgumentException when login name already exists");
    }

    // Test a successful signup
    @Test
    public void testAttemptSignUp_Success() {
        String id = "unique_id";
        String name = "John";
        String login = "john123";
        String password = "pass123";
        String email = "john@example.com";
        String creditCardNumber = "1234123412341234";
        String creditCardCVV = "123";
        String creditCardExpiryMonth = "12";
        String creditCardExpiryYear = "2023";
        String creditCardPostCode = "12345";
        String donations = "[]";  // Define donations as per your requirements.

        DataManager dm = new DataManager(new WebClient("10.0.2.2", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource.equals("/findContributorByName")) {
                    return "{\"status\": \"not found\", \"data\": []}";
                }
                return "{\"status\": \"success\", " +
                        "\"data\": {\"_id\": \"" + id + "\", " +
                        "\"name\": \"" + name + "\", " +
                        "\"email\": \"" + email + "\", " +
                        "\"creditCardNumber\": \"" + creditCardNumber + "\", " +
                        "\"creditCardCVV\": \"" + creditCardCVV + "\", " +
                        "\"creditCardExpiryMonth\": \"" + creditCardExpiryMonth + "\", " +
                        "\"creditCardExpiryYear\": \"" + creditCardExpiryYear + "\", " +
                        "\"creditCardPostCode\": \"" + creditCardPostCode + "\", " +
                        "\"donations\": " + donations + "}}";
            }
        });
        Contributor contributor = dm.attemptSignUp(name, login, password, email,
                creditCardNumber, creditCardCVV, creditCardExpiryMonth, creditCardExpiryYear, creditCardPostCode);
        assertNotNull("Contributor returned from DataManager.attemptSignUp is null", contributor);
    }
}
