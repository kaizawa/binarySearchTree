package com.cafeform.algorithm;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Node {
    static AtomicInteger stepCount = new AtomicInteger();
    private Comparable value;
    private Node leftNode;
    private Node rightNode;
    private Node parent;

    private Node getParent() {
        return parent;
    }

    private void setParent(Node parent) {
        this.parent = parent;
    }
    
    public Node(Comparable value){
        this(value, null, null);
    }
    
    public Node(Comparable value, Node leftNode, Node rightNode){
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.value = value;
    }
    
    public void resetStepCount(){
        stepCount.set(0);
    }
    
    public Integer getStepCount(){
        return stepCount.get();
    }
    
    public Comparable getValue(){
        return value;
    }
    
    public void add(Comparable newValue) throws NodeAlreadyExistException{
        add(new Node(newValue));
    }
    
    public void add(Node newNode) throws NodeAlreadyExistException{
        stepCount.incrementAndGet();
        
        if(this == newNode){
            throw new NodeAlreadyExistException();
        }
        
        if(newNode.getValue().compareTo(value) < 0)
        {
            if(null == leftNode)
            {
                leftNode = newNode;
                newNode.setParent(this);
            } 
            else 
            {
                leftNode.add(newNode);
            }
        } 
        else 
        {
            if(null == rightNode)
            {
                rightNode = newNode;
                newNode.setParent(this);
            } 
            else 
            {
                rightNode.add(newNode);
            }
        }
    }
    
    public Node search(Comparable searchValue) throws NoSuchNodeFoundException {
        stepCount.incrementAndGet();
        if(searchValue == value)
        {
            return this;
        }
        
        if(searchValue.compareTo(value) < 0)
        {
            if(null == leftNode)
            {
                throw new NoSuchNodeFoundException();
            } 
            else 
            {
                return leftNode.search(searchValue);
            }
        } 
        else 
        {
            if(null == rightNode)
            {
                throw new NoSuchNodeFoundException();                
            } 
            else 
            {
                return rightNode.search(searchValue);
            }
        }        
    }
    
    public Node getLeftNode(){
        return leftNode;
    }
    
    public Node getRightNode(){
        return rightNode;
    }
  
    public void remove(Node node) throws NoSuchNodeFoundException{
        Node parentNode = node.getParent();
        /** 
         * No child.
         * Just unlink from parent.
         */
        if(null == node.leftNode && null == node.rightNode){
            parentNode.replaceChild(node, null);
            return;
        }
        /**
         * right child only
         */
        if(null == node.leftNode){
            parentNode.replaceChild(node, node.rightNode);
        } 
        /**
         * left child only
         */
        else if(null == node.rightNode)
        {
            parentNode.replaceChild(node, node.leftNode);
        }
        else
        /**
         * both
         */
        {
            /**
             * Search largest node in left tree
             */
            Node newBaseNode = getMostRightNode(node.leftNode);
            remove(newBaseNode);
            parentNode.replaceChild(node, newBaseNode);
            newBaseNode.leftNode = node.leftNode;
            newBaseNode.rightNode = node.rightNode;
        }
    }

    public void remove(Integer value) throws NoSuchNodeFoundException {
        Node node = search(value);
        remove(node);
    }

    private void replaceChild(Node oldChildNode, Node newChildNode) throws NoSuchNodeFoundException {
        if(oldChildNode == leftNode) {
            leftNode = newChildNode;
        } else if(oldChildNode == rightNode) {
            rightNode = newChildNode;
        } else {
            throw new NoSuchNodeFoundException();
        }
        
        if(null != newChildNode) {
            newChildNode.setParent(this);
        }        
    }

    private Node getMostRightNode(Node node) {
        if(null == node.rightNode){
            return node;
        } 
        return getMostRightNode(node.rightNode);
    }

    @Override
    public boolean equals(Object obj){
        if (false == (obj instanceof Node)){
            return false;
        }
        Node node = (Node)obj;
        return this.value == node.value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.value);
        hash = 41 * hash + Objects.hashCode(this.leftNode);
        hash = 41 * hash + Objects.hashCode(this.rightNode);
        hash = 41 * hash + Objects.hashCode(this.parent);
        return hash;
    }
}
