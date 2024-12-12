package CPU.Schedulers;

import CPU.Process;
import java.util.*;

public class FCAI {
    private ArrayList<Process> processes;
    private double V1, V2;
    private int contextSwitchTime;
    private int totalTime;
    private boolean nextInArival = false , nextInFcai = false , finish = false;

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
        int currentTime = 0;
        ArrayList<String> executionOrder = new ArrayList<>();
        int n =processes.size(),currentIndx = 0;
        for (Process process : processes) {
            process.setRemainingTime(process.getBurstTime());
            process.setFcaiFactor(calculateFCAIFactor(process));
        }
        processes.sort(Comparator.comparingInt(Process::getArrivalTime)
                .thenComparingInt(Process::getFcaiFactor));
        Queue<Process> WorkingQueue = new LinkedList<>();
        ArrayList<Process> completedProcess = new ArrayList<>();
        Queue<Process> FcaiQueue = new LinkedList<>() ;
        Process currentProcess = null;
        // when remaining time finshed take next in arrival
        // else take next in fcai
        while (completedProcess.size()<n) {
            for (Process process : processes){
                if(process.getArrivalTime() <= currentTime && !WorkingQueue.contains(process)) {
                    WorkingQueue.add(process);
                }
            }
            if(WorkingQueue.isEmpty()){
                currentTime++;
            }else {
                    if(nextInFcai){
                        if(processes.size()>1){
                            Process temp = currentProcess;
                            FcaiQueue.remove(currentProcess);
                            currentProcess =  FcaiQueue.peek();
                            FcaiQueue.add(temp);
                        }else {
                            currentProcess =  FcaiQueue.peek();
                        }
                        nextInFcai =false;
                    }else {
                        currentProcess = processes.get(currentIndx);
                    }
                int timeToExecute = (int) Math.ceil(currentProcess.getQuantum() * 0.4);
                    currentProcess.setRemainingQuantum(currentProcess.getQuantum() - timeToExecute);
                    executionOrder.add(currentProcess.getName());
                    if(timeToExecute > currentProcess .getRemainingTime()){// lw al reminingtime 5ls b4elo mn working w processes w asfr al remining time
                        currentTime += currentProcess.getRemainingTime();  //  w a7to fal completed w next in arrival = true
                        currentProcess.setRemainingTime(0);
                    }else {// lw la bn2s el executed time w remaining quantum
                        currentTime += timeToExecute;
                        currentProcess.setRemainingTime(currentProcess.getRemainingTime() - timeToExecute);
                    }while (currentProcess.getRemainingTime() > 0 && currentProcess.getRemainingQuantum() > 0){
                    for (Process process : processes){
                        if(process.getArrivalTime() <= currentTime && !WorkingQueue.contains(process)) {
                            WorkingQueue.add(process);
                        }
                    }
                     FcaiQueue = check(WorkingQueue);
                     if(FcaiQueue.peek() != currentProcess){
                         nextInFcai = true; // lw fy as8r nmo nextinFCAI = true + update quantum
                         currentProcess.setQuantum(currentProcess.getQuantum() + currentProcess.getRemainingQuantum());
                         break;
                     }else{
                         currentTime++;
                         currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                         currentProcess.setRemainingQuantum(currentProcess.getRemainingQuantum() - 1);
                     }
                    }
                        if (currentProcess.getRemainingTime() == 0) {
                            completedProcess.add(currentProcess);
                            if(processes.size() > 1 ){
                                currentIndx = processes.indexOf(currentProcess) ;
                                if(currentIndx == processes.size()){
                                    currentIndx = 0;
                                }
                            }
                            processes.remove(currentProcess);
                            WorkingQueue.remove(currentProcess);
                            FcaiQueue.remove(currentProcess);
                            finish= true;
                        } else if(currentProcess.getRemainingQuantum() == 0){ // lw la b update al quantum
                            currentProcess.setQuantum(currentProcess.getQuantum() + 2);
                            nextInFcai =true;
                        }
            }

        }
        System.out.println("Processes Execution Order: " + String.join(" - ", executionOrder));

    }
}
