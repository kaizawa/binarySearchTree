package com.cafeform.algorithm;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kaizawa
 */
public class BinarySearchTree {

    private Node rootNode;
    Integer[] initialValueArray = {5, 3, 6, 1, 4, 8, 14, 12, 18};

    public static void main(String[] args) {
        try {
            new BinarySearchTree().start();
        } catch (NoSuchNodeFoundException ex) {
            Logger.getLogger(BinarySearchTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void start() throws NoSuchNodeFoundException {
        createTree();

        int maxStep = 0;
        for (Integer searchVal : initialValueArray) {
            rootNode.resetStepCount();
            rootNode.search(searchVal);
            maxStep = Math.max(maxStep, rootNode.getStepCount());
        }
        System.out.println("Max steps to search: " + rootNode.getStepCount());

        /**
         * Before remove node
         */
        new NodeUtil(rootNode).printTree();        

        rootNode.remove(5);

        /**
         * after remove node
         */
        new NodeUtil(rootNode).printTree();
    }

    private void createTree() {
        rootNode = new Node(10);
        try {
            for (Integer val : initialValueArray) {
                rootNode.add(val);
            }
        } catch (NodeAlreadyExistException ex) {
            ex.printStackTrace();
        }
    }
}
