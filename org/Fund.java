import java.util.*;

public class Fund {

	private String id;
	private String name;
	private String description;
	private long target;
	private List<Donation> donations;
	private String[] contributorArr;
	private final Map<String, List<Long>> contributorMap = new HashMap<>();
	
	public Fund(String id, String name, String description, long target) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.target = target;
		donations = new LinkedList<>();
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

	public long getTarget() {
		return target;
	}

	public void setDonations(List<Donation> donations) {
		this.donations = donations;
		Set<String> contributorSet = new HashSet<>();
		for (Donation d : donations) {
			String name = d.getContributorName();
			contributorSet.add(name);
			if (contributorMap.containsKey(name)) {
				List<Long> list = contributorMap.get(name);
				list.set(0, list.get(0) + 1);
				list.set(1, list.get(1) + d.getAmount());
			} else {
				List<Long> list = new ArrayList<>();
				list.add(1L);
				list.add(d.getAmount());
				contributorMap.put(name, list);
			}
		}
		contributorArr = contributorSet.toArray(contributorArr);
		Arrays.sort(
				contributorArr,
				(s, t1) -> Long.compare(contributorMap.get(t1).get(1), contributorMap.get(s).get(1))
		);
	}
	
	public List<Donation> getDonations() {
		return donations;
	}
	
	public int totalDonationQuantity() {
		int sum = 0;
		for (Donation d: donations) {
			sum += d.getAmount();
		}
		return sum;
	}
	
	public Double percentageOfGoal(int sum) {
		Double percentage = ( (double) sum / target * 100);
		return percentage;
	}

	public String[] getSortedContributors() { return contributorArr; }

	public Map<String, List<Long>> getContributorTotals() { return contributorMap; }

	
}

