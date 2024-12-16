public class Process {
    public String name;
    public int priority;
    public int arrivalTime;
    public int remainingBurstTime;
    public double factor;
    public int quantum;
    public int waitingTime;
    public int turnAroundTime;
    public String color;
    public int burstTime, priorityNumber, remainingTime, completionTime;
    boolean isCompleted = false;
    int yCoordinate = -1;
    public int startTime;


    public Process(String name, int priority, int arrivalTime, int burstTime, int quantum) {
        this.name = name;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.remainingBurstTime = burstTime;
        this.quantum = quantum;
    }

    public Process(String name, String color, int arrivalTime,int burstTime,int priorityNumber){
        this.priorityNumber = priorityNumber;
        this.name = name;
        this.color = color;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        remainingTime = burstTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setIsCompleted(boolean status){
        isCompleted = status;
    }

    public boolean isCompleted(){
        return  isCompleted;
    }

    public void run(){
        remainingTime--;
    }

    public int getPriority(){
        return priorityNumber;
    }

    public String getProcessName() {
        return name;
    }
}