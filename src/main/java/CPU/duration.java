package CPU;

public class duration {
   public String ProcessName;
    public int endTime;
    public  int StartTime;
    public int processArrivalTime;
    public int processBurstTime;
    public int priority;
    public int id;
    public String color;
    public int remaingtime;
    public String status;
    private String preemptedBy;
    private int Quantumm;
    private int fcaifactor;
    public duration(String processName, int startTime, int endTime, int processId, String color,String status, int remainingTime, int arrivalTime,String preemptedBy) {
        this.ProcessName = processName;
        this.StartTime = startTime;
        this.endTime = endTime;
        this.id = processId;
        this.status = status;
        this.remaingtime = remainingTime;
        this.processArrivalTime = arrivalTime;
        this.color=color;
        this.preemptedBy=preemptedBy;

    }
    public duration(String processName, int startTime, int endTime, int processId, String color,String status, int remainingTime, int arrivalTime,String preemptedBy,int Quantum,int priority,int fcaifactor) {
        this.ProcessName = processName;
        this.StartTime = startTime;
        this.endTime = endTime;
        this.id = processId;
        this.status = status;
        this.remaingtime = remainingTime;
        this.processArrivalTime = arrivalTime;
        this.color=color;
        this.preemptedBy=preemptedBy;
        this.priority=priority;
        this.Quantumm=Quantum;
        this.fcaifactor=fcaifactor;

    }
    public String getPreemptedBy() {
        return preemptedBy;
    }

    public void setPreemptedBy(String preemptedBy) {
        this.preemptedBy = preemptedBy;
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

    public int getPriority() {
        return priority;
    }

    public String getProcessName() {
        return ProcessName;
    }
    @Override
    public String toString() {
        String preemptStatus = (preemptedBy == null) ? "" : "Preempted by: " + preemptedBy + " ";
        return String.format(
                "Process: %s, Start Time: %d, End Time: %d, Process ID: %d, Status: %s, Remaining Time: %d, Arrival Time: %d %s",
                ProcessName, StartTime, endTime, id, status, remaingtime, processArrivalTime, preemptStatus
        );
    }
    public void setColor(String color) {
        this.color = color;
    }

    public int getRemaingtime() {
        return remaingtime;
    }

    public int getQuantum() {

        return Quantumm;
    }

    public int getFcaiFactor() {
        return fcaifactor;
    }
}
