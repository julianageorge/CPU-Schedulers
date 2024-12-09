package CPU.Schedulers;

import CPU.Process;
import CPU.duration;

import java.util.*;

public class FCAI {
    private ArrayList<Process> processes;
    private double V1, V2;
    private int contextSwitchTime;
    private int totalTime;
    private ArrayList<duration> durations;

    public FCAI(ArrayList<Process> processes , int contextSwitch) {
        this.processes = processes;
        this.contextSwitchTime=contextSwitch;
        calculateV1V2();
        executeSchedule();
    }

    private void calculateV1V2() {
        double lastArrivalTime = 0;
        int maxBurstTime = Integer.MIN_VALUE;
        for (Process p : processes) {
            lastArrivalTime = Math.max(lastArrivalTime, p.getArrivalTime());
            maxBurstTime = Math.max(maxBurstTime, p.getBurstTime());
        }

        V1 = lastArrivalTime / 10.0;
        V2 = maxBurstTime / 10.0;
    }
    private  Queue<Process> check( Queue<Process> pq){
        PriorityQueue<Process> temp = new PriorityQueue<>(Comparator.comparingInt(Process::getFcaiFactor)
                .thenComparingInt(Process::getArrivalTime));
        Queue<Process> queue;
        for (Process process : pq) {
            process.setFcaiFactor(calculateFCAIFactor(process));
            temp.add(process);
        }
        queue = temp;
        return queue;
    }

    private int calculateFCAIFactor(Process process) {
        double FcaiFactor = (10 - process.getPriorityNum()) + (process.getArrivalTime() / V1) + (process.getRemainingTime() / V2);
        return  (int)Math.ceil(FcaiFactor) ;
    }

    private void executeSchedule() {
        int currentTime = 0, completed = 0, totalWaitingTime = 0, totalTurnaroundTime = 0;
        ArrayList<String> executionOrder = new ArrayList<>();
        int n =processes.size();
        for (Process process : processes) {
            process.setRemainingTime(process.getBurstTime());
            process.setFcaiFactor(calculateFCAIFactor(process));
        }
        processes.sort(Comparator.comparingInt(Process::getArrivalTime)
                .thenComparingInt(Process::getPriorityNum));
        Queue<Process> queue = new LinkedList<>();
        ArrayList<Process> completedProcess = new ArrayList<>();
        Queue<Process> readyQueue ;
        Process shortestProcess = null , currentProcess = null;
        while (completedProcess.size()<n) {
            for (Process process : processes){
                if(process.getArrivalTime() <= currentTime && !queue.contains(process) && process.getRemainingTime() > 0){
                    queue.add(process);
                }
            }
            readyQueue = check(queue);
            if(!queue.isEmpty()){
                currentProcess = queue.poll();
                shortestProcess = readyQueue.peek();
//                if(currentProcess!=shortestProcess && shortestProcess!=null){
//                    currentTime += contextSwitchTime;
////                    currentProcess = shortestProcess;
//                }
//                else {
                    int timeToExecute = (int) Math.ceil(currentProcess.getQuantum() * 0.4);
                    executionOrder.add(currentProcess.getName());
                    if (timeToExecute > currentProcess.getRemainingTime()) {
                        currentTime += currentProcess.getRemainingTime();
                        currentProcess.setEndTime(currentTime);
                        currentProcess.setRemainingTime(0);
                        currentProcess.setRemainingQuantum(0);
                    }
                    else {
                        currentProcess.setRemainingTime(currentProcess.getRemainingTime() - timeToExecute);
                        currentTime += timeToExecute;
                        currentProcess.setRemainingQuantum(currentProcess.getQuantum() - timeToExecute);
                    }
                    if (currentProcess.getRemainingTime() == 0) {
                        completedProcess.add(currentProcess);
                        processes.remove(currentProcess);
                    }
                    while (currentProcess.getRemainingQuantum()>0) {
                        queue.add(currentProcess);
                        for (Process process : processes){
                            if(process.getArrivalTime() <= currentTime && !queue.contains(process) && process.getRemainingTime() > 0){
                                queue.add(process);
                            }
                        }
                        readyQueue = check(queue);
                        queue.remove(currentProcess);
                        shortestProcess = readyQueue.poll();
                        if (shortestProcess != currentProcess) {
                            queue.add(currentProcess);
                            currentProcess.setQuantum(currentProcess.getQuantum()+ currentProcess.getRemainingQuantum());
                            break;
                        } else {
                            currentTime++;
                            currentProcess.setRemainingQuantum(currentProcess.getRemainingQuantum() - 1);
                        }
                    }
                    if(currentProcess.getRemainingQuantum()==0){
                        if(currentProcess.getRemainingTime() > 0 ){
                        currentProcess.setQuantum(currentProcess.getQuantum()+2);
                        }
                        else {
                            completedProcess.add(currentProcess);
                            processes.remove(currentProcess);
                        }
                    }
//                }
//                if(currentProcess.getRemainingTime()>0){
//                    queue.add(currentProcess);
//                }else{
//                    processes.remove(currentProcess);
//                    completedProcess.add(currentProcess);
//                }
            }else {
                currentTime++;
            }
        }
        System.out.println("Processes Execution Order: " + String.join(" - ", executionOrder));

    }
    public ArrayList<duration> getduration() {
        return durations;
    }
}
