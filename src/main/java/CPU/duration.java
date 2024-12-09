package CPU;

public class duration {
   public String ProcessName;
    public int endTime;
    public  int StartTime;
    public int processArrivalTime;
    public int processBurstTime;
    public int id;
    public String color;
    public int remaingtime;
    public String status;
    public duration(String processName, int startTime, int endTime, int processId, String color,String status, int remainingTime, int arrivalTime) {
        this.ProcessName = processName;
        this.StartTime = startTime;
        this.endTime = endTime;
        this.id = processId;
        this.status = status;
        this.remaingtime = remainingTime;
        this.processArrivalTime = arrivalTime;
        this.color=color;
    }

    public String getColor() {
        return color;
    }

    public int getStartTime() {
        return StartTime;
    }

    public void setStartTime(int startTime) {
        StartTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }




    public String getProcessName() {
        return ProcessName;
    }
    @Override
    public String toString() {
        return String.format(
                "Process: %s, Start Time: %d, End Time: %d, Process ID: %d, Status: %s, Remaining Time: %d, Arrival Time: %d",
                ProcessName, StartTime, endTime, id, status,remaingtime , processArrivalTime
        );
    }
    public void setColor(String color) {
        this.color = color;
    }
}
