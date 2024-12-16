class GanttChartEntry {
    String processName;
    int startTime;
    int endTime;

    public GanttChartEntry(String processName, int startTime, int endTime) {
        this.processName = processName;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}