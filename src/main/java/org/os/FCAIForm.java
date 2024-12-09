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


        tableModel = new DefaultTableModel(new String[]{"Time", "Process", "Status", "Remaining Time", "Quantum", "FCAI Factor"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        for (duration process : durations) {
            tableModel.addRow(new Object[]{
                    process.getStartTime() + "->" + process.getEndTime(),
                    process.getProcessName(),
                    process.getStatus(),
                    process.getRemaingtime(),
                   process.getQuantum(),
                    process.getFcaiFactor()
            });
        }
    }
}
