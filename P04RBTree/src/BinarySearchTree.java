///////////////////////// TOP OF FILE COMMENT BLOCK ////////////////////////////
//
// Title:           Binary Search Tree
// Course:          Fall 24 CS 400
//
// Author:          Kejun Liu
// Email:           kliu337@wisc.edu
// Lecturer's Name: GARY DAHL
//
///////////////////////////////// CITATIONS ////////////////////////////////////
//
// None
//
///////////////////////////////// REFLECTION ///////////////////////////////////
//
// N/A
//
/////////////////////////////// 80 COLUMNS WIDE ////////////////////////////////

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a Binary Search Tree (BST) that implements the SortedCollection interface.
 * It uses a bounded generic type parameter and the provided BSTNode class for its nodes.
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {

    // Reference to the root node of the BST
    protected BSTNode<T> root = null;

    @Override
    public void insert(T data) throws NullPointerException {
        if (data == null) {
            throw new NullPointerException("Cannot insert null data");
        }
        BSTNode<T> newNode = new BSTNode<>(data);
        if (root == null) {
            root = newNode;
        } else {
            insertHelper(newNode, root);
        }
    }
    /**
     * Performs the naive binary search tree insert algorithm to recursively insert
     * the provided newNode into the provided subtree.
     * @param newNode the node to insert
     * @param subtree the subtree to insert into
     */
    protected void insertHelper(BSTNode<T> newNode, BSTNode<T> subtree) {
        int comparison = newNode.getData().compareTo(subtree.getData());

        if (comparison < 0) { // newNode is smaller
            if (subtree.getLeft() == null) { // if subtree does not have a left child
                subtree.setLeft(newNode); // set newNode as the left child
                newNode.setUp(subtree); // set subtree as newNode's parent
            } else {
                insertHelper(newNode, subtree.getLeft()); // continue to search left subtree
            }
        } else if (comparison > 0) { // newNode is greater
            if (subtree.getRight() == null) { // if subtree does not have a right child
                subtree.setRight(newNode); // set newNode as the right child
                newNode.setUp(subtree); // set subtree as newNode's parent
            } else {
                insertHelper(newNode, subtree.getRight()); // continue to search right subtree
            }
        }
        // If comparison == 0, we do nothing since duplicates are not allowed
    }

    @Override
    public boolean contains(Comparable<T> data) {
        return containsHelper(data, root);
    }

    private boolean containsHelper(Comparable<T> data, BSTNode<T> subtree) {
        if (subtree == null) {
            return false;
        }
        int compared = data.compareTo(subtree.getData());
        if (compared < 0) {
            return containsHelper(data, subtree.getLeft());
        } else if (compared > 0) {
            return containsHelper(data, subtree.getRight());
        } else {
            return true;
        }
    }

    @Override
    public int size() {
        return sizeHelper(root);
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public void clear() {
        root = null;
    }

    private int sizeHelper(BSTNode<T> subtree) {
        if (subtree == null) {
            return 0;
        }
        return 1 + sizeHelper(subtree.getLeft()) + sizeHelper(subtree.getRight());
    }

    // Test methods
    public static boolean test1() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(2);
        tree.insert(7);
        return tree.size() == 5 && tree.contains(7) && !tree.contains(20);
    }

    public static boolean test2() {
        BinarySearchTree<String> tree = new BinarySearchTree<>();
        tree.insert("apple");
        tree.insert("banana");
        tree.insert("cherry");
        tree.insert("date");
        tree.insert("fig");
        return tree.size() == 5 && tree.contains("banana") && !tree.contains("grape");
    }

    public static boolean test3() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(10);
        tree.insert(5);
        tree.clear();
        return tree.isEmpty();
    }

    public static void main(String[] args) {
        System.out.println("Test 1: " + (test1() ? "Passed" : "Failed"));
        System.out.println("Test 2: " + (test2() ? "Passed" : "Failed"));
        System.out.println("Test 3: " + (test3() ? "Passed" : "Failed"));
    }
}
