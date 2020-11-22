package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new SearchEngine().importDirectories();
    }
}

class SearchEngine {

    LinearSearch linear = new LinearSearch();

    JumpSearch jump = new JumpSearch();

    BinarySearch binary = new BinarySearch();

    InstantSearch instant = new InstantSearch();


    void searchMenu(ArrayList<String> dir, ArrayList<String> findDir, HashMap<String, Long> phoneDir) {
        System.out.println("Start searching (linear search)...");
        long linearTime = linear.linearSearch(dir, findDir);
        System.out.print("Time taken: " + convertTime(linearTime) + "\n");
        System.out.println();

        jump.jumpSearch(dir, findDir, linearTime);
        System.out.println();

        System.out.println("Start searching (quick sort + binary search)...");
        binary.binarySearch(dir, findDir);
        System.out.println();

        System.out.println("Start searching (hash table)...");
        long tableCreationTime = instant.createTable(phoneDir);
        long instantSearchingTime = instant.instantSearch(findDir);
        System.out.println("Time taken: " + convertTime(tableCreationTime + instantSearchingTime));
        System.out.println("Creating time: " + convertTime(tableCreationTime));
        System.out.println("Searching time: " + convertTime(instantSearchingTime));

    }

    void importDirectories() {
        File dirFile = new File("C:\\Users\\abosede\\Downloads\\directory.txt");
        File findFile = new File("C:\\Users\\abosede\\Downloads\\find.txt");

        HashMap<String, Long> phoneDirectory = new HashMap<>();
        ArrayList<String> directory = new ArrayList<>();
        ArrayList<String> findDir = new ArrayList<>();

        try (Scanner scanDir = new Scanner(dirFile)) {
            while(scanDir.hasNext()) {
                String s = scanDir.nextLine();
                String[] arry = s.split(" ");
                directory.add(arry[1]);
                long number = Long.parseLong(arry[0]);
                phoneDirectory.put(arry[1], number);
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

        this.searchMenu(directory, findDir, phoneDirectory);
    }

    String convertTime(long time) {
        long min = (time / 1000) / 60;
        int sec = (int) (time / 1000) % 60;
        long milli = time - (sec + (min * 60 ))* 1000;
        return min + " min. " + sec + " sec. " + milli + " ms." ;
    }
    
}

class LinearSearch {

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


        System.out.print("Found " + found + "/" + numberOfEntries + " entries ");

        return  System.currentTimeMillis() - startTime;
    }
}

class JumpSearch {

    long jumpSearch(ArrayList<String> dir, ArrayList<String> findDir) {
        long searchStart = System.currentTimeMillis();
        int totalEntries = 0;
        int found = 0;

        int jumpLength = (int) Math.sqrt(dir.size());
        int currentBlk = 0;
        int previousBlk = 0;


        for (String name : findDir) {
            totalEntries++;

            while (currentBlk < dir.size() - 1) {

                currentBlk = Math.min(currentBlk + jumpLength, dir.size() - 1);

                if (dir.get(currentBlk).compareTo(name) >= 0) {//find possible block

                    for (int i = currentBlk; i > previousBlk; i--) {  //do a backward search
                        if (dir.get(i).equals(name)) {
                            found++;
                            break;
                        }
                    }
                }

                previousBlk = currentBlk;
            }

        }

        System.out.print("Found " + found + "/" +totalEntries + " entries.");
        return System.currentTimeMillis() - searchStart;

    }

    void jumpSearch (ArrayList<String> dir, ArrayList<String> findDir, long linearTime) {

        System.out.println("Start searching (bubble sort + jump search)...");
        long sortTime = this.bubbleSort(dir, linearTime);

        if (sortTime > 10 * linearTime) {
            LinearSearch linear = new LinearSearch();
            long t = linear.linearSearch(dir, findDir);
            long totalTime = t + sortTime;
            System.out.println("Time taken: " + convertTime(totalTime));
            System.out.print("Sorting Time: " + convertTime(sortTime) + ".");
            System.out.println("- STOPPED, moved to linear search");
            System.out.println("Searching time: " + convertTime(t));
            return;
        }

        long jumpTime = jumpSearch(dir, findDir);
        System.out.println("Time taken: " + convertTime(sortTime + jumpTime));
        System.out.print("Sorting Time: " + convertTime(sortTime) + ".");
        System.out.println("Searching time: " + convertTime(jumpTime));

    }

