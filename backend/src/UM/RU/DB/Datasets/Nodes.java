package UM.RU.DB.Datasets;

import java.util.ArrayList;

public class Nodes {

private String UserId;
private Integer NodeCount;
private ArrayList<NodeList> NodeList;


    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public Integer getNodeCount() {
        return NodeCount;
    }

    public void setNodeCount(Integer nodeCount) {
        NodeCount = nodeCount;
    }

    public ArrayList<UM.RU.DB.Datasets.NodeList> getNodeList() {
        return NodeList;
    }

    public void setNodeList(ArrayList<UM.RU.DB.Datasets.NodeList> nodeList) {
        NodeList = nodeList;
    }
}
