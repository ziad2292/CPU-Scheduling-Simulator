import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class srtfScheduling  implements scheduler {
    int currentTime = 0, contextSwitches = 0;
    Process current = null;

    @Override
    public void execute(List<Process> processes, int contextSwitchTime) {
        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        Queue<Process> readyQueue = new LinkedList<>();

        // Simulation
        while (true) {
            // Add new arrived processes
            for (Process p : processes) {
                if (!p.isCompleted && p.arrivalTime <= currentTime && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }

            // Select the process with the shortest remaining time
            Process shortest = null;
            for (Process process : readyQueue) {
                if (shortest == null || process.remainingTime < shortest.remainingTime) {
                    shortest = process;
                }
            }

            // Check if a context switch occurs
            if (shortest != current) {
                if (current != null) {
                    contextSwitches++;
                }
                current = shortest;
                SchedulingGUI.gui.createProcess(shortest);
                if (current != null) {
                    currentTime += contextSwitchTime; // Simulate context switch time
                    SchedulingGUI.rectangleX += (700/30)*contextSwitchTime;
                }
                if(current.yCoordinate == -1){
                    current.yCoordinate = SchedulingGUI.rectangleY;
                    SchedulingGUI.rectangleY += 50;
                }
            }

            // Run the current process
            if (current != null) {
                current.run();
                currentTime++;
                if(currentTime > SchedulingGUI.maximumTime){
                    SchedulingGUI.maximumTime = currentTime;
                }
                SchedulingGUI.gui.createRectangle(SchedulingGUI.rectangleX, current.yCoordinate, Color.GREEN);
                SchedulingGUI.rectangleX += 700/30;




                // Check if the process is completed
                if (current.remainingTime == 0) {
                    current.setIsCompleted(true);
                    current.completionTime = currentTime;
                    current.turnAroundTime = current.completionTime - current.arrivalTime;
                    current.waitingTime = current.turnAroundTime - current.burstTime;
                    readyQueue.remove(current);
                    current = null;
                }
            } else {
                // Idle time
                currentTime++;
                SchedulingGUI.rectangleX += 700/30;
            }

            // Break when all processes are completed
            if (processes.stream().allMatch(p -> p.isCompleted)) break;
        }

        // Display results
        System.out.println("\nProcess\tArrival\tBurst\tCompletion\tWaiting\tTurnaround");
        for (Process process : processes) {
            System.out.printf("%s\t%d\t%d\t%d\t\t%d\t%d\n",
                    process.name, process.arrivalTime, process.burstTime,
                    process.completionTime, process.waitingTime, process.turnAroundTime);
        }

        System.out.println("Total Context Switches: " + contextSwitches);

        // Calculate and display average waiting time
        double avgWaitingTime = processes.stream()
                .mapToInt(p -> p.waitingTime)
                .average()
                .orElse(0);
        System.out.println("Average Waiting Time: " + avgWaitingTime);
        SchedulingGUI.gui.finalSummary(avgWaitingTime, 0);
    }
}