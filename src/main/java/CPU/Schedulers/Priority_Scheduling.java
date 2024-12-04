package CPU.Schedulers;

import CPU.Process;
import java.util.*;

public class Priority_Scheduling {
    private ArrayList<Process> processes;
    private int contextSwitchTime;

    public Priority_Scheduling(ArrayList<Process> processes, int contextSwitchTime) {
        this.processes = processes;
        this.contextSwitchTime = contextSwitchTime;
        executeScheduling();
    }

    private void executeScheduling() {
        processes.sort(Comparator.comparingInt(Process::getArrivalTime).thenComparingInt(Process::getPriorityNum));
        int currentTime = 0, totalWaitingTime = 0, totalTurnaroundTime = 0;

        ArrayList<String> executionOrder = new ArrayList<>();

        for (Process process : processes) {
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }

            if (!executionOrder.isEmpty()) {
                currentTime += contextSwitchTime;
            }

            process.setStartTime(currentTime);
            executionOrder.add(process.getName());

            int waitingTime = currentTime - process.getArrivalTime();
            process.setWaitingTime(waitingTime);
            totalWaitingTime += waitingTime;

            int turnaroundTime = waitingTime + process.getBurstTime();
            process.setTurnAroundTime(turnaroundTime);
            totalTurnaroundTime += turnaroundTime;

            process.setEndTime(currentTime + process.getBurstTime());

            currentTime += process.getBurstTime();
        }

        System.out.println("\nNon-Preemptive Priority Scheduling Results: ");
        System.out.println("Processes Execution Order: " + String.join(" - ", executionOrder));

        System.out.println("\nProcess Details:");
        System.out.printf("%-10s%-15s%-15s%-15s%-15s\n", "Process", "Waiting Time", "Turnaround Time ", "Start Time", "End Time");
        for (Process process : processes) {
            System.out.printf("%-10s%-15d%-15d%-15d%-15d\n",
                    process.getName(),
                    process.getWaitingTime(),
                    process.getTurnAroundTime(),
                    process.getStartTime(),
                    process.getEndTime());
        }


        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();

        System.out.printf("\nAverage Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}