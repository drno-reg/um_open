package UM.RU.DB.Datasets;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable {

    private String tokenId;
    private Integer nodeCount;
    private ArrayList<NodeList> nodeList;


    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Integer getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
    }

    public ArrayList<NodeList> getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList<NodeList> nodeList) {
        this.nodeList = nodeList;
    }
}
