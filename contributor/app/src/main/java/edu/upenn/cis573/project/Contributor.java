package edu.upenn.cis573.project;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Contributor implements Serializable {

    private String id;
    private String name;
    private String email;
    private String creditCardNumber;
    private String creditCardCVV;
    private String creditCardExpiryMonth;
    private String creditCardExpiryYear;
    private String creditCardPostCode;
    private List<Donation> donations;

    public Contributor(String id, String name, String email, String creditCardNumber,
                       String creditCardCVV, String creditCardExpiryMonth, String creditCardExpiryYear,
                       String creditCardPostCode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.creditCardNumber = creditCardNumber;
        this.creditCardCVV = creditCardCVV;
        this.creditCardExpiryMonth = creditCardExpiryMonth;
        this.creditCardExpiryYear = creditCardExpiryYear;
        this.creditCardPostCode = creditCardPostCode;
        donations = new LinkedList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getCreditCardCVV() {
        return creditCardCVV;
    }

    public String getCreditCardExpiryMonth() {
        return creditCardExpiryMonth;
    }

    public String getCreditCardExpiryYear() {
        return creditCardExpiryYear;
    }

    public String getCreditCardPostCode() {
        return creditCardPostCode;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Contributor other = (Contributor) obj;
        return Objects.equals(this.id, other.getId())
                && Objects.equals(this.name, other.getName())
                && Objects.equals(this.email, other.getEmail())
                && Objects.equals(this.creditCardNumber, other.getCreditCardNumber())
                && Objects.equals(this.creditCardCVV, other.getCreditCardCVV())
                && Objects.equals(this.creditCardExpiryMonth, other.getCreditCardExpiryMonth())
                && Objects.equals(this.creditCardExpiryYear, other.getCreditCardExpiryYear())
                && Objects.equals(this.creditCardPostCode, other.getCreditCardPostCode())
                && Objects.equals(this.donations, other.getDonations());
    }
}