    long bubbleSort(ArrayList<String> dir, long linearTime) { //sort directory with bubble sort
        long sortStart = System.currentTimeMillis();

        for (int i = 0; i < dir.size() - 1; i++) {
            for(int j = 0; j < dir.size() - i - 1; j++) {

                if (dir.get(j).compareTo(dir.get(j + 1)) > 0) {
                    String temp = dir.get(j + 1);
                    dir.set(j + 1,  dir.get(j));
                    dir.set(j, temp);
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

}

class BinarySearch {

    void binarySearch(ArrayList<String> dir, ArrayList<String> findDir) {

        long quickSortTime = this.quickSortMenu(dir);

        long binarySearchStart = System.currentTimeMillis();
        int found = 0;
        for (String name: findDir) {

            if (this.binarySearch(dir, name, 0, dir.size() - 1)) {
                found++;
            }
        }
        long binarySearchTime = System.currentTimeMillis() - binarySearchStart;

        System.out.print("Found " + found + "/" + findDir.size() + " entries.");
        System.out.println("Time taken: " + convertTime(binarySearchTime + quickSortTime));
        System.out.println("Sorting time: " + convertTime(quickSortTime));
        System.out.println("Searching time: " + convertTime(binarySearchTime));

    }

    boolean binarySearch(ArrayList<String> dir, String target, int left, int right) {

        while(left <= right) {

            int mid = left + (right - left) / 2;

            if (dir.get(mid).equals(target)) {
                return true;
            } else if (target.compareTo(dir.get(mid)) < 0) {
                right = mid - 1;
            } else if (target.compareTo(dir.get(mid)) > 0) {
                left = mid + 1;
            }
        }

        return false;
    }

    long quickSortMenu(ArrayList<String> dir) {

        long sortStart = System.currentTimeMillis();

        quickSort(dir, 0, dir.size() - 1);

        return System.currentTimeMillis() - sortStart;
    }

    void quickSort(ArrayList<String> dir, int left, int right) {

        if (left < right) {
            int partitionIndex = partition(dir, left, right);
            quickSort(dir, left, partitionIndex - 1);
            quickSort(dir, partitionIndex + 1, right);
        }

    }

    int partition(ArrayList<String> dir, int left, int right) {

        String pivot = dir.get(right);
        int partitionIndex = left;

        for (int i = left; i < right; i++) {
            String ch = dir.get(i);
            if (ch.compareTo(pivot) <= 0) {
                swap(dir, i, partitionIndex);
                partitionIndex++;
            }
        }

        swap(dir, partitionIndex, right);

        return partitionIndex;
    }

    void swap(ArrayList<String> dir, int i, int j) {
        String temp = dir.get(i);
        dir.set(i, dir.get(j));
        dir.set(j, temp);
    }

    String convertTime(long time) {
        long min = (time / 1000) / 60;
        int sec = (int) (time / 1000) % 60;
        long milli = time - (sec + (min * 60 ))* 1000;
        return min + " min. " + sec + " sec. " + milli + " ms." ;
    }
}



class InstantSearch {

    HashTable<Long> contacts = new HashTable<>(100);

    long createTable(HashMap<String, Long> dir) {

        long createStart = System.currentTimeMillis();

        for(HashMap.Entry<String, Long> entry : dir.entrySet()) {
            int key = Math.abs(entry.getKey().hashCode());
            contacts.put(key, entry.getValue());
        }
        return System.currentTimeMillis() - createStart;
    }

    long instantSearch(ArrayList<String> findDir) {
        int found = 0;
        int totalEntries = 0;
        long searchStart = System.currentTimeMillis();

        for (String name : findDir) {
            totalEntries++;
            int key = Math.abs(name.hashCode());
            if (contacts.get(key) != null) {

                found++;
            }
        }

        System.out.print("Found " + found + "/" + totalEntries + " entries.");

        return System.currentTimeMillis() - searchStart;
    }

}

class HashTable<T> {
    private int size;
    private TableEntry[] table;

    public HashTable(int size) {
        this.size = size;
        table = new TableEntry[size];
    }

    private int findEntryIndex(int key) {
        int hash = key % size;
        while(!(table[hash] == null || table[hash].getKey() == key)) {

            hash = (hash + 1) % size;

            if (hash == key % size) {
                return  -1;
            }
        }

        return hash;
    }

    public boolean put(int key, T value) {

        int idx = findEntryIndex(key);

        if (idx == -1) {
            rehash();
            idx = findEntryIndex(key);
        }

        table[idx] = new TableEntry(key, value);
        return true;
    }

    public T get(int key) {

        int idx = findEntryIndex(key);

        if (idx == -1 || table[idx] == null) {
            return null;
        }

        return (T) table[idx].getValue();
    }

    private void rehash() {
        size = size * 2;
        TableEntry[] oldTable = table;
        table = new TableEntry[size];

        for (TableEntry t : oldTable) {
            int idx = findEntryIndex(t.getKey());

            table[idx] = t;
        }

    }

}

class TableEntry<T> {

    private final int key;
    private final T value;

    public TableEntry(int key, T value) {

        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }
}