package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new LinearFind().readFile();
    }
}

class LinearFind { //Linear search for contacts.
    void searchInFile(ArrayList<String> dir, ArrayList<String> findDir) {
        int numberOfEntries = 0;
        int found = 0;
        System.out.println("Start searching...");
        long startTime = System.currentTimeMillis();

        for(String userToFind : findDir) {
            numberOfEntries++;
            for (String contact : dir) {
                if (contact.contains(userToFind)) {
                    found++;
                    break;
                }
            }
        }
        long timeDiff = (System.currentTimeMillis() - startTime);

        System.out.print("Found " + found + " / " + numberOfEntries + " entries ");
        System.out.print("Time taken: " + convertTime(timeDiff));
    }

    void readFile() {	//load file to memory
        File dirFile = new File("directory.txt");
        File findFile = new File("find.txt");

        ArrayList<String> directory = new ArrayList<>();
        ArrayList<String> findDir = new ArrayList<>();

        try (Scanner scanDir = new Scanner(dirFile)) {
            while(scanDir.hasNext()) {

                directory.add(scanDir.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try (Scanner scanFindFile = new Scanner(findFile)) {
            while(scanFindFile.hasNext()) {

                findDir.add(scanFindFile.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        this.searchInFile(directory, findDir);

    }

    String convertTime(long time) { //convert time from milliseconds
        long min = (time / 1000) / 60;
        int sec = (int) (time / 1000) % 60;
        long milli = time - (sec + (min * 60 ))* 1000;
        return min + " min. " + sec + " sec. " + milli + " ms." ;
    }
}