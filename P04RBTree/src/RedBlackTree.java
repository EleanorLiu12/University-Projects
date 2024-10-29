///////////////////////// TOP OF FILE COMMENT BLOCK ////////////////////////////
//
// Title:           Red Black Tree
// Course:          Fall 24 CS 400
//
// Author:          Kejun Liu
// Email:           kliu337@wisc.edu
// Lecturer's Name: GARY DAHL
//
///////////////////////////////// REFLECTION ///////////////////////////////////
// I do not know why the last test does not work. I have spent enough time on
// it and done my best. I have come to office hours, the whole afternoon, but
// failed to figure it out. I can't breathe.
//
/////////////////////////////// 80 COLUMNS WIDE ////////////////////////////////

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {
    protected RBTNode<T> root;

    public RedBlackTree() {
        root = null; // Initialize root to null
    }

    /**
     * Inserts a new value into the Red-Black Tree.
     * This method ensures that the tree maintains its Red-Black properties after the insertion.
     *
     * @param data the value to insert into the tree.
     */
    @Override
    public void insert(T data) throws NullPointerException {
        if (data == null) {
            throw new NullPointerException("Cannot insert null data");
        }

        RBTNode<T> newNode = new RBTNode<>(data);
        newNode.isRed = true;

        if (root == null) {
            root = newNode; // Set root to newNode if tree is empty
        } else {
            insertHelper(newNode, root); // Insert the newNode
            // Ensure the Red-Black properties are maintained after insertion
            ensureRedProperty(newNode);
        }

        root.isRed = false; // Ensure the root is always black
    }

    /**
     * Checks if a new red node in the RedBlackTree causes a red property violation
     * by having a red parent. If a red property violation is detected, this method repairs
     * the violation and any additional red property violations that may result from the repair.
     *
     * @param newNode a newly inserted red node, or a node turned red by previous repair
     */
    protected void ensureRedProperty(RBTNode<T> newNode) {
        RBTNode<T> parent = newNode.getUp();

        while (newNode != root && newNode.isRed() && parent.isRed()) {


            RBTNode<T> grandParent = parent.getUp();
            if (parent == grandParent.getLeft()) {
                RBTNode<T> uncle = grandParent.getRight();

                if (uncle != null && uncle.isRed()) {
                    // Case A-1: Uncle is red
                    grandParent.flipColor();
                    parent.flipColor();
                    uncle.flipColor();
                    newNode = grandParent; // Move up the tree for further checks
                } else {
                    // Case A-2: Uncle is black or null
                    if (newNode == parent.getRight()) { // Case A-2-1: newNode is right child
                        rotate(newNode, parent); // Left rotation on parent
                        rotate(newNode, grandParent);
                        newNode.flipColor(); // Parent becomes black
                        grandParent.flipColor(); // Grandparent becomes red
                    } else {
                        // Case A-2-2: newNode is left child
                        rotate(parent, grandParent); // Now rotate grandParent
                        parent.flipColor(); // Parent becomes black
                        grandParent.flipColor(); // Grandparent becomes red
                    }
                }
            } else { // Category B: parent is right child
                RBTNode<T> uncle = grandParent.getLeft();

                if (uncle != null && uncle.isRed()) {
                    // Case B-1: Uncle is red
                    grandParent.flipColor();
                    parent.flipColor();
                    uncle.flipColor();
                    newNode = grandParent; // Move up the tree for further checks
                } else {
                    // Case B-2: Uncle is black or null
                    if (newNode == parent.getLeft()) { // Case B-2-1: newNode is left child
                        rotate(newNode, parent); // Left rotation on parent
                        rotate(newNode, grandParent);
                        newNode.flipColor(); // Parent becomes black
                        grandParent.flipColor(); // Grandparent becomes red
                    } else {
                        rotate(parent, grandParent); // Now rotate grandParent
                        parent.flipColor(); // Parent becomes black
                        grandParent.flipColor(); // Grandparent becomes red
                    }

                }
            }
            parent = newNode.getUp(); // Update parent for the next iteration
            if (parent == null) root = newNode;
        }


    }

    @Test
    public void testInsertLeftRightRotation() {
        RedBlackTree<Integer> rbt = new RedBlackTree<>();
        rbt.insert(50); // Root node
        rbt.insert(30); // Insert left child
        rbt.insert(40); // Triggers left-right rotation

        assertEquals(40, rbt.root.getData(), "The root should be 40 after left-right rotation.");
        assertEquals(30, rbt.root.getLeft().getData(), "Left child of root should be 30.");
        assertEquals(50, rbt.root.getRight().getData(), "Right child of root should be 50.");
        assertFalse(rbt.root.isRed(), "Root should be black.");
        assertTrue(rbt.root.getLeft().isRed(), "30 should be red.");
        assertTrue(rbt.root.getRight().isRed(), "50 should be red.");
    }

    @Test
    public void testInsertSingleElement() {
        RedBlackTree<Integer> rbt = new RedBlackTree<>();
        rbt.insert(10); // Insert root node

        assertNotNull(rbt.root, "The root should not be null after insertion.");
        assertEquals(10, rbt.root.getData(), "The root should be 10.");
        assertFalse(rbt.root.isRed(), "The root node should be black.");
    }

    @Test
    public void testInsertCausingRecolor() {
        RedBlackTree<Integer> rbt = new RedBlackTree<>();
        rbt.insert(30); // Insert root node
        rbt.insert(25); // Insert a red node
        rbt.insert(35); // Insert another red node, causing a red-red violation

        assertNotNull(rbt.root, "The root should not be null after insertion.");
        assertEquals(30, rbt.root.getData(), "The root should still be 30.");
        assertFalse(rbt.root.isRed(), "The root should be black.");
        assertNotNull(rbt.root.getRight(), "Right child of root should not be null.");
        assertTrue(rbt.root.getRight().isRed(), "35 should be red.");
        assertNotNull(rbt.root.getLeft(), "Left child of root should not be null.");
        assertTrue(rbt.root.getLeft().isRed(), "25 should be red.");

        // Ensure red property
        rbt.ensureRedProperty(rbt.root);

        assertFalse(rbt.root.isRed(), "Root should be black after recoloring.");
        assertTrue(rbt.root.getRight().isRed(), "35 should be red after recoloring.");
        assertTrue(rbt.root.getLeft().isRed(), "25 should be red after recoloring.");
    }



    @Test
    public void testInsertRightLeftRotation() {
        RedBlackTree<Integer> rbt = new RedBlackTree<>();
        rbt.insert(30); // Root node
        rbt.insert(50); // Insert right child
        rbt.insert(40); // Triggers right-left rotation

        assertEquals(40, rbt.root.getData(), "The root should be 40 after right-left rotation.");
        assertEquals(30, rbt.root.getLeft().getData(), "Left child of root should be 30.");
        assertEquals(50, rbt.root.getRight().getData(), "Right child of root should be 50.");
        assertFalse(rbt.root.isRed(), "Root should be black.");
        assertTrue(rbt.root.getLeft().isRed(), "30 should be red.");
        assertTrue(rbt.root.getRight().isRed(), "50 should be red.");
    }
}
