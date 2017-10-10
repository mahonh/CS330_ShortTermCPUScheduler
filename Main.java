import java.util.Scanner;

public class Main
{
    public static void main(String args[])
    {
        Scanner scany = new Scanner(System.in);
        int userChoice;

        while (true)
        {
            System.out.println("Welcome! Please select an option for which algorithm you want to run:");
            System.out.println("1. First Come First Serve");
            System.out.println("2. Round Robin");
            System.out.println("3. Static Priority");
            System.out.println("4. Exit");

            userChoice = scany.nextInt();

            if (userChoice == 4)
            {
                System.out.println("Goodbye");
                System.exit(0);
            }

            if (userChoice < 1 || userChoice > 4)
                System.out.println("Invalid choice, try again");
            else
                break;
        }

        OS myOS = new OS(userChoice);

    }
}
