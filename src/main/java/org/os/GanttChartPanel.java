package org.os;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.IntervalCategoryToolTipGenerator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.chart.plot.CategoryPlot;
import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import org.jfree.chart.axis.DateAxis;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import CPU.duration;

public class GanttChartPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public GanttChartPanel(final String title, ArrayList<duration> processes) {
        super();
        int averageWait = 0;
        int averageTurnaround = 0;
        HashMap<Integer, Boolean> occurred = new HashMap<>();
        StringBuilder message = new StringBuilder();

        for (duration process : processes) {
            if (occurred.get(process.id) == null) {
                int wait = process.StartTime - process.processArrivalTime;
                averageWait += wait;
                averageTurnaround += (wait + process.processBurstTime);
                message.append(MessageFormat.format("{0}, Waiting Time: {1}, Turnaround Time: {2}\n",
                        process.ProcessName, wait, wait + process.processBurstTime));
                occurred.put(process.id, true);
            }
        }

        message.append(MessageFormat.format("The average waiting time is: {0}\n", averageWait / processes.size()));
        message.append(MessageFormat.format("The average turnaround time is: {0}\n", averageTurnaround / processes.size()));

        GanttCategoryDataset dataset = createDataset(processes);

        JFreeChart chart = createChart(dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 400));
        this.add(chartPanel);
    }
    public GanttCategoryDataset createDataset(ArrayList<duration> processes) {
        HashMap<Integer, Task> tasks = new HashMap<>();

        for (duration process : processes) {
            if (tasks.get(process.id) == null) {
                tasks.put(process.id, new Task(process.ProcessName,
                        new SimpleTimePeriod(0, processes.get(processes.size() - 1).endTime)));
            }

            Task subtask = new Task(process.ProcessName,
                    new SimpleTimePeriod(process.StartTime, process.endTime));
            subtask.setDescription(process.ProcessName);
            tasks.get(process.id).addSubtask(subtask);
        }

        TaskSeriesCollection collection = new TaskSeriesCollection();
        tasks.forEach((id, task) -> {
            TaskSeries taskSeries = new TaskSeries(task.getDescription());
            taskSeries.add(task);
            collection.add(taskSeries);
        });

        return collection;
    }

    private JFreeChart createChart(GanttCategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createGanttChart(
                "CPU Usage",
                "Processes",
                "Time",
                dataset,
                true,
                true,
                false
        );

        CategoryPlot plot = chart.getCategoryPlot();

        plot.getRenderer().setDefaultToolTipGenerator(new MyToolTipGenerator());

        DateAxis axis = (DateAxis) plot.getRangeAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("S"));

        return chart;
    }


    static class MyToolTipGenerator extends IntervalCategoryToolTipGenerator {
        @Override
        public String generateToolTip(CategoryDataset cds, int row, int col) {
            final String s = super.generateToolTip(cds, row, col);
            StringBuilder sb = new StringBuilder(s);
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
    }
}