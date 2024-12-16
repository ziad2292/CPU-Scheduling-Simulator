import java.awt.*;
import java.util.*;
import java.util.List;

public class sjfScheduling implements scheduler {

    public double averageWaitingTime;
    public double averageTurnAroundTime;

    @Override
    public void execute(List<Process> processes, int contextSwitchTime) {
        int currentTime = 0;
        int completed = 0;
        int n = processes.size();

        PriorityQueue<Process> readyQueue = new PriorityQueue<>((p1, p2) -> {
            if (p1.remainingBurstTime == p2.remainingBurstTime) {
                return Integer.compare(p1.priority, p2.priority); // Compare by aging priority
            }
            return Integer.compare(p1.remainingBurstTime, p2.remainingBurstTime);
        });

        List<Process> arrivedProcesses = new ArrayList<>();
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        while (completed < n) {
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && !arrivedProcesses.contains(p)) {
                    readyQueue.add(p);
                    arrivedProcesses.add(p);
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                SchedulingGUI.rectangleX += 700/30;
                continue;
            }

            Process currentProcess = readyQueue.poll();
            SchedulingGUI.gui.createProcess(currentProcess);

            if(currentProcess.yCoordinate == -1){
                currentProcess.yCoordinate = SchedulingGUI.rectangleY;
            }

            currentTime += currentProcess.remainingBurstTime;
            for(int i = 0; i < currentProcess.remainingBurstTime; i++) {
                SchedulingGUI.gui.createRectangle(SchedulingGUI.rectangleX, currentProcess.yCoordinate, Color.GREEN);
                SchedulingGUI.rectangleX += 700/30;
            }
            currentProcess.turnAroundTime = currentTime - currentProcess.arrivalTime;
            currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.remainingBurstTime;

            currentProcess.remainingBurstTime = 0; // Process is completed
            completed++;

            for (Process p : readyQueue) {
                p.priority++; // Increment priority to implement aging
            }

            currentTime += contextSwitchTime;// Add context switch time
            SchedulingGUI.rectangleX += (700/30)*contextSwitchTime;
            SchedulingGUI.rectangleY += 50;

            if(currentTime > SchedulingGUI.maximumTime){
                SchedulingGUI.maximumTime = currentTime;
            }
        }

        calculateAverageTimes(processes);

    }

    private void calculateAverageTimes(List<Process> processes) {
        int totalWaitingTime = 0;
        int totalTurnAroundTime = 0;

        for (Process p : processes) {
            totalWaitingTime += p.waitingTime;
            totalTurnAroundTime += p.turnAroundTime;
        }

        averageWaitingTime = (double) totalWaitingTime / processes.size();
        averageTurnAroundTime = (double) totalTurnAroundTime / processes.size();

        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnAroundTime);
        SchedulingGUI.gui.finalSummary(averageWaitingTime, averageTurnAroundTime);
    }

}