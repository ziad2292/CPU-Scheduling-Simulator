import java.awt.*;
import java.util.*;
import java.util.List;

public class PriorityScheduling implements scheduler{
    int currentTime = 0, contextSwitches = 0;
    Process current = null;
    int waitCounter;

    @Override
    public void execute(List<Process> processes, int contextSwitchTime){
        //Sort by arrival time
        processes.sort(Comparator.comparingInt(p -> p.getArrivalTime()));

        List<Process> completedProcesses = new ArrayList<>();

        //Simulation
        while(!processes.isEmpty()) {

            // Select the process with the highest priority
            Process highestPriority = null;
            for (Process process : processes) {
                if(process.getArrivalTime() <= currentTime){
                    if (highestPriority == null || process.getPriority() < highestPriority.getPriority()) {
                        highestPriority = process;
                    }
                }
            }

            if(highestPriority == null){
                currentTime++;
                continue;
            }

            // Check if the process has changed (context switch)
            if (highestPriority != current) {
                SchedulingGUI.gui.createProcess(highestPriority);
                contextSwitches++;
                currentTime += contextSwitchTime; // Simulate context switch time
                current = highestPriority;
                SchedulingGUI.rectangleX = 100;
                if(current.yCoordinate == -1){
                    current.yCoordinate = SchedulingGUI.rectangleY;
                    SchedulingGUI.rectangleY += 50;
                }
            }

            waitCounter = currentTime;

            // Run
            currentTime += current.burstTime;
            current.completionTime = currentTime;
            current.turnAroundTime = current.completionTime - current.arrivalTime;
            current.waitingTime = current.turnAroundTime - current.burstTime;
            if(currentTime > SchedulingGUI.maximumTime){
                SchedulingGUI.maximumTime = currentTime;
            }
            for(int i = 0; i < waitCounter-1; i++) {
                SchedulingGUI.rectangleX += 700/30;
            }
            for(int i = 0; i < current.burstTime; i++) {
                SchedulingGUI.gui.createRectangle(SchedulingGUI.rectangleX, current.yCoordinate, Color.GREEN);
                SchedulingGUI.rectangleX += 700/30;
            }




            // Add to completed list and remove from pending processes
            completedProcesses.add(current);
            processes.remove(current);
        }
        // Display results
        System.out.println("\nProcess\tArrival\tBurst\tPriority\tWaiting\tTurnaround\tCompletion");
        for (Process process : completedProcesses) {
            System.out.printf("%s\t%d\t%d\t%d\t\t%d\t%d\t\t%d\n",
                    process.name, process.arrivalTime, process.burstTime, process.priorityNumber,
                    process.waitingTime, process.turnAroundTime, process.completionTime);
        }

        System.out.println("Total Context Switches: " + contextSwitches);
    }

}