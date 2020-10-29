package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new SearchEngine().readFile();
    }
}

class SearchEngine {

    void linearSearch(ArrayList<String> dir, ArrayList<String> findDir) {
        int numberOfEntries = 0;
        int found = 0;
        System.out.println("Start searching (linear search)...");
        long startTime = System.currentTimeMillis();

        for(String userToFind : findDir) {
            numberOfEntries++;
            for (String contact : dir) {
                if (contact.equals(userToFind)) {
                    found++;
                    break;
                }
            }
        }
        long timeDiff = (System.currentTimeMillis() - startTime);

        System.out.print("Found " + found + " / " + numberOfEntries + " entries ");
        System.out.print("Time taken: " + convertTime(timeDiff));
    }

    void readFile() {
        File dirFile = new File("C:\\Users\\abosede\\Downloads\\directory.txt");
        File findFile = new File("C:\\Users\\abosede\\Downloads\\find.txt");

        ArrayList<String> directory = new ArrayList<>();
        ArrayList<String> findDir = new ArrayList<>();

        try (Scanner scanDir = new Scanner(dirFile)) {
            while(scanDir.hasNext()) {
                String s = scanDir.nextLine();
                String[] arry = s.split(" ");
                directory.add(arry[1]);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try (Scanner scanFindFile = new Scanner(findFile)) {
            while(scanFindFile.hasNext()) {
                String s = scanFindFile.nextLine();
                String[] arry = s.split(" ");
                findDir.add(arry[0]);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        this.linearSearch(directory, findDir);
        this.jumpSearch(directory, findDir);

    }

    void jumpSearch (ArrayList<String> dir, ArrayList<String> findDir) {
        int found = 0;
        int entries = 0;
        System.out.println("Start searching (bubble sort + jump search)...");
        this.sortDirectory(dir);
        System.out.println(dir.get(0));

    }

    long sortDirectory(ArrayList<String> dir) {
        String[] arry = dir.toArray(new String[0]);
        long sortStart = System.currentTimeMillis();

        for (int i = 0; i < arry.length - 1; i++) {
            for(int j = 0; j < arry.length - 1 - i; j++) {

                if (arry[j].charAt(0) > arry[j + 1].charAt(0)) {
                    String temp = arry[j + 1];
                    arry[j + 1] = arry[j];
                    arry[j] = temp;
                }
            }
        }
        long sortEnd = System.currentTimeMillis() - sortStart;
        return sortEnd;
    }

    String convertTime(long time) {
        long min = (time / 1000) / 60;
        int sec = (int) (time / 1000) % 60;
        long milli = time - (sec + (min * 60 ))* 1000;
        return min + " min. " + sec + " sec. " + milli + " ms." ;
    }
}