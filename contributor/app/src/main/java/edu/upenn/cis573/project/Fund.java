package edu.upenn.cis573.project;

import java.util.Objects;

public class Fund {

    private String id;
    private String name;
    private long target;
    private long totalDonations;

    public Fund(String id, String name, long target, long totalDonations) {
        this.id = id;
        this.name = name;
        this.target = target;
        this.totalDonations = totalDonations;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getTarget() {
        return target;
    }

    public long getTotalDonations() {
        return totalDonations;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Fund other = (Fund)obj;
        return Objects.equals(this.id, other.getId())
                && Objects.equals(this.name, other.getName())
                && Objects.equals(this.target, other.getTarget())
                && Objects.equals(this.totalDonations, other.getTotalDonations());
    }
}

