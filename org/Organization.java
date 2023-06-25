import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Organization {
	
	private String id;
	private String name;
	private String description;
	
	private List<Fund> funds;
	
	public Organization(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
		funds = new LinkedList<>();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<Fund> getFunds() {
		return funds;
	}
	
	public void addFund(Fund fund) {
		funds.add(fund);
	}
	
	public List<Donation> getAllSortedDonations() {
		
		// initialize list of donations
		List<Donation> donationList = new ArrayList<Donation>();
		
		for (Fund f : funds) {
			for (Donation d : f.getDonations()) {
				donationList.add(d);
			}
		}
		
		Comparator<Donation> dateCompare = new Comparator<Donation>() {
			 SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

	            @Override
	            public int compare(Donation don1, Donation don2) {
	                try {
	                    Date date1 = dateFormat.parse(don1.getDate());
	                    Date date2 = dateFormat.parse(don2.getDate());
	                    return date2.compareTo(date1);  // Descending order
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	                return 0;
	            }
		};
		
		Collections.sort(donationList, dateCompare);
		
		return donationList;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

