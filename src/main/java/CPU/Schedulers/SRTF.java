package CPU.Schedulers;

import CPU.Process;
import CPU.duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SRTF {
    private ArrayList<Process> processes;
    private int contextSwitchTime;
    private int agingFactor = 1;
    private int Aging = 3;
    private ArrayList<duration> durations;

    public SRTF(ArrayList<Process> processes, int contextSwitchTime) {
        this.processes = processes;
        this.contextSwitchTime = contextSwitchTime;
        this.durations = new ArrayList<>();
        executeScheduling();
    }
    private void executeScheduling() {
        int currentTime = 0, completed = 0, totalWaitingTime = 0, totalTurnaroundTime = 0;
        int n = processes.size();

        for (Process process : processes) {
            process.setRemainingTime(process.getBurstTime());
            process.setPriorityNum(process.getRemainingTime());
        }
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriorityNum)
                .thenComparingInt(Process::getArrivalTime));
        Process currentProcess = null;
        boolean complete = false;
        ArrayList<String> executionOrder = new ArrayList<>();
        ArrayList<Process> completedProcess = new ArrayList<>();
        Process shortedProcess = null;
        int startTime = -1;

        while (completed < n) {
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && !readyQueue.contains(process) && process.getRemainingTime() > 0) {
                    readyQueue.add(process);
                }
            }

            // to handle starvation
            if (currentTime % Aging == 0) {
                PriorityQueue<Process> temp = new PriorityQueue<>(Comparator.comparingInt(Process::getPriorityNum)
                        .thenComparingInt(Process::getArrivalTime));
                while (!readyQueue.isEmpty()) {
                    Process process = readyQueue.poll();
                    int waitingTime = currentTime - process.getArrivalTime();
                    process.setPriorityNum(process.getPriorityNum() - (waitingTime / agingFactor));
                    temp.add(process);
                }
                readyQueue = temp;
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
            } else {
                shortedProcess = readyQueue.poll();
                if (currentProcess != shortedProcess) {

                    if (currentProcess != null && startTime != -1) {
                        duration prevDuration = new duration(
                                currentProcess.getName(),
                                startTime,
                                currentTime,
                                currentProcess.getId(),
                                "Working",
                                currentProcess.getRemainingTime(),
                                currentProcess.getArrivalTime()
                        );
                        durations.add(prevDuration);
                    }


                    if (currentProcess!=null) {
                        currentTime += contextSwitchTime;
                    }

                    executionOrder.add(shortedProcess.getName());
                    currentProcess = shortedProcess;
                    startTime = currentTime;
                }



                if (shortedProcess.getRemainingTime() == shortedProcess.getBurstTime()) {
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
                    int waitingTime = turnaroundTime - shortedProcess.getBurstTime();
                    shortedProcess.setWaitingTime(waitingTime);
                    totalWaitingTime += waitingTime;

                    duration finalDuration = new duration(
                            shortedProcess.getName(),
                            startTime,
                            currentTime,
                            shortedProcess.getId(),
                            "Completed",
                            0,
                            shortedProcess.getArrivalTime()
                    );
                    durations.add(finalDuration);
                    //currentProcess = null;
                     complete =true;
                    startTime = -1;
                } else {
                    readyQueue.add(shortedProcess);
                }
            }
        }

        // Print the results
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

        System.out.println("\nSub-task Durations:");
        for (duration d : durations) {
            System.out.println(d);
        }

        double averageWaitingTime = (double) totalWaitingTime / completedProcess.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / completedProcess.size();
        System.out.printf("Average Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}