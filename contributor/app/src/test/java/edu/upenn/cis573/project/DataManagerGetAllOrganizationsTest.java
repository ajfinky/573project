package edu.upenn.cis573.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataManagerGetAllOrganizationsTest {

    @Test
    public void testUnsuccessfulResponse() {
        DataManager dm = new DataManager(new WebClient(null, 0) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                String id = "123";
                String name = "Snoopy";

                return "{\"status\": \"unsuccessful\", " +
                        "\"data\": {\"_id\": \"" + id + "\", \"name\": \"" + name + "\"}}";
            }
        });
        assertNull(dm.getAllOrganizations());
    }

    @Test
    public void testMissingJsonParameter() {
        DataManager dm = new DataManager(new WebClient(null, 0) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                String id = "123";

                return "{\"status\": \"success\", " +
                        "\"data\": {\"_id\": \"" + id + "\"}}";
            }
        });
        assertNull(dm.getAllOrganizations());
    }

    @Test
    public void testGetAllOrganizations() {
        String id1 = "123";
        String name1 = "Snoopy";
        String fund1 = "[{\"_id\": \"1\", \"name\": \"Fund A\", \"target\": 1000, \"totalDonations\": 5000}," +
                "{\"_id\": \"2\", \"name\": \"Fund B\", \"target\": 5000, \"totalDonations\": 2500}]";

        String id2 = "456";
        String name2 = "Snoopy2";
        String fund2 = "[{\"_id\": \"1\", \"name\": \"Fund C\", \"target\": 1000, \"totalDonations\": 5000}," +
                "{\"_id\": \"2\", \"name\": \"Fund D\", \"target\": 5000, \"totalDonations\": 2500}]";

        DataManager dm = new DataManager(new WebClient("http://localhost", 8080) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return String.format("{\"status\": \"success\", " +
                                "\"data\": [{\"_id\": \"%s\", \"name\": \"%s\", \"funds\": %s}, " +
                                "{\"_id\": \"%s\", \"name\": \"%s\", \"funds\": %s}]}",
                        id1, name1, fund1, id2, name2, fund2);
            }
        });

        List<Organization> organizations = new LinkedList<>();
        Organization org1 = new Organization(id1, name1);
        List<Fund> fundList1 = new LinkedList<>();
        fundList1.add(new Fund("1", "Fund A", 1000, 5000));
        fundList1.add(new Fund("2", "Fund B", 5000, 2500));
        org1.setFunds(fundList1);
        organizations.add(org1);

        Organization org2 = new Organization(id2, name2);
        List<Fund> fundList2 = new LinkedList<>();
        fundList2.add(new Fund("1", "Fund C", 1000, 5000));
        fundList2.add(new Fund("2", "Fund D", 5000, 2500));
        org2.setFunds(fundList2);
        organizations.add(org2);

        List<Organization> actualOutput = dm.getAllOrganizations();
        assertNotNull(actualOutput);
        assertEquals(organizations, actualOutput);
    }
}
