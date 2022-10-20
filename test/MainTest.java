import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {
    Path testMembersFile = Paths.get("test/testcustomers.txt");
    Path membersFile = Paths.get("src/customers.txt");
    String testactivitypath = "test/testactivity.txt";
    Path readactivitytest = Paths.get("test/testactivity.txt");
    Main testMain = new Main();
    List<Member> testMembers = new ArrayList<>();


    @Test
    public final void getListFromFile() { //Controls if method accurately reads file & Creates a list from it.
        List<Member> memberslistFromFile = testMain.createMembersListFromFile(testMembersFile);
        assertTrue(memberslistFromFile.size() == 3);
        assertTrue(memberslistFromFile.get(0).getPersonalNumber().equals("9010211234"));
        assertTrue(memberslistFromFile.get(1).getFullName().equals("Björn Bjuv"));
        assertTrue(!memberslistFromFile.get(2).getPersonalNumber().equals("9010211234"));
        assertTrue(memberslistFromFile.get(0).getLastYearlyPayment().equals("2022-07-01")); //Checks that lastpaymentdate is correctly read from file as it is changed in other test.
        assertTrue(memberslistFromFile.get(1).getLastYearlyPayment().equals("2012-12-02"));//Checks that lastpaymentdate is correctly read from file as it is changed in other test.
    }


    @Test
    public final void checkMembership() {
        List<Member> testMembers = testMain.createMembersListFromFile(testMembersFile);
        String activeCustomer = "Alina Andersson";
        testMembers.get(0).setLastYearlyPayment(LocalDate.now().minusMonths(5).toString());
        //Ensures testmember 'Alina Andersson' is active, regardless of when the test is run.
        String inactiveCustomer = "8204021234";
        testMembers.get(1).setLastYearlyPayment(LocalDate.now().minusYears(1).minusMonths(3).toString());
        //Ensures testmember 'Björn Bjuv' is inactive, regardless of when the test is run.
        String notCustomer = "9102025423";
        //Tests if correct input can be found by name, and if active status is read correctly
        assertTrue(testMain.membershipControl(activeCustomer, testMembers).equals("Alina Andersson is an activate member. Last payment date: " + LocalDate.now().minusMonths(5).toString()));
        //Tests if correct input can be found by 'Personal-number', and if inactive status can be read correctly.
        assertTrue(testMain.membershipControl(inactiveCustomer, testMembers).equals("Björn Bjuv is an inactive member, payment is due. Last payment date: " + LocalDate.now().minusYears(1).minusMonths(3).toString()));
        //Tests if incorrect input works as intended.
        assertTrue(testMain.membershipControl(notCustomer, testMembers).equals("No member found with the full name or personal-number of \"9102025423\""));

    }

    @Test
    public final void checkDate() {

        LocalDate currentDate = LocalDate.now(); // controls if method "lessTimeThanAYear" works as intended. Returns true if time passed is that of a year or less.

        String validDate = currentDate.minusMonths(2).toString();
        String invalidDate = currentDate.minusYears(1).minusMonths(1).toString();
        String oneYear = currentDate.minusYears(1).toString();
        boolean testValidDate = testMain.lessTimeThanAYear(validDate);
        boolean testInvalidDate = testMain.lessTimeThanAYear(invalidDate);
        boolean testOneYearDate = testMain.lessTimeThanAYear(oneYear);

        assertTrue(testValidDate);
        assertFalse(testInvalidDate);
        assertTrue(testOneYearDate);
        assertTrue(!testInvalidDate);

    }

    @Test
    public final void checkActivityLog() { //Tests the writing to file function and that data is saved correctly.
        List<Member> testMembers = testMain.createMembersListFromFile(testMembersFile);
        String writtenRowInTestFile = "";
        assertTrue(writtenRowInTestFile.isBlank());
        testMain.registerMemberActivity(testactivitypath, testMembers.get(0));
        testMain.registerMemberActivity(testactivitypath, testMembers.get(1));
        testMain.registerMemberActivity(testactivitypath, testMembers.get(2));

        try (Scanner readTextFile = new Scanner(readactivitytest)) {
            Files.delete(readactivitytest);
            //Deletes testfile at the start of test, new file will be created. This leaves the file after test if manual viewing is necessary.
            // A new file each run also ensures that the test won't because of previous run-dates.
            while (readTextFile.hasNext()) {
                writtenRowInTestFile = readTextFile.nextLine();
                System.out.println(writtenRowInTestFile);
                assertTrue(writtenRowInTestFile.equals("Alina Andersson worked out " + LocalDate.now()));
                assertTrue(!writtenRowInTestFile.isBlank());
                writtenRowInTestFile = readTextFile.nextLine();
                System.out.println(writtenRowInTestFile);
                assertTrue(writtenRowInTestFile.equals("Björn Bjuv worked out " + LocalDate.now()));
                assertTrue(!writtenRowInTestFile.isBlank());
                writtenRowInTestFile = readTextFile.nextLine();
                System.out.println(writtenRowInTestFile);
                assertTrue(writtenRowInTestFile.equals("Carina Cederholm worked out " + LocalDate.now()));
                assertTrue(!writtenRowInTestFile.isBlank());
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


    }
}