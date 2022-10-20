public class Member {

    String personalNumber;
    String fullName;
    String lastYearlyPayment;
//    String firstName;
//    String Lastname;

    public Member(String personalNumber, String fullName, String lastYearlyPayment){
        this.personalNumber=personalNumber;
        this.fullName = fullName;
        this.lastYearlyPayment=lastYearlyPayment;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setLastYearlyPayment(String lastYearlyPayment) {
        this.lastYearlyPayment = lastYearlyPayment;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLastYearlyPayment() {
        return lastYearlyPayment;
    }
}
