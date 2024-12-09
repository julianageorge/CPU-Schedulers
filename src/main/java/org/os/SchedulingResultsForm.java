package org.os;

import CPU.duration;
import CPU.Process;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SchedulingResultsForm extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public SchedulingResultsForm(ArrayList<Process> processes, ArrayList<duration> durations) {
        setTitle("Scheduling Algorithm Results");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());


        tableModel = new DefaultTableModel(new String[]{"Time", "Process", "Status", "Remaining Time"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);


        for (duration process : durations) {
            String status = process.getStatus();
            if (process.getPreemptedBy() != null) {
                status = process.getPreemptedBy() + " preempts " + process.getProcessName() + ", " + status;
            }

            tableModel.addRow(new Object[]{
                    process.getStartTime() + "->" + process.getEndTime(),
                    process.getProcessName(),
                    status,
                    process.getRemaingtime()
            });
        }
    }
}
