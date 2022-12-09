/*
 * Name: Wilson, Tyler
 * Date: 15NOV22
 * Course: CMSC 451
 * Professor: Dr Potolea, Rodica
 */
package src;

public class UnsortedException extends Exception {
    public UnsortedException(String errorMessage) {
        super("Array is not sorted in ascending order. " + errorMessage);
    }
}
