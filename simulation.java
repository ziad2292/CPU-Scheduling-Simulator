import java.util.*;

import javax.swing.SwingUtilities;

public class simulation {
    static Scanner sc = new Scanner(System.in);
    static int contextSwitchTime;
    static int choice;
    
    static List<Process> processes;
    
        public static void main(String[] args) {

               // This is the main method that will run the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SchedulingGUI(); // Initialize the SchedulingGUI instance
            }
        });


            /*while (true) {
                    if(gui.selectedButton.equals("Priority Scheduling")){
                        input(false ,true, false);
                        new PriorityScheduling().execute(processes, contextSwitchTime);
                    }
                      
                    else if(gui.selectedButton.equals("SJF")){
                        input(false ,false, true);
                        new sjfScheduling().execute(processes, contextSwitchTime);
                    }
                        
                    else if(gui.selectedButton.equals("SRTF")){
                        input(false ,false,false);
                        new srtfScheduling().execute(processes, contextSwitchTime);
                    }
                        
                    else if(gui.selectedButton.equals("FCAI Scheduling")){
                        input(true,true,false);
                        new fcaiScheduling().execute(processes, contextSwitchTime);
                    }
                    else{
                        break;
                    }

            }*/
            
        //sc.close();
    }


    public static void input(boolean isFCAI, boolean isPriority, boolean isSJF){
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        processes = new ArrayList<>();

        int timeQuantum = 0;
        int priority = 0;
        
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Process Name: ");
            String name = sc.next();

            System.out.print("Enter Burst Time: ");
            int burstTime = sc.nextInt();

            System.out.print("Enter Arrival Time: ");
            int arrivalTime = sc.nextInt();

            if(isPriority){
                System.out.print("Enter Priority: ");
                priority = sc.nextInt();
            }
            
            if(isFCAI){
                System.out.print("Enter Time Quantum ");
                timeQuantum = sc.nextInt();
            }

            if(isFCAI &&  isPriority && !isSJF){
                processes.add(new Process(name,priority, arrivalTime, burstTime, timeQuantum));
            }
            else if(isSJF && !isFCAI && !isPriority){
                processes.add(new Process(name,0, arrivalTime, burstTime, 0));
            }
            else if(isPriority && !isFCAI && !isSJF){
                processes.add(new Process(name, "null", arrivalTime, burstTime, priority));
            }
            else if(!isFCAI && !isPriority && !isSJF){
                System.out.println("SRTF choice");
                processes.add(new Process(name, "null", arrivalTime, burstTime, 0));
            }
            
        }

        System.out.print("Enter Context Switching Time: ");
        contextSwitchTime = sc.nextInt();
    }

}