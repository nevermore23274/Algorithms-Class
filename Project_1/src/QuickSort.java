/*
 * Name: Wilson, Tyler
 * Date: 15NOV22
 * Course: CMSC 451
 * Professor: Dr Potolea, Rodica
 */
// Import(s)
package src;
import java.util.ArrayList;
import java.util.Collections;

public class QuickSort implements SortInterface {
    // Fields
    private int count;
    private long time;
    
    // Default constructor
    public QuickSort()
    {
    }
    
    public int getCount() {
        return this.count;
    }
    
    public long getTime() {
        return this.time;
    }
    
    public void recursiveSort(int[] list) throws UnsortedException {
        this.time = 0;
        this.count = 0;
        ArrayList<Integer> ls = new ArrayList<Integer>();
        for (int i = 0; i < list.length; i++) {
            ls.add(list[i]);
        }
        
        long startTime = System.nanoTime();
        quickRecursive(ls, 0, ls.size() - 1);
        long endTime = System.nanoTime();
        this.time = endTime - startTime;
        check(ls, "recursive");
    }

    // Iterative sorting
    public void iterativeSort(int[] list) throws UnsortedException {
        this.time = 0;
        this.count = 0;
        ArrayList<Integer> ls = new ArrayList<Integer>();
        for (int i = 0; i < list.length; i++) {
            ls.add(list[i]);
        }
    
        long startTime = System.nanoTime();
        quickIterative(ls, 0, ls.size() - 1);
        long endTime = System.nanoTime();
        this.time = endTime - startTime;
    
        //check correctness
        check(ls, "iterative");
    }

    // See https://stackoverflow.com/questions/17500391/determines-if-the-array-list-is-sorted
    public void check(ArrayList<Integer> ls, String sortType) {
        for (int i = 0; i < (ls.size() - 1); i++) {
            if (ls.get(i) > ls.get(i + 1)) {
                try {
                    throw new UnsortedException("Custom exception: " + sortType +
                    " sort produced wrong result");
                } catch (UnsortedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // See pseudocode from Chapter 7.1 (page 171) of Intro to Algorithms by Cormen, also see "references" in README
    private void quickRecursive(ArrayList<Integer> ls, int left, int right) {
        if (left >= right) { 
            count++; // CRITICAL operation
            return;
        }
        else {
            int pivot = partition(ls, left, right);
            quickRecursive(ls, left, pivot - 1);
            quickRecursive(ls, pivot + 1, right);
        }
    }

    private void quickIterative(ArrayList<Integer> ls, int left, int right) {
        int[] temp_stack = new int[right - left + 1];
        int last = -1;
        temp_stack[++last] = left;
        temp_stack[++last] = right;
        while (last > -1) { 
            count++; // CRITICAL operation
            int r = temp_stack[last--];
            int l = temp_stack[last--];
            int pivot = partition(ls, l, r);
            // if elements to the left of pivot, add to temp_stack
            // CRITICAL operation
            if (pivot - 1 > l) { 
                count++;
                temp_stack[++last] = l;
                temp_stack[++last] = pivot - 1;
            }
        
            // if elements to the right of pivot, add to temp_stack
            // CRITICAL operation
            if (pivot + 1 < r) {
                count++;
                temp_stack[++last] = pivot + 1;
                temp_stack[++last] = r;
            }
        }
    }

    // Recursive Quicksort
    private int partition(ArrayList<Integer> ls, int left, int right) {
        int value = ls.get(right);
        int i = left - 1;
        // CRITICAL operation
        for (int j = left; j < right; j++) {
            if (ls.get(j) <= value) { 
                count++;
                i++;
                Collections.swap(ls, j, i);
            }
        }
    
        Collections.swap(ls, i + 1, right);
        return i + 1;
    }

    public void verifySort(int[] list) {
    }
}
