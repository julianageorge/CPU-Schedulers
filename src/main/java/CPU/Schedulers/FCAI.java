package CPU.Schedulers;

import CPU.Process;
import java.util.*;

public class FCAI {
    private ArrayList<Process> processes;
    private double V1, V2;
    private int contextSwitchTime;
    private int totalTime;
    private boolean nextInArival = false , nextInFcai = false;

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
        double FcaiFactor = (10 - process.getPriorityNum()) + (int)Math.ceil(process.getArrivalTime() / V1) + (int)Math.ceil(process.getRemainingTime() / V2);
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
        ArrayList<Process> underProcess = new ArrayList<>();
        Queue<Process> readyQueue ;
        Process shortestProcess = null , currentProcess = null;
        while (completedProcess.size()<n) {
            for (Process process : processes){
                if(process.getArrivalTime() <= currentTime && !queue.contains(process) && process.getRemainingTime() > 0){
                    queue.add(process);
                }
            }
//            readyQueue = check(queue);
            if(!queue.isEmpty()){
                if(!nextInFcai && !nextInArival) {
                    currentProcess = queue.poll();
                }else {
                    if(nextInFcai) {
                        currentProcess = shortestProcess;
                        nextInFcai = false;
                    } else {
                        nextInArival = false;
                        for (Process process : queue){
//                            assert currentProcess != null;
                            if(process.getArrivalTime() > currentProcess.getArrivalTime()){
                                currentProcess = process;
                                break;
                            }
                        }
                    }
                }
                    int timeToExecute = (int) Math.ceil(currentProcess.getQuantum() * 0.4);
                    executionOrder.add(currentProcess.getName());
                    if (timeToExecute > currentProcess.getRemainingTime()) {
                        currentTime += currentProcess.getRemainingTime();
                        currentProcess.setEndTime(currentTime);
                        currentProcess.setRemainingTime(0);
                        currentProcess.setRemainingQuantum(0);
                        completedProcess.add(currentProcess);
                        processes.remove(currentProcess);
                        queue.remove(currentProcess);
                        nextInArival = true;
                    }
                    else {
                        currentProcess.setRemainingTime(currentProcess.getRemainingTime() - timeToExecute);
                        currentTime += timeToExecute;
                        currentProcess.setRemainingQuantum(currentProcess.getQuantum() - timeToExecute);
                    }
                    while (currentProcess.getRemainingQuantum()>0 && currentProcess .getRemainingTime() > 0 ) {
//                        queue.add(currentProcess);
                        for (Process process : processes){
                            if(process.getArrivalTime() <= currentTime && !queue.contains(process) && process.getRemainingTime() > 0){
                                queue.add(process);
                            }
                        }
                        readyQueue = check(queue);
//                        queue.remove(currentProcess);
                        shortestProcess = readyQueue.poll();
                        if (shortestProcess != currentProcess) {
                            nextInFcai =true;
                            currentProcess.setQuantum(currentProcess.getQuantum()+ currentProcess.getRemainingQuantum());
                            queue.add(currentProcess);
                            break;
                        } else {
                            currentTime++;
                            currentProcess.setRemainingQuantum(currentProcess.getRemainingQuantum() - 1);
                            currentProcess.setRemainingTime(currentProcess.getRemainingTime()-1);
                        }
                    }
                    if(currentProcess.getRemainingQuantum()==0){
                        if(currentProcess.getRemainingTime() > 0 ){
                        currentProcess.setQuantum(currentProcess.getQuantum()+2);
                            if(!nextInFcai) {
                                readyQueue = check(queue);
                                readyQueue.remove(currentProcess);
                                shortestProcess = readyQueue.poll();
                                nextInFcai = true;
                            }
                        }
                    }
                    if(currentProcess.getRemainingTime() == 0 ) {
                        completedProcess.add(currentProcess);
                        processes.remove(currentProcess);
                        queue.remove(currentProcess);
                        nextInArival = true ;
                    }
            }else {
                currentTime++;
            }
        }
        System.out.println("Processes Execution Order: " + String.join(" - ", executionOrder));

    }
}
