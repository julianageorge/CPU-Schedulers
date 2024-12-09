package CPU.Schedulers;

import CPU.Process;
import CPU.duration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SJF {
    private ArrayList<Process> processes;
    private ArrayList<duration> durations;
    private int contextSwitchTime;
    private int agingFactor = 1;
    public SJF(ArrayList<Process> processes, int contextSwitchTime) {
        this.processes=processes;
        this.contextSwitchTime=contextSwitchTime;
        this.durations = new ArrayList<>();
        executeSchuduling();
    }
    private void executeSchuduling(){
        processes.sort(Comparator.comparingInt(Process::getArrivalTime).thenComparingInt(Process::getBurstTime));
        for (Process process : processes) {
            process.setPriorityNum(process.getBurstTime());
        }
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriorityNum)
                .thenComparingInt(Process::getArrivalTime));
        int currentTime = 0, totalWaitingTime = 0, totalTurnaroundTime = 0;
        ArrayList<String> executionOrder = new ArrayList<>();
        ArrayList<Process> completedProcess = new ArrayList<>();

        while (!processes.isEmpty()) {
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && !readyQueue.contains(process)) {
                    readyQueue.add(process);
//                    process.setPriorityNum(process.getBurstTime());
                }
            }

            for (Process process : readyQueue) {
                int waitingTime = currentTime - process.getArrivalTime();
                process.setPriorityNum(process.getPriorityNum() - (waitingTime / agingFactor));// checking
            }

            if (!readyQueue.isEmpty()) {
                Process processToExecute = readyQueue.poll();
                processes.remove(processToExecute);
                completedProcess.add(processToExecute);

                if (currentTime < processToExecute.getArrivalTime()) {
                    currentTime = processToExecute.getArrivalTime();
                }

                if (!executionOrder.isEmpty()) {
                    currentTime += contextSwitchTime;
                }

                processToExecute.setStartTime(currentTime);
                executionOrder.add(processToExecute.getName());

                int waitingTime = currentTime - processToExecute.getArrivalTime();
                processToExecute.setWaitingTime(waitingTime);
                totalWaitingTime += waitingTime;

                int turnaroundTime = waitingTime + processToExecute.getBurstTime();
                processToExecute.setTurnAroundTime(turnaroundTime);
                totalTurnaroundTime += turnaroundTime;

                processToExecute.setEndTime(currentTime + processToExecute.getBurstTime());
                durations.add(new duration(
                        processToExecute.getName(),
                        currentTime,
                        currentTime + processToExecute.getBurstTime(),
                        processToExecute.getId(),
                        processToExecute.getColor(),
                        "Completed",
                        processToExecute.getRemainingTime(),
                        processToExecute.getArrivalTime(),
                        null
                ));


                currentTime += processToExecute.getBurstTime();
            } else {
                // If no process is ready, increment the current time
                currentTime++;
            }
        }
        System.out.println("\nNon-Preemptive Shortest-Job-First Results: ");
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
    public ArrayList<duration> getduration() {
        return durations;
    }

}