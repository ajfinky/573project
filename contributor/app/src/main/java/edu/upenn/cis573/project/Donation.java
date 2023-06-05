package edu.upenn.cis573.project;

import java.io.Serializable;
import java.util.Objects;

public class Donation implements Serializable {

    private String fundName;
    private String contributorName;
    private long amount;
    private String date;

    public Donation(String fundName, String contributorName, long amount, String date) {
        this.fundName = fundName;
        this.contributorName = contributorName;
        this.amount = amount;
        this.date = date;
    }

    public String getFundName() {
        return fundName;
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

    public String toString() {
        return fundName + ": $" + amount + " on " + date;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Donation other = (Donation) obj;
        return Objects.equals(this.fundName, other.getFundName())
                && Objects.equals(this.contributorName, other.getContributorName())
                && Objects.equals(this.amount, other.getAmount())
                && Objects.equals(this.date, other.getDate());
    }

}