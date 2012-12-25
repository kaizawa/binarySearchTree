package com.cafeform.algorithm;

/**
 *
 * @author kaizawa
 */
public class NodeUtil {
    private Integer[][] treeArray;
    private Node rootNode;
    private int depth;
    private int width;    

    NodeUtil(Node rootNode) {
        this.rootNode = rootNode;
        this.depth = getDepth();
        this.width = new Double(Math.pow(2.0, depth) * 4).intValue();        
    }
 
    public void printTree(){
        //System.out.println("Depth: " + depth + " Width: " + width);        
        treeArray = new Integer[depth+1][width];
        setLine(rootNode, 0, 0);

        int currentDepth = 0;

        for(Integer[] line: treeArray){
            printEdge(currentDepth);                        
            StringBuilder lineStr = new StringBuilder();
            int adjust = 0;                        
            int currentColumn = 0;
            for(Integer value : line){
                if(null == value || 0 == value)
                {
                    if(adjust > 0){
                        adjust--;
                    } else {
                        lineStr.append(" ");
                    }
                } 
                else 
                {
                    adjust = value.toString().length() - 1;
                    lineStr.append(String.format("%d", value));
                }
                currentColumn++;
            }
            System.out.println(lineStr.toString());
            currentDepth++;            
        }
    }
    
    private void printEdge(Integer currentDepth){
        if(0 == currentDepth){
            return;
        }
        
        int height = new Double(Math.pow(2, depth - currentDepth + 1)).intValue(); 
        int numTrees = new Double(Math.pow(2, currentDepth - 1)).intValue(); 
        //System.out.println("CurrentDepth:" + currentDepth + " height: " + height + " numTrees: " + numTrees);
        int subTreeWidth = width / numTrees;
        
        for(int h = 0 ; h < height ; h++){
            char[] line = new char[width];     
            for(int i = 0 ; i < width ; i ++){
                line[i] = ' ';
            }
            for(int t = 0 ; t < numTrees ; t++){
                int subTreeStart = subTreeWidth * t;
                int subTreeMiddle = subTreeStart + subTreeWidth / 2;
                //System.out.println("subTreeStart:" + subTreeStart + " subTreeWidth: " + subTreeWidth +  " (subTreeStart + subTreeWidth/4): "  + (subTreeStart + subTreeWidth/4));
                //System.out.println("treeArray[" + currentDepth + "][" + (subTreeStart + subTreeWidth/4) + "]: " + treeArray[currentDepth][subTreeStart + subTreeWidth/4]);
                
                /** 
                 * Draw left side edge if left node exists
                 */
                if(null != treeArray[currentDepth][subTreeStart + subTreeWidth/4] )
                {
                    line[subTreeMiddle - h - 1] = '/';
                }
                /**
                 * Draw right edge if right node exists
                 */ 
                if (null != treeArray[currentDepth][subTreeStart + subTreeWidth*3/4])
                {
                    line[subTreeMiddle + h] = '\\';
                }
            }
            System.out.println(new String(line));
        }
    }
    
    /**
     * Fill value of node 
     * @param node node to set the value
     * @param depth depth of node from root
     * @param path path from root node
     */
    private void setLine(Node node, Integer depth, Integer path){
        if(depth > this.depth){
            return;
        }
        int widthPosition = getWidthPosition(depth, path);
        
        treeArray[depth][widthPosition] = node.getValue();

        /**
         * path is bitmap of path
         * most left bit represents depth of 1. 
         * most right bit represents bottom.
         * 1 means left node. 0 means right node.
         * 
         *        0
         *       / \
         *      /   \
         *     0     1
         *    / \   / \
         *   00 01 10 11
         */
        if(null != node.getLeftNode()){
            setLine(node.getLeftNode(), depth + 1, path << 1);
        }
        
        if(null != node.getRightNode()){
            setLine(node.getRightNode(), depth + 1, path << 1 | 1 );
        }
    }    
    
    /**
     * Get position of node
     * @param depth depth of node from root node.
     * @param path path the node.
     * 
     * @return index number of width of treeArray.
     */
    private int getWidthPosition(Integer depth, Integer path) {
        int numColumns = new Double(Math.pow(2, depth)).intValue();
        int columnWidth = width / numColumns;
        int columnIndex = path;
        int widthPosition = columnWidth * columnIndex + columnWidth / 2;
        //System.out.println("depth = " + depth + " columns= " + numColumns);
        return widthPosition;
    }
    
    private Integer getDepth(){
        return doDepth(rootNode, 0);
    }

    private Integer doDepth(Node node, int depth) {
        int leftDepth = depth;
        int rightDepth = depth;
        
        if(null != node.getLeftNode())
        {
            leftDepth = doDepth(node.getLeftNode(), depth + 1);
        }
        
        if(null != node.getRightNode()) 
        {
            rightDepth = doDepth(node.getRightNode(), depth + 1);            
        }
        
        return Math.max(leftDepth, rightDepth);
    }    
    
}
