package org.os;

import java.util.*;
import CPU.Process;
import CPU.Schedulers.*;

public class Main {
    private static Scanner input = new Scanner(System.in); // Made static
    private static ArrayList<Process> processes = new ArrayList<>(); // Made static
    private static int numProcess;
    private static int RRTimeQuantum;
    private static int contextSwitchTime;
    private static String ProcessName;
    private static String ProcessColor;
    private static int ArrivalTime;
    private static int BurstTime;
    private static int priorityNum;

    public static void receive_processes_data(int num, boolean flag) {
        for (int i = 0; i < num; i++) {
            System.out.println("Please enter name of process " + (i + 1) + ":");
            ProcessName = input.next();
            System.out.println("Please enter color of process " + ProcessName + ":");
            ProcessColor = input.next();
            System.out.println("Please enter arrival time of process " + ProcessName + ":");
            ArrivalTime = input.nextInt();
            System.out.println("Please enter burst time of process " + ProcessName + ":");
            BurstTime = input.nextInt();
            System.out.println("Please enter priority number of process " + ProcessName + ":");
            priorityNum = input.nextInt();

            if (flag) {
                System.out.println("Please enter quantum time:");
                RRTimeQuantum = input.nextInt();
            }

            processes.add(new Process(ProcessName,i,ProcessColor, ArrivalTime, BurstTime, priorityNum, (flag ? RRTimeQuantum : 0)));
        }

        if (!flag) {
            System.out.println("Please enter context switch time:");
            contextSwitchTime = input.nextInt();
        }
    }

    public static void main(String[] args) {
        System.out.println("Please enter number of processes:");
        numProcess = input.nextInt();

        System.out.println("Please choose a scheduler:");
        System.out.println("1. Non-Preemptive Priority");
        System.out.println("2. Non-Preemptive SJF");
        System.out.println("3. SRTF Scheduler");
        System.out.println("4. FCAI Scheduler");

        int selectedCase = input.nextInt();

        switch (selectedCase) {
            case 1:
                receive_processes_data(numProcess, false);
                Priority_Scheduling priorityScheduling = new Priority_Scheduling(processes, contextSwitchTime);
                break;

            case 2:
                receive_processes_data(numProcess, false);
                SJF sjf = new SJF(processes, contextSwitchTime);
                break;

            case 3:
                receive_processes_data(numProcess, false);
                SRTF srtf = new SRTF(processes, contextSwitchTime);
                break;

            case 4:
                receive_processes_data(numProcess, true);
                FCAI fcai = new FCAI(processes);
                break;

            default:
                System.out.println("Please choose a valid scheduler.");
                break;
        }
    }
}