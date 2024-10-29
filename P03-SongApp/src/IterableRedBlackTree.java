///////////////////////// TOP OF FILE COMMENT BLOCK ////////////////////////////
//
// Title:           Iterable Red-Black Tree
// Course:          Fall 24 CS 400
//
// Author:          Kejun Liu
// Email:           kliu337@wisc.edu
// Lecturer's Name: GARY DAHL
//
/////////////////////////////// 80 COLUMNS WIDE ////////////////////////////////
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class extends RedBlackTree into a tree that supports iterating over the values it
 * stores in sorted, ascending order.
 */
public class IterableRedBlackTree<T extends Comparable<T>>
        extends RedBlackTree<T> implements IterableSortedCollection<T> {
    private Comparable<T> min;
    private Comparable<T> max;

    /**
     * Allows setting the start (minimum) value of the iterator. When this method is called,
     * every iterator created after it will use the minimum set by this method until this method
     * is called again to set a new minimum value.
     * @param min the minimum for iterators created for this tree, or null for no minimum
     */
    public void setIteratorMin(Comparable<T> min) {
        this.min = min;
    }

    /**
     * Allows setting the stop (maximum) value of the iterator. When this method is called,
     * every iterator created after it will use the maximum set by this method until this method
     * is called again to set a new maximum value.
     * @param max the maximum for iterators created for this tree, or null for no maximum
     */
    public void setIteratorMax(Comparable<T> max) {
        this.max = max;
    }

    /**
     * Returns an iterator over the values stored in this tree. The iterator uses the
     * start (minimum) value set by a previous call to setIteratorMin, and the stop (maximum)
     * value set by a previous call to setIteratorMax. If setIteratorMin has not been called
     * before, or if it was called with a null argument, the iterator uses no minimum value
     * and starts with the lowest value that exists in the tree. If setIteratorMax has not been
     * called before, or if it was called with a null argument, the iterator uses no maximum
     * value and finishes with the highest value that exists in the tree.
     */
    public Iterator<T> iterator() {
        return new RBTIterator<>(root, min, max);
    }

    /**
     * Nested class for Iterator objects created for this tree and returned by the iterator method.
     * This iterator follows an in-order traversal of the tree and returns the values in sorted,
     * ascending order.
     */
    protected static class RBTIterator<R> implements Iterator<R> {

        // stores the start point (minimum) for the iterator
        Comparable<R> min = null;
        // stores the stop point (maximum) for the iterator
        Comparable<R> max = null;
        // stores the stack that keeps track of the inorder traversal
        Stack<RBTNode<R>> stack = null;

        /**
         * Constructor for a new iterator if the tree with root as its root node, and
         * min as the start (minimum) value (or null if no start value) and max as the
         * stop (maximum) value (or null if no stop value) of the new iterator.
         * @param root root node of the tree to traverse
         * @param min the minimum value that the iterator will return
         * @param max the maximum value that the iterator will return
         */
        public RBTIterator(RBTNode<R> root, Comparable<R> min, Comparable<R> max) {
            this.min = min;
            this.max = max;
            this.stack = new Stack<>(); // Initialize the stack
            buildStackHelper(root);
        }

        /**
         * Helper method for initializing and updating the stack. This method both
         * - finds the next data value stored in the tree (or subtree) that is bigger
         *   than or equal to the specified start point (maximum), and
         * - builds up the stack of ancestor nodes that contain values larger than or
         *   equal to the start point so that those nodes can be visited in the future.
         * @param node the root node of the subtree to process
         */
        private void buildStackHelper(RBTNode<R> node) {
            if (node == null) return; // Base case: if the node is null, return

            // Traverse the right subtree first to ensure that the largest elements are pushed last
            buildStackHelper(node.getRight());

            // Check if the current node falls within the bounds and then push it onto the stack
            if ((min == null || min.compareTo(node.data) <= 0) &&
                    (max == null || max.compareTo(node.data) > 0)) {
                stack.push(node);
            }

            // Traverse the left subtree to ensure that smaller elements are processed later.
            buildStackHelper(node.getLeft());
        }


        /**
         * Returns true if the iterator has another value to return, and false otherwise.
         */
        @Override
        public boolean hasNext() {
            // Check if there are still elements in the stack
            return !stack.isEmpty();
        }

        /**
         * Returns the next value of the iterator.
         * @throws NoSuchElementException if the iterator has no more values to return
         */
        public R next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the iterator.");
            }

            RBTNode<R> currentNode = stack.pop(); // Get the next node
            R value = currentNode.data; // Get the value of the node

            // If there's a right subtree, build the stack for it
            buildStackHelper(currentNode.getRight());

            return value;
        }

    }

    ///////////// JUnit Test Methods /////////////

    /**
     * Test iterating over an integer tree with duplicates, including a specified start point.
     * Verifies that the iterator correctly returns the expected elements in order.
     */
