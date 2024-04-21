import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static ArrayList<String> myArrList = new ArrayList<>();
    private static Scanner in = new Scanner(System.in);
    private static boolean needsToBeSaved = false;
    private static String currentFileName = "";

    public static void main(String[] args) {
        boolean quit = false;

        do {
            showMyArrList();
            showMenu();
            String option = SafeInput.getRegExString(in, "Please choose an option (A/D/O/S/C/V/Q)","[AaDdOoSsCvVqQ]");
            switch (option.toUpperCase()) {
                case "A":
                    addItem();
                    break;
                case "D":
                    deleteItem();
                    break;
                case "O":
                    openList();
                    break;
                case "S":
                    saveList();
                    break;
                case "C":
                    clearList();
                    break;
                case "V":
                    viewList();
                    break;
                case "Q":
                    quit = quitProgram();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (!quit);

        in.close();
    }

    private static void showMenu() {
        SafeInput.prettyHeader("Menu Options");
        System.out.println("A – Add an item to the list");
        System.out.println("D – Delete an item from the list");
        System.out.println("O – Open a list file from disk");
        System.out.println("S – Save the current list file to disk");
        System.out.println("C – Clear the current list");
        System.out.println("V – View the current list");
        System.out.println("Q – Quit the program");
    }

    private static void showMyArrList() {
        if (myArrList.isEmpty()) {
            System.out.println("The list is empty.\n");
            return;
        }

        System.out.println("Current items:");
        printMyArrList(myArrList);
        System.out.println();
    }

    private static void addItem() {
        String item = SafeInput.getNonZeroLenString(in, "Please enter the item you would like to add");
        myArrList.add(item);
        needsToBeSaved = true;
        System.out.println("This item has been added successfully!");
    }

    private static void deleteItem() {
        if (myArrList.isEmpty()) {
            System.out.println("The list is currently empty.");
            return;
        }

        System.out.println("Current items:");
        printMyArrList(myArrList);

        int itemNumber = SafeInput.getRangedInt(in, "Please enter the number of the item you wish to delete from the list", 1, myArrList.size());
        String removedItem = myArrList.remove(itemNumber - 1);
        needsToBeSaved = true;
        System.out.println("Item '" + removedItem + "' deleted successfully!\n");
    }

    private static void openList() {
        if (needsToBeSaved) {
            if (!promptToSaveList()) {
                return;
            }
        }

        String filename = SafeInput.getRegExString(in, "Please enter the filename you would like to open", ".*\\.txt");

        try (Scanner fileScanner = new Scanner(new File(filename))) {
            myArrList.clear();
            while (fileScanner.hasNextLine()) {
                myArrList.add(fileScanner.nextLine());
            }
            needsToBeSaved = false;
            currentFileName = filename;
            System.out.println("The list has been loaded from the file successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    private static void saveList() {
        if (currentFileName.isEmpty()) {
            currentFileName = SafeInput.getRegExString(in, "Please enter the filename you wish to save", ".*\\.txt");
        }

        try (PrintWriter writer = new PrintWriter(currentFileName)) {
            for (String item : myArrList) {
                writer.println(item);
            }
            needsToBeSaved = false;
            System.out.println("List saved to file successfully!");
        } catch (IOException e) {
            System.out.println("Error saving the list to file.");
        }
    }

    private static void clearList() {
        myArrList.clear();
        needsToBeSaved = true;
        System.out.println("List cleared successfully!");
    }

    private static void viewList() {
        showMyArrList();
    }

    private static void printMyArrList(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". " + list.get(i));
        }
    }

    private static boolean quitProgram() {
        if (needsToBeSaved) {
            if (!promptToSaveList()) {
                return false;
            }
        }
        return SafeInput.getYNConfirm(in, "Are you sure you want to quit the program? (Y/N)");
    }

    private static boolean promptToSaveList() {
        boolean save = SafeInput.getYNConfirm(in, "Do you want to save the current list before proceeding? (Y/N)");
        if (save) {
            saveList();
        }
        return save;
    }
}
