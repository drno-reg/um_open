package UM.RU.DB.Datasets;

import java.io.Serializable;
import java.util.ArrayList;

//public class Metrics implements Serializable {
public class Metrics {

    private String UserId;
    private Integer MetricCount;
    private ArrayList<MetricList> MetricList;


    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public Integer getMetricCount() {
        return MetricCount;
    }

    public void setMetricCount(Integer metricCount) {
        MetricCount = metricCount;
    }

    public ArrayList<UM.RU.DB.Datasets.MetricList> getMetricList() {
        return MetricList;
    }

    public void setMetricList(ArrayList<UM.RU.DB.Datasets.MetricList> metricList) {
        MetricList = metricList;
    }
}
