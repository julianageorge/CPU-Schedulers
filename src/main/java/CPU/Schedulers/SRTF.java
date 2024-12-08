package CPU.Schedulers;
import CPU.Process;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SRTF {
    private ArrayList<Process> processes;
    private int contextSwitchTime;
    private int agingFactor = 1;
    private int Aging = 3;
    public SRTF(ArrayList<Process> processes, int contextSwitchTime) {
        this.processes=processes;
        this.contextSwitchTime=contextSwitchTime;
        executeSchuduling();
    }
    private void executeSchuduling(){
        int currentTime = 0 ,completed = 0, totalWaitingTime = 0, totalTurnaroundTime = 0;
        int n = processes.size();

        for (Process process : processes) {
            process.setRemainingTime(process.getBurstTime());
            process.setPriorityNum(process.getRemainingTime());
        }
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriorityNum)
                .thenComparingInt(Process::getArrivalTime));
        Process currentProcess = null;
        ArrayList<String> executionOrder = new ArrayList<>();
        ArrayList<Process> completedProcess = new ArrayList<>();
        Process shortedProcess = null;
        while(completed < n){
//            int shortestRemainingTime = Integer.MAX_VALUE;

            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && !readyQueue.contains(process)&& process.getRemainingTime() > 0) {
                    readyQueue.add(process);
                }
            }
            for( Process process : readyQueue){
                if(processes.contains(process))
                    processes.remove(process);
            }

            // to handle staravtion
            if(currentTime+1%Aging==0) {
                PriorityQueue<Process> temp = new PriorityQueue<>(Comparator.comparingInt(Process::getPriorityNum)
                        .thenComparingInt(Process::getArrivalTime));
                while (!readyQueue.isEmpty()) {
                    Process process = readyQueue.poll();
                    int waitingTime = currentTime - process.getArrivalTime();
                    process.setPriorityNum(process.getPriorityNum() - (waitingTime / agingFactor));// checking
                    temp.add(process);
                }
                readyQueue = temp;
            }

            if(readyQueue.isEmpty()){
                currentTime++;
            }
            else {
                shortedProcess = readyQueue.poll();
//                shortestRemainingTime = shortedProcess.getRemainingTime();
                if (currentProcess != shortedProcess) {
                    if(currentProcess!= null)
                        currentTime += contextSwitchTime;
                    executionOrder.add(shortedProcess.getName());
                    currentProcess = shortedProcess;
                }
                if(shortedProcess.getRemainingTime()==shortedProcess.getBurstTime()){
                    shortedProcess.setStartTime(currentTime);
                }
                shortedProcess.setRemainingTime(shortedProcess.getRemainingTime() - 1);
                currentTime++;
                if (shortedProcess.getRemainingTime() == 0) {
                    completedProcess.add(shortedProcess);
                    completed++;
                    shortedProcess.setEndTime(currentTime);
                    int turnaroundTime = currentTime - shortedProcess.getArrivalTime();
                    shortedProcess.setTurnAroundTime(turnaroundTime);
                    totalTurnaroundTime += turnaroundTime;
                    int waitingTime = shortedProcess.getStartTime() - shortedProcess.getArrivalTime();
                    shortedProcess.setWaitingTime(waitingTime);
                    totalWaitingTime += waitingTime;
                } else {
                    readyQueue.add(shortedProcess);
                }
            }
        }
        System.out.println("\nPreemptive Shortest-Remaining-Time-First (SRTF) Results: ");
        System.out.println("Processes Execution Order: " + String.join(" - ", executionOrder));
        System.out.println("\nProcess Details:");
        System.out.printf("%-10s%-15s%-15s%-15s%-15s\n", "Process", "Waiting Time", "Turnaround Time ", "Start Time", "End Time");
        for (Process process : completedProcess) {
            System.out.printf("%-10s%-15d%-15d%-15d%-15d\n",
                    process.getName(),
                    process.getWaitingTime(),
                    process.getTurnAroundTime(),
                    process.getStartTime(),
                    process.getEndTime());
        }
        double averageWaitingTime = (double) totalWaitingTime / completedProcess.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / completedProcess.size();
        System.out.printf("Average Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}
