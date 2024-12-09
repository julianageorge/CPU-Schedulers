package org.os;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import CPU.Process;
import CPU.Schedulers.*;
import CPU.duration;
public class SchedulerGUI extends JFrame {
    private ArrayList<Process> processes = new ArrayList<>();
    private ArrayList<duration> durations = new ArrayList<>();
    private JTextField nameField, colorField, arrivalField, burstField, priorityField, quantumField, contextField;
    public static JComboBox<String> algorithmBox;
    private JButton addButton, scheduleButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public SchedulerGUI() {
        setTitle("CPU Scheduler");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Process"));

        // Layout Setup
        addComponent(inputPanel, new JLabel("Process Name:"), 0, 0, gbc);
        nameField = new JTextField(15);
        addComponent(inputPanel, nameField, 1, 0, gbc);

        addComponent(inputPanel, new JLabel("Process Color:"), 0, 1, gbc);
        colorField = new JTextField(15);
        addComponent(inputPanel, colorField, 1, 1, gbc);

        addComponent(inputPanel, new JLabel("Arrival Time:"), 0, 2, gbc);
        arrivalField = new JTextField(15);
        addComponent(inputPanel, arrivalField, 1, 2, gbc);

        addComponent(inputPanel, new JLabel("Burst Time:"), 0, 3, gbc);
        burstField = new JTextField(15);
        addComponent(inputPanel, burstField, 1, 3, gbc);

        addComponent(inputPanel, new JLabel("Priority:"), 0, 4, gbc);
        priorityField = new JTextField(15);
        addComponent(inputPanel, priorityField, 1, 4, gbc);

        addComponent(inputPanel, new JLabel("Quantum Time:"), 0, 5, gbc);
        quantumField = new JTextField(15);
        addComponent(inputPanel, quantumField, 1, 5, gbc);

        addButton = new JButton("Add Process");
        addComponent(inputPanel, addButton, 0, 6, gbc);

        algorithmBox = new JComboBox<>(new String[]{"Non-Preemptive Priority", "Non-Preemptive SJF", "SRTF", "FCAI"});
        addComponent(inputPanel, algorithmBox, 1, 6, gbc);

        JPanel contextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addComponent(contextPanel, new JLabel("Context Switch Time:"), 0, 0, gbc);
        contextField = new JTextField(15);
        contextPanel.add(contextField);
        scheduleButton = new JButton("Schedule");
        contextPanel.add(scheduleButton);

        add(inputPanel, BorderLayout.NORTH);
        add(contextPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new String[]{"Process", "Waiting Time", "Turnaround Time", "Start Time", "End Time"}, 0);
        resultTable = new JTable(tableModel);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);

        addButton.addActionListener(e -> addProcess());
        scheduleButton.addActionListener(e -> scheduleProcesses());
    }

    private void addComponent(JPanel panel, JComponent component, int gridx, int gridy, GridBagConstraints gbc) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        panel.add(component, gbc);
    }


    private int idCounter = 0;

    private void addProcess() {
        try {
            String name = nameField.getText();
            String color = colorField.getText().trim();
            int arrival = Integer.parseInt(arrivalField.getText());
            int burst = Integer.parseInt(burstField.getText());
            int priority = Integer.parseInt(priorityField.getText());
            int quantum = quantumField.getText().isEmpty() ? 0 : Integer.parseInt(quantumField.getText());

            // Validate color format
            if (!color.startsWith("#") || color.length() != 7) {
                JOptionPane.showMessageDialog(this, "Invalid color format. Please use hex format like #FF0000.");
                return;
            }


            int id = idCounter++;


            Process process = new Process(name, id, color, arrival, burst, priority, quantum);
            processes.add(process);

            JOptionPane.showMessageDialog(this, "Process added successfully!");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check your entries.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        colorField.setText("");
        arrivalField.setText("");
        burstField.setText("");
        priorityField.setText("");
        quantumField.setText("");
        contextField.setText("");
    }

    private void scheduleProcesses() {
        if (processes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one process.");
            return;
        }
        int context = contextField.getText().isEmpty() ? 0 : Integer.parseInt(contextField.getText());
        String selectedAlgorithm = (String) algorithmBox.getSelectedItem();

        durations.clear();

        if ("Non-Preemptive Priority".equals(selectedAlgorithm)) {
            Priority_Scheduling ps = new Priority_Scheduling(processes, context);
            durations.addAll(ps.getduration());
        } else if ("Non-Preemptive SJF".equals(selectedAlgorithm)) {
            SJF sjf = new SJF(processes, context);
            durations.addAll(sjf.getduration());
        } if ("SRTF".equals(selectedAlgorithm)) {
            SRTF srtf = new SRTF(processes, context);
            durations.addAll(srtf.getduration());
        } /*else if ("FCAI".equals(selectedAlgorithm)) {
            FCAI fcai = new FCAI(processes);
            durations.addAll(fcai.getduration());
        }*/

        updateTable();
        showGanttChart();
    }

    private void showGanttChart() {

        JFrame frame = new JFrame("Gantt Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 500);


        JPanel chartPanel = new GanttChartPanel("Gantt Chart", durations);
        frame.setContentPane(chartPanel);


        frame.setVisible(true);
    }


    private void updateTable() {
        tableModel.setRowCount(0);
        for (Process process : processes) {
            tableModel.addRow(new Object[]{
                    process.getName(),
                    process.getWaitingTime(),
                    process.getTurnAroundTime(),
                    process.getStartTime(),
                    process.getEndTime()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SchedulerGUI().setVisible(true);
        });
    }
}