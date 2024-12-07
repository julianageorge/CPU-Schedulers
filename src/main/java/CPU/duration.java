package CPU;

public class duration {
   public String ProcessName;
    public int endTime;
    public  int StartTime;
    public int processArrivalTime;
    public int processBurstTime;
    public int id;
    public String status;
    public duration(String name, int start, int end, int id, String status , int burstTime , int arrivalTime) {
        this.ProcessName = name;
        this.StartTime = start;
        this.endTime = end;
        this.id = id;
        this.status = status;
        this.processArrivalTime = arrivalTime ;
        this.processBurstTime = burstTime;
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
}
