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
	
	public void start(String currentPassword) {
		
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
			
			// 3.3 : Add in option to edit account information
			System.out.println("Enter -3 to edit this organization’s account information");
			
			// 3.2 : Add in option to change password
			System.out.println("Enter -4 to edit this organization’s password");
			
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
							ui.start(password);
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
				
			// 3.3 : option to change account information
			} else if (option == -3) {
				System.out.println("Enter your password:");
				String passwordEntered = in.next();
				in.nextLine();
				
				// check if password ok
				if (passwordEntered.equals(currentPassword)) {
					System.out.println("Success!");

						try {
							updateAccountInfo();
						} catch (Exception e) {
							System.out.println("An error occurred.");
						}
					
				} else {
					System.out.println("Login failed. Going back to main menu.");
				}	
			
			// 3.2 : option to change password
			} else if (option == -4) {
				System.out.println("Enter your password:");
				String passwordEntered = in.next();
				in.nextLine();
				
				if (passwordEntered.equals(currentPassword)) {
	
					System.out.println("Current password entered successfully.");
					
					try {
						String newPassword = updatePassword(currentPassword);
						currentPassword = newPassword;
						
					} catch (Exception e) {
						System.out.println("An error occurred.");
					}
					
					
				} else {
					
					System.out.println("Login failed. Going back to main menu.");
					
				}
				
			}
			else {
				displayFund(option);
			}
		}			
			
	}
	
	public String updatePassword(String currentPassword) { 
		
		while (true) {
			System.out.println("Enter your new password: ");
			String newPassword = in.next();
			in.nextLine();
			
			System.out.println("Enter your new password again: "); 
			String newPasswordRepeat = in.next();
			in.nextLine();
			
			if (!newPassword.equals(newPasswordRepeat)) {
				System.out.println("Passwords do not match. Going back to main menu.");
				return currentPassword;
				
			} else {
				
				try {
					String newPasswordFinal = dataManager.updatePassword(org.getId(), newPassword);
					System.out.println("Password changed!");
					return newPasswordFinal;
				} catch (Exception e) {
					System.out.println("There's been an error, try again.");
				}
			
			}
			
		}
		
	}
	
	public void updateAccountInfo() {
		
		while (true) {
			
			boolean updateAccount = false;
			String name = org.getName();
			String description = org.getDescription();
			
			System.out.println("Your organization's current name is: " + org.getName());
			System.out.println("Would you like to change the name? (y/n)");
			String wantsToChangeName = in.next();
			in.nextLine();
			
			if (wantsToChangeName.toLowerCase().equals("y")) {
				System.out.println("Enter organization's new name: ");
				String newName = in.nextLine();
				in.nextLine();
				
				if (!newName.equals(name)) {
					updateAccount = true;
					name = newName;
				}
			}
			
			System.out.println("Your organization's current description is: " + org.getDescription());
			System.out.println("Would you like to change the description? (y/n)");
			String wantsToChangeDescription = in.next();
			in.nextLine();
			
			if (wantsToChangeDescription.toLowerCase().equals("y")) {
				System.out.println("Enter organization's new description: ");
				String newDescription = in.nextLine();
				in.nextLine();
				
				if (!newDescription.equals(description)) {
					updateAccount = true;
					description = newDescription;
				}
			}
			
			if (updateAccount) { 
				try {
					this.org = dataManager.updateAccount(org, name, description);
					System.out.println("Successfully edited organization's info.");
					break;
				} catch (Exception e){
					System.out.println("There's been an error, try again.");
				}
			} else {
				System.out.println("Returning to main menu.");
				break;
			}
			
		}
	}
	
	public void createFund() { 
		
		while(true) {
			System.out.print("Enter the fund name: ");
			String name = in.nextLine().trim();
			
			System.out.print("Enter the fund description: ");
			String description = in.nextLine().trim();
			
			System.out.print("Enter the fund target: ");
			
			try {
				long target = in.nextInt();
				in.nextLine();
				Fund fund = dataManager.createFund(org.getId(), name, description, target);
				org.getFunds().add(fund);
				break;
			} catch (Exception e) {
				System.out.println("Error. Please enter values again.");
				in.nextLine();
			}
			
		}
		
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
		System.out.println("\n\n");
		System.out.println("Donations:");
		for (Donation donation : donations) {
			System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on " + donation.getDate());
		}

		// print aggregate contributions
		System.out.println("\n\n");
		System.out.println("Aggregated Donations:");
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
	
	/**
	 * Creates restarts the program to allow for the creation of a new organization.
	 */
	public static void restart() {
		Scanner in = new Scanner(System.in);
		
		while (true) {
			System.out.println("Enter 1 to submit login information for a previously-created organization");
			System.out.println("Enter 2 to create a new organization");
			
			try {
				int option = in.nextInt();
				in.nextLine();
				
				if (option == 1) {
					System.out.println("Enter username:");
					String username = in.next();
					in.nextLine();
					
					System.out.println("Enter password:");
					String password = in.next();
					in.nextLine();
					
					DataManager ds = new DataManager(new WebClient("localhost", 3001));
					
					try {
						Organization org = ds.attemptLogin(username, password);

						if (org == null) {
							System.out.println("Login failed.");
							continue;
						}
						else {
							UserInterface ui = new UserInterface(ds, org);
							ui.start(password);
						}
					}
					catch (Exception e) {
						System.out.println("Error in communicating with the server.");
					}
					
				} else if (option == 2) {
					
					while (true) {
						System.out.println("Enter username login:");
						String login = in.next();
						
						System.out.println("Enter password:");
						String password = in.next();
						
						System.out.println("Enter name of organization:");
						String name = in.next();
						
						System.out.println("Enter description of organization:");
						String description = in.next();
						
						DataManager ds = new DataManager(new WebClient("localhost", 3001));
						
						try {
							boolean status = ds.createOrg(login, password, name, description);
							if (status == true) {
								
								try {
									Organization org = ds.attemptLogin(login, password);

									if (org == null) {
										System.out.println("Login failed.");
										continue;
									}
									else {
										UserInterface ui = new UserInterface(ds, org);
										ui.start(password);
									}
								}
								
								catch (Exception e) {
									System.out.println("Error in communicating with the server.");
								}
								
							} else {
								System.out.println("Organization creation failed. Enter information again:");
								continue;
							}
							
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
			
					}
					
				} else {
					continue;
				}
			} catch (Exception e) {
				System.out.println("Input not readable");
				in.next();
			}
		}
	}
	
	
	public static void main(String[] args) {
		
		DataManager ds = new DataManager(new WebClient("localhost", 3001));
		
		try {
			String login = args[0];
			String password = args[1];
			
			try {
				Organization org = ds.attemptLogin(login, password);
				if (org == null) {
					// old way 
					System.out.println("Login failed with incorrect information.");
					
					restart();
				}
				else {
					UserInterface ui = new UserInterface(ds, org);
					ui.start(password);
				}
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			System.out.println("No login information provided.");
			
			restart();
		}
		

	}

}
