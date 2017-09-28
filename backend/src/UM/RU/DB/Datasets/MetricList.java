package UM.RU.DB.Datasets;

public class MetricList {

    private Integer MetricId;
    private String MetricName;
    private String MetricStatus;

    public Integer getMetricId() {
        return MetricId;
    }

    public void setMetricId(Integer metricId) {
        MetricId = metricId;
    }

    public String getMetricName() {
        return MetricName;
    }

    public void setMetricName(String metricName) {
        MetricName = metricName;
    }

    public String getMetricStatus() {
        return MetricStatus;
    }

    public void setMetricStatus(String metricStatus) {
        MetricStatus = metricStatus;
    }
}
