import java.util.*;

// Process class Initialization to save each process info
class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int waitingTime;
    int turnaroundTime;
    int completionTime;

    Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.completionTime = 0;
    }
}

public class SRTFScheduling {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Number of processes
        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();

        // Process details
        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time and burst time for Process " + (i + 1) + ": ");
            int arrivalTime = scanner.nextInt();
            int burstTime = scanner.nextInt();
            processes.add(new Process(i + 1, arrivalTime, burstTime));
        }

        // Context switching time
        System.out.print("Enter context switching time: ");
        int contextSwitchTime = scanner.nextInt();

        simulateSRTF(processes, contextSwitchTime);

        scanner.close();
    }

    public static void simulateSRTF(List<Process> processes, int contextSwitchTime) {
        int currentTime = 0; // Program Timer
        int completedProcesses = 0; // No. of completed processes
        Process currentProcess = null; // Process currently working on

        List<Process> completedProcess = new ArrayList<>(); // List of completed processes

        // Find process with the shortest remaining time
        // The function runs as long as the processes are completed
        while (completedProcesses < processes.size()) {
            Process nextProcess = null;
            //Picking the next process
            for (Process p : processes) {
                //If it still has remaining time + gya abl aw m3 el current time lel program
                if (p.remainingTime > 0 && p.arrivalTime <= currentTime) {
                    // it should handle starvation
                    if (p.waitingTime>=10) {
                        nextProcess = p;
                    }
                    //lw el remaining time fi el next process akbr mn el current process, khod el el current yb2a hwa el next
                    else if (nextProcess == null || p.remainingTime < nextProcess.remainingTime) {
                        nextProcess = p;
                    }
                    //lw remaining time fi el current wel next zy b3d, khod el process ely gya el awel
                    else if (p.remainingTime == nextProcess.remainingTime && p.arrivalTime < nextProcess.arrivalTime) {
                        nextProcess = p;
                    }
                }
            }

            if (nextProcess != null) {
                // lw el next process msh hya ely gya, y3ny hy7sal context switch, then add 3al time wa2t el switching da
                if (currentProcess != nextProcess) {
                    currentTime += contextSwitchTime;
                }

                currentProcess = nextProcess;

                // Execute the process for 1 unit of time
                currentProcess.remainingTime--;
                currentTime++;

                // Check if the process is completed
                if (currentProcess.remainingTime == 0) {
                    //kda el process khlst fi el current time da
                    currentProcess.completionTime = currentTime;
                    //el turnaround hwa total time fi el system, el completed time lel process - arrived emta fi el queue
                    currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    //waiting time bta3 kol process, turn around - el burst (total time in system - execution "Burst" time)
                    currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                    completedProcesses++;

                    // Add the process to the completedProcess list
                    completedProcess.add(currentProcess);
                }
            } else {
                currentTime++;
            }
            // Avoid starvation by incrementing priority of long-waiting processes
            for (Process p : processes) {
                if (p.remainingTime > 0 && p.arrivalTime <= currentTime) {
                    p.waitingTime++;
                }
            }
        }

        // Display results in the order of completion
        System.out.println("\nProcesses Completed\tArrival Time\tBurst Time\tTurn Around Time\tWaiting Time");
        for (Process p : completedProcess) {
            System.out.println(p.pid + "\t\t\t\t\t" + p.arrivalTime + "\t\t\t\t" + p.burstTime + "\t\t\t" + p.turnaroundTime + "\t\t\t\t\t" + p.waitingTime);
        }

        double avgTAT = 0;
        double avgWT = 0;
        for (Process p : processes) {
            avgTAT += p.turnaroundTime;
            avgWT += p.waitingTime;
        }

        System.out.println("\nAverage Turnaround Time: " + (avgTAT / processes.size()));
        System.out.println("Average Waiting Time: " + (avgWT / processes.size()));
    }
}
