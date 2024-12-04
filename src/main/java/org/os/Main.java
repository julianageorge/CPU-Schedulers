package org.os;
import java.awt.*;
import java.util.*;
import CPU.Process;
import CPU.Schedulers.*;
public class Main {
    public static void main(String[] args) {
        int numProcess;
        int RRTimeQuantum;
        int contextSwitchTime;
        String ProcessName;
        String ProcessColor;
        int ArrivalTime;
        int BurstTime;
        int priorityNum;
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter number of processes ");
        numProcess=input.nextInt();
        ArrayList<Process> processes = new ArrayList<Process>();
        System.out.println("Please Choose a scheduler:");
        System.out.println("1. Non-Preemptive Priority");
        System.out.println("2. Non-Preemptive SJF");
        System.out.println("3. SRTF Scheduler");
        System.out.println("4. FCAI Scheduler");
        int Case = input.nextInt();
        switch (Case){
            case 1:
                for (int i = 0; i < numProcess; i++) {
                    System.out.println("Please enter name of process " + (i+1));
                    ProcessName = input.next();
                    System.out.println("Please enter Color of process " + ProcessName);
                    ProcessColor=input.next();
                    System.out.println("Please enter arrival time of process " + ProcessName);
                    ArrivalTime = input.nextInt();
                    System.out.println("Please enter burst time of process " + ProcessName);
                    BurstTime = input.nextInt();
                    System.out.println("Please enter priority Number of Process "+ ProcessName);
                    priorityNum = input.nextInt();
                    processes.add(new Process(ProcessName,ProcessColor, ArrivalTime, BurstTime, priorityNum));}
                System.out.println("Please enter context switching time ");
                contextSwitchTime = input.nextInt();
                Priority_Scheduling priorityScheduling=new Priority_Scheduling(processes,contextSwitchTime);
                break;
            case 2:
                for (int i = 0; i < numProcess; i++) {
                    System.out.println("Please enter name of process " + (i+1));
                    ProcessName = input.next();
                    System.out.println("Please enter Color of process " + ProcessName);
                    ProcessColor=input.next();
                    System.out.println("Please enter arrival time of process " + ProcessName);
                    ArrivalTime = input.nextInt();
                    System.out.println("Please enter burst time of process " + ProcessName);
                    BurstTime = input.nextInt();
                    System.out.println("Please enter priority Number of Process "+ ProcessName);
                    priorityNum = input.nextInt();
                    processes.add(new Process(ProcessName,ProcessColor, ArrivalTime, BurstTime, priorityNum));}
                System.out.println("Please enter context switching time ");
                contextSwitchTime = input.nextInt();
                SJF sjf=new SJF(processes,contextSwitchTime);
                break;
            case 3:
                for (int i = 0; i < numProcess; i++) {
                    System.out.println("Please enter name of process " + (i+1));
                    ProcessName = input.next();
                    System.out.println("Please enter Color of process " + ProcessName);
                    ProcessColor=input.next();
                    System.out.println("Please enter arrival time of process " + ProcessName);
                    ArrivalTime = input.nextInt();
                    System.out.println("Please enter burst time of process " + ProcessName);
                    BurstTime = input.nextInt();
                    System.out.println("Please enter priority Number of Process "+ ProcessName);
                    priorityNum = input.nextInt();
                    processes.add(new Process(ProcessName,ProcessColor, ArrivalTime, BurstTime, priorityNum));}
                SRTF srtf=new SRTF(processes);
                break;
            case 4:
                for (int i = 0; i < numProcess; i++) {
                    System.out.println("Please enter name of process " + (i+1));
                    ProcessName = input.next();
                    System.out.println("Please enter Color of process " + ProcessName);
                    ProcessColor=input.next();
                    System.out.println("Please enter arrival time of process " + ProcessName);
                    ArrivalTime = input.nextInt();
                    System.out.println("Please enter burst time of process " + ProcessName);
                    BurstTime = input.nextInt();
                    System.out.println("Please enter priority Number of Process "+ ProcessName);
                    priorityNum = input.nextInt();
                System.out.println("Please enter Quantum time ");
                RRTimeQuantum = input.nextInt();
                processes.add(new Process(ProcessName,ProcessColor, ArrivalTime, BurstTime, priorityNum,RRTimeQuantum));
                }
                FCAI fcai =new FCAI(processes);
                break;
            default:
                System.out.println("Please Choose valid Scheduler");
                break;
    }
}}