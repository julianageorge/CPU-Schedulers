package org.os;

import CPU.duration;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import CPU.Process;

class ResultForm extends JFrame {
    public ResultForm(ArrayList<Process> processes, ArrayList<duration> durations, double avgWaitingTime, double avgTurnaroundTime) {
        setTitle("Schedule Results");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnNames = {"Process", "Waiting Time", "Turnaround Time"};
        DefaultTableModel resultsModel = new DefaultTableModel(columnNames, 0);
        JTable resultsTable = new JTable(resultsModel);

        for (Process process : processes) {
            int waitingTime = process.getWaitingTime();
            int turnaroundTime = process.getTurnAroundTime();
            resultsModel.addRow(new Object[]{process.getName(), waitingTime, turnaroundTime});
        }


        JPanel averagesPanel = new JPanel();
        averagesPanel.setLayout(new BoxLayout(averagesPanel, BoxLayout.Y_AXIS));
        averagesPanel.setBorder(BorderFactory.createTitledBorder("Averages"));

        JLabel avgWaitingLabel = new JLabel("Average Waiting Time: " + avgWaitingTime);
        JLabel avgTurnaroundLabel = new JLabel("Average Turnaround Time: " + avgTurnaroundTime);

        avgWaitingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avgTurnaroundLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        averagesPanel.add(avgWaitingLabel);
        averagesPanel.add(avgTurnaroundLabel);


        add(new JScrollPane(resultsTable), BorderLayout.CENTER);
        add(averagesPanel, BorderLayout.SOUTH);
    }
}

