import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    Path membersFile= Paths.get("src/customers.txt");
    final String memberActivityPath = "src/memberactivity.txt";

    public void mainProgram() {
        List<Member> listOfMembers = createMembersListFromFile(membersFile);
        while (true) {
            String userInput = getUserInput();
            System.out.println(membershipControl(userInput, listOfMembers));
        }

    }



    public String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the full name 'First name  Surname' or personal-number 'YYMMDDNNNN' of the member: ");
        String userInput = scanner.nextLine();

        while (userInput.isBlank()) {
            System.out.println("No input read, please enter a valid input");
            userInput = scanner.nextLine();
        }

        return userInput;
    }

    public List<Member> createMembersListFromFile(Path membersFile) {
        List<Member> membersListFromFile = new ArrayList<>();
        String personNumber = "";
        String fullname = "";
        String lastPaidDate = "";
        try (Scanner readTextFile = new Scanner(membersFile)) {
            while (readTextFile.hasNext()) {
                personNumber = readTextFile.next().replace(",", "").trim(); //Reads first fileinput(PersonalNumber)
                if (readTextFile.hasNext()) {
                    fullname = readTextFile.nextLine(); //Reads the rest of the firstLine(Full name)
                    fullname = fullname.substring(1);
                    if (readTextFile.hasNext()) {
                        lastPaidDate = readTextFile.nextLine(); //Reads the second line as paymentDate.
                    }
                }
                membersListFromFile.add(new Member(personNumber, fullname, lastPaidDate));
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found.");
            e.printStackTrace();
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Something went wrong, please contact the system administrator.");
            e.printStackTrace();
            System.exit(0);
        }


        return membersListFromFile;
    }

    public String membershipControl(String userInput, List<Member> listOfMembers) {
        boolean membership = false;

        for (Member member : listOfMembers) {
            if (userInput.equalsIgnoreCase(member.getFullName()) || userInput.equals(member.getPersonalNumber())) {
                String lastPaymentDate = member.getLastYearlyPayment();
                membership = lessTimeThanAYear(lastPaymentDate);
                if (membership == true) {
                    registerMemberActivity(memberActivityPath, member);
                    return member.getFullName() + " is an activate member. Last payment date: " + member.getLastYearlyPayment();
                } else {
                    return member.getFullName() + " is an inactive member, payment is due. Last payment date: " + member.getLastYearlyPayment();
                }
            }
        }
        return "No member found with the full name or personal-number of \"" + userInput + "\"";
    }
    public boolean lessTimeThanAYear(String lastpaymentString) {
        LocalDate lastPaymentDate = LocalDate.parse(lastpaymentString);
        LocalDate currentDate = LocalDate.now();

        if (lastPaymentDate.isAfter(currentDate.minusYears(1)) || lastPaymentDate.isEqual(currentDate.minusYears(1))) {
            return true;
        } else {
            return false;
        }
    }
    public void registerMemberActivity(String activitylog, Member member) {

        try (PrintWriter writer = new PrintWriter(
                new FileOutputStream(activitylog, true)))
        {
            writer.append(member.getFullName() + " worked out " + LocalDate.now()+"\n");


        } catch (FileNotFoundException fnf) {
            System.out.println("File not found");
            fnf.printStackTrace();
        } catch (Exception e) {
            System.out.println("Oops! Something went wrong!");
            e.printStackTrace();
        }


    }




    public static void main(String[] args) {
        Main BestGymEver = new Main();
        BestGymEver.mainProgram();
    }
}
