package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new SearchEngine().importDirectories();
    }
}

class SearchEngine {

    long linearSearch(ArrayList<String> dir, ArrayList<String> findDir) {
        long startTime = System.currentTimeMillis();

        int numberOfEntries = 0;
        int found = 0;
        for(String userToFind : findDir) {
            numberOfEntries++;
            for (String contact : dir) {
                if (contact.equals(userToFind)) {
                    found++;
                    break;
                }
            }
        }


        System.out.print("Found " + found + " / " + numberOfEntries + " entries ");
//        System.out.print("Time taken: " + convertTime(timeDiff));
        long timeDiff = (System.currentTimeMillis() - startTime);
        return timeDiff;
    }

    void importDirectories() {
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

        this.searchMenu(directory, findDir);


    }

    long actualJumpSearch(ArrayList<String> dir, ArrayList<String> findDir) {
        long searchStart = System.currentTimeMillis();
        int found = 0;
        int entries = 0;

        int jumpLength = (int) Math.sqrt(dir.size());
        int currentBlk = 0;
        int previousBlk = 0;


        for (String name : findDir) {
            while (currentBlk < dir.size()) {

                currentBlk = Math.min(currentBlk + jumpLength, dir.size());

                if (dir.get(currentBlk).charAt(0) >= name.charAt(0)) {//find possible block

                    for (int i = currentBlk; i > previousBlk; i--) {//do a backward search
                        if (dir.get(i).equals(name)) {
                            found++;
                            break;
                        }
                    }
                }

                previousBlk = currentBlk;
            }

        }

        System.out.print("Found " + found + "/ 500 entries.");
        return System.currentTimeMillis() - searchStart;

    }

    void jumpSearch (ArrayList<String> dir, ArrayList<String> findDir, long linearTime) {

        System.out.println("Start searching (bubble sort + jump search)...");
        long sortTime = this.sortDirectory(dir, linearTime);

        if (sortTime > 10 * linearTime) {
            long t = linearSearch(dir, findDir);
            long totalTime = t + sortTime;
            System.out.println("Time taken: " + convertTime(totalTime));
            System.out.print("Sorting Time: " + convertTime(sortTime) + ".");
            System.out.println("- STOPPED, moved to linear search");
            System.out.println("Searching time: " + convertTime(t));
            return;
        }

        long jumpTime = actualJumpSearch(dir, findDir);
        System.out.println("Time taken: " + convertTime(sortTime + jumpTime));
        System.out.print("Sorting Time: " + convertTime(sortTime) + ".");
        System.out.println("Searching time: " + convertTime(jumpTime));

    }

    long sortDirectory(ArrayList<String> dir, long linearTime) { //sort directory with bubble sort
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

            long sortMidway = System.currentTimeMillis() - sortStart; //check time range to terminate if necessary
            if (sortMidway > 100 * linearTime) {
                return sortMidway;

            }
        }

        return System.currentTimeMillis() - sortStart;
    }

    String convertTime(long time) {
        long min = (time / 1000) / 60;
        int sec = (int) (time / 1000) % 60;
        long milli = time - (sec + (min * 60 ))* 1000;
        return min + " min. " + sec + " sec. " + milli + " ms." ;
    }

    void searchMenu(ArrayList<String> dir, ArrayList<String> findDir ) {
        System.out.println("Start searching (linear search)...");
        long linearTime = linearSearch(dir, findDir);
        System.out.print("Time taken: " + convertTime(linearTime) + "\n");
        System.out.println("\n");

        jumpSearch(dir, findDir, linearTime);
    }
}