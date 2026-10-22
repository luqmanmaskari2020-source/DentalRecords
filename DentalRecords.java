import java.util.Scanner;

/**
 * Manages dental records for Florida families.
 *
 * This program records the upper and lower teeth of up to
 * six family members using a three-dimensional array.
 * Users can print records, extract teeth, and calculate
 * root canal indices.
 *
 * I represents incisors, B represents bicuspids,
 * and M represents missing teeth.
 *
 * @author Luqman Al Maskari
 * @version 1.0
 */

public class DentalRecords {


    /**
     * Starts the dental records program.
     *
     * Collects family names and tooth information,
     * then displays the menu until the user exits.
     *
     * @param args command-line arguments, not used
     */

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        char[][][] teeth = new char[6][2][8];
        String[] names = new String[6];

        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("---------------------------------------");

        int familySize;

        do {
            System.out.print("Please enter number of people in the family: ");
            familySize = input.nextInt();

            if (familySize < 1 || familySize > 6) {
                System.out.println("Invalid number of people, try again");
            }

        } while (familySize < 1 || familySize > 6);


        input.nextLine(); // Clear the Enter key

        for (int index = 0; index < familySize; index++) {

            System.out.print("Please enter the name for family member "
                    + (index + 1) + ": ");
            names[index] = input.nextLine();

            for (int layer = 0; layer < 2; layer++) {

                String toothType;

                if (layer == 0) {
                    System.out.print("Please enter the uppers for "
                            + names[index] + ": ");
                } else {
                    System.out.print("Please enter the lowers for "
                            + names[index] + ": ");
                }

                toothType = input.nextLine().toUpperCase();

                // Check the teeth input
                while (toothType.length() < 1 ||
                        toothType.length() > 8 ||
                        !toothType.matches("[IBM]+")) {

                    System.out.println("Invalid teeth types, try again");
                    toothType = input.nextLine().toUpperCase();
                }

                // Store the teeth in the 3D array
                for (int toothIndex = 0; toothIndex < toothType.length(); toothIndex++) {
                    teeth[index][layer][toothIndex] = toothType.charAt(toothIndex);
                }
            }
        }


        String option;

        do {
            System.out.println();
            System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it: ");

            option = input.nextLine().toUpperCase();

            switch (option) {
                case "P":
                    printRecords(names, teeth, familySize);
                    break;

                case "E":
                    extractTooth(input, names, teeth, familySize);
                    break;

                case "R":
                    calculateRoots(teeth, familySize);
                    break;

                case "X":
                    System.out.println("Exiting the Floridian Tooth Records :-)");
                    break;

                default:
                    System.out.println("Invalid menu option, try again");
            }

        } while (!option.equals("X"));

        input.close();
    }


    /**
     * Displays every family member's dental records.
     *
     * @param names the family members' names
     * @param teeth the three-dimensional teeth array
     * @param familySize the number of family members
     */

    public static void printRecords(String[] names,
                                    char[][][] teeth,
                                    int familySize) {

        for (int index = 0; index < familySize; index++) {

            System.out.println(names[index]);

            for (int layer = 0; layer < 2; layer++) {

                if (layer == 0) {
                    System.out.print("  Uppers: ");
                } else {
                    System.out.print("  Lowers: ");
                }


                for (int toothIndex = 0; toothIndex < 8; toothIndex++) {
                    if (teeth[index][layer][toothIndex] != '\0') {
                        System.out.print((toothIndex + 1) + ":"
                                + teeth[index][layer][toothIndex] + " ");
                    }
                }

                System.out.println();
            }
        }

    }



    /**
     * Extracts a tooth by replacing its type with M.
     * Validates the family member, layer, and position.
     *
     * @param input scanner used to read user input
     * @param names the family members' names
     * @param teeth the three-dimensional teeth array
     * @param familySize the number of family members
     */

    public static void extractTooth(Scanner input, String[] names,
                                    char[][][] teeth, int familySize) {

        String member;
        int person = -1;

        // Find the family member
        do {
            System.out.print("Which family member: ");
            member = input.nextLine();

            for (int index = 0; index < familySize; index++) {
                if (names[index].equalsIgnoreCase(member)) {
                    person = index;
                    break;
                }
            }

            if (person == -1) {
                System.out.println("Invalid family member, try again");
            }

        } while (person == -1);

        // Select upper or lower teeth
        String choice;

        do {
            System.out.print("Which tooth layer (U)pper or (L)ower: ");
            choice = input.nextLine().toUpperCase();

            if (!choice.equals("U") && !choice.equals("L")) {
                System.out.println("Invalid layer, try again");
            }

        } while (!choice.equals("U") && !choice.equals("L"));

        int layer = choice.equals("U") ? 0 : 1;

        // Find how many positions are occupied
        int toothCount = 0;

        for (int index = 0; index < 8; index++) {
            if (teeth[person][layer][index] != '\0') {
                toothCount++;
            }
        }

        boolean hasTooth = false;

        for (int index = 0; index < toothCount; index++) {
            if (teeth[person][layer][index] != 'M') {
                hasTooth = true;
                break;
            }
        }

        if (!hasTooth) {
            System.out.println("No teeth available to extract");
            return;
        }



        // Ask which tooth to extract
        int toothNumber;


        do {
            System.out.print("Which tooth number: ");
            toothNumber = Integer.parseInt(input.nextLine());

            if (toothNumber < 1 || toothNumber > toothCount) {
                System.out.println("Invalid tooth number, try again");
            } else if (teeth[person][layer][toothNumber - 1] == 'M') {
                System.out.println("Missing tooth, try again");
            }

        } while (toothNumber < 1 || toothNumber > toothCount
                || (toothNumber >= 1 && toothNumber <= toothCount
                && teeth[person][layer][toothNumber - 1] == 'M'));


        // Extract the tooth
        teeth[person][layer][toothNumber - 1] = 'M';

        System.out.println("Tooth extracted successfully!");
    }


    /**
     * Counts the family's tooth types and calculates
     * the roots of the equation Ix^2 + Bx - M = 0.
     *
     * @param teeth the three-dimensional teeth array
     * @param familySize the number of family members
     */

    public static void calculateRoots(char[][][] teeth, int familySize) {

        int incisors = 0;
        int bicuspids = 0;
        int missing = 0;

        // Count all teeth in the family
        for (int index = 0; index < familySize; index++) {

            for (int layer = 0; layer < 2; layer++) {

                for (int toothIndex = 0; toothIndex < 8; toothIndex++) {

                    char tooth = teeth[index][layer][toothIndex];

                    if (tooth == 'I') {
                        incisors++;
                    } else if (tooth == 'B') {
                        bicuspids++;
                    } else if (tooth == 'M') {
                        missing++;
                    }
                }
            }
        }

        // Calculate the roots
        if (incisors == 0) {
            if (bicuspids == 0) {
                System.out.println("No root canal indices available");
            } else {
                double root = (double) missing / bicuspids;
                System.out.printf("One root canal at %.2f%n", root);
            }
        } else {
            double discriminant = bicuspids * bicuspids
                    + 4.0 * incisors * missing;

            double root1 = (-bicuspids + Math.sqrt(discriminant))
                    / (2.0 * incisors);

            double root2 = (-bicuspids - Math.sqrt(discriminant))
                    / (2.0 * incisors);

            System.out.printf("One root canal at %.2f%n", root1);
            System.out.printf("Another root canal at %.2f%n", root2);
        }
    }


}// end of DentalRecords class
