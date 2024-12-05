package CPU.Schedulers;
import CPU.Process;
import java.util.ArrayList;

public class SRTF {
    private ArrayList<Process> processes;
    private int contextSwitchTime;
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
        }

        Process currentProcess = null;
        ArrayList<String> executionOrder = new ArrayList<>();

        while(completed < n){
            Process shortedProcess = null;
            int shortestRemainingTime = Integer.MAX_VALUE;

            for(Process process: processes){
                if(process.getArrivalTime() <= currentTime && process.getRemainingTime() > 0){
                    if(process.getRemainingTime() < shortestRemainingTime) {
                        shortestRemainingTime = process.getRemainingTime();
                        shortedProcess = process;
                    }
                }
            }
            if(shortedProcess == null){
                currentTime++;
                continue;
            }
            if(currentProcess != shortedProcess){
                executionOrder.add(shortedProcess.getName());
                currentProcess = shortedProcess;
                if(currentProcess != null)
                   currentTime += contextSwitchTime;

            }
            shortedProcess.setRemainingTime(shortedProcess.getRemainingTime()-1);
            currentTime++;

            if(shortedProcess.getRemainingTime() == 0){
                completed++;
                shortedProcess.setEndTime(currentTime);
                int turnaroundTime = currentTime - shortedProcess.getArrivalTime();
                shortedProcess.setTurnAroundTime(turnaroundTime);
                totalTurnaroundTime += turnaroundTime;

                int waitingTime = turnaroundTime - shortedProcess.getBurstTime();
                shortedProcess.setWaitingTime(waitingTime);
                totalWaitingTime += waitingTime;
            }
        }
        System.out.println("\nPreemptive Shortest-Remaining-Time-First (SRTF) Results: ");
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

        System.out.printf("Average Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}