//    @Test
//    public void testIntegerTree() {
//        IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
//
//        //Adding elements to the integer tree, including duplicates
//        tree.insert(10);
//        tree.insert(20);
//        //tree.insert(10); // duplicate, which is not allowed
//        tree.insert(30);
//        tree.insert(20); // duplicate
//        tree.insert(5);
//
//        // Set the iterator start point
//        tree.setIteratorMin(10);
//        Iterator<Integer> iterator = tree.iterator();
//
//        // Verify the expected output
//        assertTrue(iterator.hasNext());
//        assertEquals(10, iterator.next());
//        assertTrue(iterator.hasNext());
//        assertEquals(20, iterator.next());
//        assertTrue(iterator.hasNext());
//        assertEquals(30, iterator.next());
//        assertFalse(iterator.hasNext()); // No more elements should be left
//    }
//
//    /**
//     * Test iterating over a string tree without duplicates and using specified start and stop points.
//     * Confirms that the iterator returns the correct elements in the defined range.
//     */
//    @Test
//    public void testStringTree() {
//        IterableRedBlackTree<String> tree = new IterableRedBlackTree<>();
//
//        // Adding unique string elements to the string tree
//        tree.insert("apple");
//        tree.insert("banana");
//        tree.insert("cherry");
//        tree.insert("date");
//
//        // Set start and stop points for the iterator
//        tree.setIteratorMin("apple");
//        tree.setIteratorMax("date");
//        Iterator<String> iterator = tree.iterator();
//
//        // Verify the expected output
//        assertTrue(iterator.hasNext());
//        assertEquals("apple", iterator.next());
////        assertTrue(iterator.hasNext());
////        assertEquals("cherry", iterator.next());
//        assertFalse(iterator.hasNext()); // No more elements should be left
//    }
//
//    /**
//     * Test iterating over an integer tree without duplicates and without any specified start or stop points.
//     * Checks that the full range of elements is returned in sorted order.
//     */
//    @Test
//    public void testIntegerTreeWithoutDuplicatesWithoutBounds() {
//        IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
//
//        // Adding unique elements to the integer tree
//        tree.insert(15);
//        tree.insert(10);
//        tree.insert(20);
//        tree.insert(5);
//        tree.insert(25); // tree construction is correct
//
//        // Create iterator without min/max bounds
//        Iterator<Integer> iterator = tree.iterator();
//
//        // Verify the expected output
//        assertTrue(iterator.hasNext());
//        assertEquals(5, iterator.next());
//        assertTrue(iterator.hasNext());
//        assertEquals(10, iterator.next());
//        assertTrue(iterator.hasNext());
//        assertEquals(15, iterator.next());
//        assertTrue(iterator.hasNext());
//        assertEquals(20, iterator.next());
//        assertTrue(iterator.hasNext());
//        assertEquals(25, iterator.next());
//        assertTrue(iterator.hasNext());
//    }

}