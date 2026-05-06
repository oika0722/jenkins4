package fromL2;

import java.io.*;

public class Violation implements Serializable {
    int id;
    String licensePlate;
    String lastName;
    String firstName;
    String middleName;
    String violationType;
    String violationDate;
    double fineAmount;
    String paymentDate;

    public Violation() {
    }

    public Violation(String licensePlate, String lastName, String firstName, String middleName,
                     String violationType, String violationDate, double fineAmount, String paymentDate) {
        this.licensePlate = licensePlate;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.violationType = violationType;
        this.violationDate = violationDate;
        this.fineAmount = fineAmount;
        this.paymentDate = paymentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getViolationType() {
        return violationType;
    }

    public void setViolationType(String violationType) {
        this.violationType = violationType;
    }

    public String getViolationDate() {
        return violationDate;
    }

    public void setViolationDate(String violationDate) {
        this.violationDate = violationDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Violation{" +
                "id=" + id +
                ", licensePlate='" + licensePlate + '\'' +
                ", driver='" + lastName + " " + firstName + " " + middleName + '\'' +
                ", violationType='" + violationType + '\'' +
                ", violationDate='" + violationDate + '\'' +
                ", fineAmount=" + fineAmount +
                ", paymentDate='" + paymentDate + '\'' +
                '}';
    }
}