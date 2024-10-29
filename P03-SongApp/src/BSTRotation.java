///////////////////////// TOP OF FILE COMMENT BLOCK ////////////////////////////
//
// Title:           BST Rotation
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

public class BSTRotation <T extends Comparable<T>>
        extends BinarySearchTree<T>{
    protected BSTNode<T> root = super.root;

    /**
     * Performs the rotation operation on the provided nodes within this tree.
     *
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation.
     *
     * When the provided child is a right child of the provided parent, this method
     * will perform a left rotation.
     *
     * When the provided nodes are not related in one of these ways, this
     * method will either throw a NullPointerException: when either reference is
     * null, or otherwise will throw an IllegalArgumentException.
     *
     * @param child is the node being rotated from child to parent position
     * @param parent is the node being rotated from parent to child position
     * @throws NullPointerException when either passed argument is null
     * @throws IllegalArgumentException when the provided child and parent
     *     nodes are not initially (pre-rotation) related that way
     */
    protected void rotate(BSTNode<T> child, BSTNode<T> parent)
            throws NullPointerException, IllegalArgumentException {
        // TODO: Implement this method.
        if (parent == null || child == null ){
            throw new NullPointerException("Null node.");
        } else if (parent.getLeft() == child) { // child is a left child: right rotation
            rotateRight(parent);
        } else if (parent.getRight() == child) { // child is a right child: left rotation
            rotateLeft(parent);
        } else {
            throw new IllegalArgumentException("Parent and child are not related.");
        }
    }

    /**
     * Left rotation.
     *      p               pr
     *     / \             / \
     *   pl   pr  ==>     p   rr
     *       / \         / \
     *      rl  rr      pl  rl
     * No need to adjust: p-pl & pr-rr
     * Need to adjust:
     * 1.pr-rl => p-rl (change rl's parent)
     *      1) rl <- p.right
     *      2) p <- rl.parent
     * 2.rl-pr => rl-p: p has a parent/parents?
     *      1) Yes: pr.parent <- p.parent
     *          a) p has only one parent: done
     *          b) p has two parents:
     *              if p.parent.left == p
     *                  p.parent.left = pr
     *              else p.parent.right = pr
     *      2) No (p is the root): done
     * 3. p-pr => pr-p: p.parent = pr; pr.left = p
     * @param p The root of tree that need left rotation
     */
    private void rotateLeft (BSTNode<T> p){
        if (p != null) {
            // Get pr and rl
            BSTNode<T> pr = p.getRight();
            BSTNode<T> rl = (pr != null) ? pr.getLeft() : null;

            if (pr != null) {
                if (rl != null) {
                    // 1.Set rl's parent from pr to p
                    p.setRight(rl);
                    rl.setUp(p);
                } else {
                    p.setRight(null);
                }

                // 2.Set pr's parent to p's old parent: p has a parent?
                // It doesn't matter whether p has a parent. If not, pr's new parent
                // will be null. There will be no exception.
                pr.setUp(p.getUp());
                // If p was the root, now pr is the new root
                if (pr.getUp() == null) {
                    this.root = pr;
                } else {
                    if (p == p.getUp().getLeft()) { //p is the left child
                        p.getUp().setLeft(pr);
                    } else { // p has only one parent, or p is the right child
                        p.getUp().setRight(pr);
                    }
                }
            }

            // 3. Set p's parent as pr
            pr.setLeft(p);
            p.setUp(pr);
        }
    }

    /**
     * Right rotation.
     *      p               pl
     *     / \             / \
     *   pl   pr  ==>    ll    p
     *  / \                   / \
     * ll  lr               lr  pr
     * No need to adjust: p-pr & pl-ll
     * @param p The root of tree that need right rotation
     */
    private void rotateRight (BSTNode<T> p){
        if (p != null) {
            // Get pl and lr
            BSTNode<T> pl = p.getLeft();
            BSTNode<T> lr = (pl != null )? pl.getRight() : null;

            // 1.Sey lr's parent from pl to p
            if (pl != null) {
                if (lr != null) {
                    p.setLeft(lr);
                    lr.setUp(p);
                }
            } else {
                pl.setLeft(null);
            }

            // 2.Set pl's parent to p's old parent.
            // It doesn't matter whether p has a parent. If not, pr's new parent
            // will be null, but there will be no exception.
            pl.setUp(p.getUp());

            // If p was the root, now pr is the new root
            if (p.getUp() == null) {
                this.root = pl;
            } else {
                if (p == p.getUp().getLeft()) { //p is the left child
                    p.getUp().setLeft(pl);
                } else { // p has only one parent, or p is the right child
                    p.getUp().setRight(pl);
                }
            }
            // 3. Set p's parent as pr
            pl.setRight(p);
            p.setUp(pl);
        }
    }


}