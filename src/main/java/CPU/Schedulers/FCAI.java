package CPU.Schedulers;

import CPU.Process;
import java.util.*;

public class FCAI {
    private ArrayList<Process> processes;
    private double V1, V2;
    private int totalTime;

    public FCAI(ArrayList<Process> processes) {
        this.processes = processes;
        calculateV1V2();
        schedule();
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

    private void calculateFCAIFactor(Process process) {
        double FcaiFactor = (10 - process.getPriorityNum()) + (process.getArrivalTime() / V1) + (process.getRemainingTime() / V2);
        process.setTurnAroundTime((int) FcaiFactor);
    }

    private void schedule() {
        for (Process process : processes) {
            calculateFCAIFactor(process);
            process.setQuantum(process.getQuantum());
        }


        Collections.sort(processes);

        int currentTime = 0;
        while (!processes.isEmpty()) {
            Process processToExecute = getNextProcessToExecute(currentTime);
            if (processToExecute != null) {
                executeProcess(processToExecute, currentTime);
                currentTime = processToExecute.getEndTime();
            } else {
                break;
            }
        }

    }

    private Process getNextProcessToExecute(int currentTime) {
        Process selectedProcess = null;
        double minFCAI = Double.MAX_VALUE;

        for (Process p : processes) {
            if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0) {
                double currentFCAI = p.getTurnAroundTime();
                if (currentFCAI < minFCAI) {
                    minFCAI = currentFCAI;
                    selectedProcess = p;
                }
            }
        }

        return selectedProcess;
    }

    private void executeProcess(Process process, int currentTime) {
        int quantum = process.getQuantum();
        int executedTime = Math.min(quantum, process.getRemainingTime());
        process.setRemainingTime(process.getRemainingTime() - executedTime);
        process.setStartTime(currentTime);
        process.setEndTime(currentTime + executedTime);

        if (process.getRemainingTime() == 0) {
        } else {
            process.setQuantum(process.getQuantum() + 2);
        }

        currentTime = process.getEndTime();

        if (process.getRemainingTime() > 0) {
            process.setQuantum(process.getQuantum() + process.getQuantum());
        }
    }}