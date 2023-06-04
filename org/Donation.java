public class Donation {
	
	private String fundId;
	private String contributorName;
	private long amount;
	private String date;
	
	public Donation(String fundId, String contributorName, long amount, String date) {
		this.fundId = fundId;
		this.contributorName = contributorName;
		this.amount = amount;

		Map<String, String> month = Map.of(
				"01", "January",
				"02", "February",
				"03", "March",
				"04", "April",
				"05", "May",
				"06", "June",
				"07", "July",
				"08", "August",
				"09", "September",
				"10", "October",
				"11", "November",
				"12", "December"
		);

		this.date = month.get(date.substring(5, 7),) + " " + date.substring(8, 10) + ", " + date.substring(4);
	}

	public String getFundId() {
		return fundId;
	}

	public String getContributorName() {
		return contributorName;
	}

	public long getAmount() {
		return amount;
	}

	public String getDate() {
		return date;
	}
	
	

}
