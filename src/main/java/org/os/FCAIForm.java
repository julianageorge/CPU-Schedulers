package org.os;
import CPU.duration;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import CPU.Process;

public class FCAIForm extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public FCAIForm(ArrayList<Process> processes, ArrayList<duration> durations) {
        setTitle("FCAI Scheduling Results");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());


        tableModel = new DefaultTableModel(new String[]{"Time", "Process", "Status", "Remaining Time", "Quantum", "Priority","FCAI Factor"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        for (duration process : durations) {
            String quantumStatus = process.getRemaingtime() == 0 ? "Complete" : String.valueOf(process.getQuantum());
            String status = process.getStatus();
            if (process.getPreemptedBy() != null) {
                status = process.getPreemptedBy() + " preempts " + process.getProcessName() + ", " + status;
            }

            tableModel.addRow(new Object[]{
                    process.getStartTime() + "->" + process.getEndTime(),
                    process.getProcessName(),
                    status,
                    process.getRemaingtime(),
                   quantumStatus,
                    process.getPriority(),
                    process.getFcaiFactor()
            });
        }
    }
}
