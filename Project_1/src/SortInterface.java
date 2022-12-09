/*
 * Name: Wilson, Tyler
 * Date: 15NOV22
 * Course: CMSC 451
 * Professor: Dr Potolea, Rodica
 */
package src;

public interface SortInterface {
    void recursiveSort(int[] list) throws UnsortedException;
    void iterativeSort(int[] list) throws UnsortedException;
    int getCount();
    long getTime();
}
