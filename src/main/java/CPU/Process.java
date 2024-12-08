package CPU;

public class Process implements Comparable<Process> {
   private String processName;
    private String processColor;
    private int processArrivalTime;
    private int processBurstTime;
    private int id;
    private int priorityNum;//
    private int quantumTime;// need in RR
    private int turnAroundTime;
    private int endTime;
    private int StartTime;
    private int remainingTime;
    private int waitingTime;

    public Process(String Name,int id,String Color,int arrivalTime,int BurstTime,int Priority){
        this.processName=Name;
        this.processColor=Color;
        this.processArrivalTime=arrivalTime;
        this.processBurstTime=BurstTime;
        this.priorityNum=Priority;
        this.id=id;
    }
    public Process(String Name,int id,String Color,int arrivalTime,int BurstTime,int Priority,int quantumTime){// if RR
        this.processName=Name;
        this.processColor=Color;
        this.processArrivalTime=arrivalTime;
        this.processBurstTime=BurstTime;
        this.quantumTime=quantumTime;
        this.priorityNum=Priority;
        this.id=id;
    }
    public Process() {
        setRemainingTime(processBurstTime);
    }
    @Override
    public int compareTo(Process process) {
        return Integer.compare(this.processArrivalTime, process.processArrivalTime);
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
    public int getRemainingTime() {
        return remainingTime;
    }

    public void setName(String Name) {
        this.processName = Name;
    }
    public String getName() {
        return processName;
    }
    public void setColor(String color) {
        this.processColor = color;
    }
    public String getColor() {
        return processColor;
    }
    public void setArrivalTime(int arrivalTime) {
        this.processArrivalTime = arrivalTime;
    }
    public int getArrivalTime() {
        return processArrivalTime;
    }
    public void setBurstTime(int burstTime) {
        this.processBurstTime = burstTime;
    }
    public int getBurstTime() {
        return processBurstTime;
    }
    public int getQuantum() {
        return quantumTime;
    }

    public void setQuantum(int quantum) {
        this.quantumTime = quantum;
    }
    public int getStartTime() {
        return StartTime;
    }

    public void setStartTime(int startTime) {
        this.StartTime = startTime;
    }
    public int getEndTime(){
        return endTime;
    }
    public void setEndTime (int endTime){
        this.endTime=endTime;
    }
    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getPriorityNum(){
        return priorityNum;
    }
    public void setPriorityNum(int priorityNum){
        this.priorityNum=priorityNum;
    }
    public int getTurnAroundTime(){
        return turnAroundTime;
    }
    public void setTurnAroundTime(int AroundTime){
        this.turnAroundTime=AroundTime;
    }


    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
