import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserInterface {
	
	
	private DataManager dataManager;
	private Organization org;
	private Scanner in = new Scanner(System.in);

	public UserInterface(DataManager dataManager, Organization org) {
		this.dataManager = dataManager;
		this.org = org;
	}
	
	public void start() {
		
		// initialize map variable
		Map<String, String> idMap = new HashMap<String, String>();	
		
		while (true) {
			System.out.println("\n\n");
			if (org.getFunds().size() > 0) {
				System.out.println("There are " + org.getFunds().size() + " funds in this organization:");
			
				int count = 1;
				
				for (Fund f : org.getFunds()) {
					
					// create dictionary to map ID to name
					idMap.put(f.getId(), f.getName());
					
					System.out.println(count + ": " + f.getName());
					
					count++;
				}
				System.out.println("Enter the fund number to see more information.");
			}
			System.out.println("Enter 0 to create a new fund");
			
			// 2.8 : Add in logout functionality
			System.out.println("Enter -1 to log out of this account and log into another");
			
			// 2.9 : Add in option to display all fund information
			System.out.println("Enter -2 to display donations for all funds in this organization");
			
			int option = in.nextInt();
			in.nextLine();
			if (option == 0) {
				createFund(); 
				
			// 2.8 : option to log out
			} else if (option == -1) {
				while(true) {
					System.out.println("Enter new username:");
					String username = in.next();
					in.nextLine();
					
					System.out.println("Enter new password:");
					String password = in.next();
					in.nextLine();
					
					DataManager ds = new DataManager(new WebClient("localhost", 3001));
					
					try {
						Organization org = ds.attemptLogin(username, password);

						if (org == null) {
							System.out.println("Login failed.");
						}
						else {
							UserInterface ui = new UserInterface(ds, org);
							ui.start();
						}
					}
					catch (Exception e) {
						System.out.println("Error in communicating with the server.");
					}
				}
			} else if (option == -2) {
				List<Donation> donationList = org.getAllSortedDonations();
				
				if (donationList.size() == 0) {
					System.out.println("There are no donations for this organization.");
				}
				
				for (Donation d : donationList) {
					System.out.println("* : $" + d.getAmount() + " on " + d.getDate() + " for " + idMap.get(d.getFundId()));
				}
			}
			else {
				displayFund(option);
			}
		}			
			
	}
	
	public void createFund() {
		
		System.out.print("Enter the fund name: ");
		String name = in.nextLine().trim();
		
		System.out.print("Enter the fund description: ");
		String description = in.nextLine().trim();
		
		System.out.print("Enter the fund target: ");
		long target = in.nextInt();
		in.nextLine();

		Fund fund = dataManager.createFund(org.getId(), name, description, target);
		org.getFunds().add(fund);

		
	}
	
	
	public void displayFund(int fundNumber) {
		
		Fund fund = org.getFunds().get(fundNumber - 1);
		
		System.out.println("\n\n");
		System.out.println("Here is information about this fund:");
		System.out.println("Name: " + fund.getName());
		System.out.println("Description: " + fund.getDescription());
		System.out.println("Target: $" + fund.getTarget());
		
		List<Donation> donations = fund.getDonations();
		System.out.println("Number of donations: " + donations.size());
		
		// create variable to store donation percentage

		// print individual contributions
		for (Donation donation : donations) {
			System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on " + donation.getDate());
		}

		// print aggregate contributions
		String[] contributorArr = fund.getSortedContributors();
		Map<String, List<Long>> contributorMap = fund.getContributorTotals();
		for (String s : contributorArr) {
			List<Long> stats = contributorMap.get(s);
			System.out.println(s + ", " + stats.get(0) + " donations, $" + stats.get(1) + " total.");
		}

		
		// Print sum of donations
		if (fund.getTarget() > 0) {
			System.out.println("Sum of Donations: $" + fund.totalDonationQuantity() + " (" + fund.percentageOfGoal(fund.totalDonationQuantity()) + 
					"% of Goal)");
		} else {
			System.out.println("Sum of Donations: $" + fund.totalDonationQuantity() + " (Target is not set or valid)");
		}
		
		
				
		System.out.println("Press the Enter key to go back to the listing of funds");
		in.nextLine();
		
		
	}
	
	
	public static void main(String[] args) {
		
		DataManager ds = new DataManager(new WebClient("localhost", 3001));
		
		String login = args[0];
		String password = args[1];
		
		try {
			Organization org = ds.attemptLogin(login, password);

			if (org == null) {
				System.out.println("Login failed.");
			}
			else {
				UserInterface ui = new UserInterface(ds, org);
				ui.start();
			}
		}
		catch (Exception e) {
			System.out.println("Error in communicating with the server.");
		}
	}

}
