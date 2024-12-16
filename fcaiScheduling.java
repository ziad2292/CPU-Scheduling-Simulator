import java.awt.*;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class fcaiScheduling implements scheduler{

    private PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingDouble(p -> p.factor));

    @Override
    public void execute(List<Process> processes, int contextSwitchTime){

        if (processes.isEmpty()) {
            System.out.println("No processes to schedule.");
            return;
        }

        int maxBurstTime = -1;
        for(var process: processes){
            maxBurstTime = Math.max(maxBurstTime, process.remainingBurstTime);
        }

        double v1 = processes.get(processes.size()-1).arrivalTime/ 10.0;
        double v2 = maxBurstTime / 10.0;

        System.out.println("V1: " + v1 + ", V2: " + v2);


        int currTime = 0;

        while (!readyQueue.isEmpty() || !processes.isEmpty()) {

            Iterator iterator = processes.iterator();

            while (iterator.hasNext()) {
                Process process = (Process)iterator.next();
                if(process.arrivalTime <= currTime){
                    process.factor = calculateFcaiFactor(v1, v2, process);
                    SchedulingGUI.gui.createProcess(process);
                    readyQueue.add(process);
                    System.out.println(process.name + " added to ready queue with FCAI Factor: " + process.factor);
                    iterator.remove();
                }
            }

            if (readyQueue.isEmpty()) {
                if (!processes.isEmpty()) {
                    currTime = processes.get(0).arrivalTime;
                }
                continue;
            }

            Process currProcess = readyQueue.poll();

            System.out.println("Time: " + currTime + ", Executing Process " + currProcess.name);
            if(currProcess.yCoordinate == -1){
                currProcess.yCoordinate = SchedulingGUI.rectangleY;
                SchedulingGUI.rectangleY += 50;
            }

            int executionTime = Math.min(currProcess.quantum, currProcess.remainingBurstTime);
            boolean preempted = false;
            int actualExecutionTime = 0;

            for (int t = 0; t < executionTime; t++) {
                currTime++;
                actualExecutionTime++;
                SchedulingGUI.gui.createRectangle(SchedulingGUI.rectangleX, currProcess.yCoordinate, Color.GREEN);
                SchedulingGUI.rectangleX += 700 / 30;
            
                if (t >= (int)(currProcess.quantum * 0.4)) {
                    for (Process p : processes) {
                        if (p.arrivalTime <= currTime) {
                            p.factor = calculateFcaiFactor(v1, v2, p);
                            if (p.factor < currProcess.factor) {
                                preempted = true;
                                break;
                            }
                        }
                    }
                }
            
                if (preempted) break;
            }
            if(currTime > SchedulingGUI.maximumTime){
                SchedulingGUI.maximumTime = currTime;
            }

            currProcess.remainingBurstTime-=actualExecutionTime;
            System.out.println(currProcess.name + " executed for " + actualExecutionTime + " units. Remaining Burst Time: "+ (currProcess.remainingBurstTime));

            if(currProcess.remainingBurstTime > 0){
                int unusedQuantum = currProcess.quantum - actualExecutionTime;
                if(unusedQuantum>0){
                    currProcess.quantum += unusedQuantum;
                }
                else if(unusedQuantum == 0){
                    currProcess.quantum+=2;
                }

                currProcess.factor = calculateFcaiFactor(v1, v2, currProcess);
                readyQueue.add(currProcess);
                System.out.println(currProcess.name + " re-added to ready queue with updated Quantum: " + currProcess.quantum + ", FCAI Factor: " + currProcess.factor);
            }
            if(!readyQueue.isEmpty()){
                currTime+=contextSwitchTime;
                SchedulingGUI.rectangleX += (700/30)*contextSwitchTime;
            }
        }

    }

    private double calculateFcaiFactor(double v1, double v2, Process process){
        return  (10-process.priority) + Math.ceil(process.arrivalTime/v1) + Math.ceil(process.remainingBurstTime/v2);
    }

}