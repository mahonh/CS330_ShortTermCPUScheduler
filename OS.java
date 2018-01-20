import java.io.File;
import java.util.*;

/*
OS Class is the where the algorithms are stored, CPU and IO Devices are managed
 */

public class OS
{
    private int userChoice;
    private int processCounter;
    private Queue<Process> New_Queue;
    private Queue<Process> Ready_Queue;
    private Queue<Process> Wait_Queue;
    private Queue<Process> Terminated_Queue;

    public OS(int userChoice)
    {
        New_Queue = new LinkedList<Process>();
        Ready_Queue = new LinkedList<Process>();
        Wait_Queue = new LinkedList<Process>();
        Terminated_Queue = new LinkedList<Process>();

        this.userChoice = userChoice;
        processCounter = 0;

        readFile();

        initialize();
    }

    private void initialize()
    {
        switch (userChoice)
        {
            case 1:
                algorithm_FCFS();
                break;
            case 2:
                algorithm_RR();
                break;
            case 3:
                algorithm_SP();
                break;
        }
    }

    /*
    FCFS Algorithm simple goes through all of the processes in queue and processes all of them in the order
    they appeared in the queue
     */

    private void algorithm_FCFS()
    {
        CPU cpu = new CPU(0);
        IODevice io = new IODevice();

        Process process = null;

        System.out.println("First Come First Serve");

        while (processCounter > Terminated_Queue.size())
        {
            if (!cpu.CPUisBusy()) //Proceeds if the CPU is not busy
            {
                if (cpu.getProcess() == null) //CPU is idle, so new process in added to it
                {
                    process = Ready_Queue.poll();
                    process.setState("RUNNING");
                    cpu.setProcess(process);
                    cpu.run();
                }
                else if (cpu.getProcess() != null) //CPU has a process on it
                {
                    if (cpu.isDone() == 1 && cpu.getProcess().isProcessComplete()) //Process is complete
                    {                                                              //Process is moved to Terminated Queue
                        process = cpu.getProcess();
                        process.setState("TERMINATED");
                        process.getImage().getPcb_data().setEndTime();
                        process.setCPUThroughput(cpu.calculateThroughput());
                        Terminated_Queue.add(process);
                        cpu.setProcess(null); //CPU is idle again
                    }
                    else if (cpu.isDone() == 1) //CPU is done with process, but the process needs to go to IO
                    {
                        process = cpu.getProcess();
                        process.updateBurst();
                        process.setState("WAITING");
                        Wait_Queue.add(process);
                        cpu.setProcess(null);
                    }
                }
            }

            if (!io.IOisBusy()) //Proceeds if the IODevice is not busy
            {
                if (!Wait_Queue.isEmpty()) //Checks to see if the Wait Queue is empty
                {
                process = Wait_Queue.poll();
                io.setIOBurst(process.getBurstValue()); //Adds new IO Burst to IO Device
                io.run();
                }
                if (io.isDone() == 1 && Wait_Queue.isEmpty()) //Burst is complete
                {
                    if (process.getImage().isFirstIOBurst())
                        process.getImage().getPcb_data().setEndTimeIO();
                    process.updateBurst();
                    process.setState("READY"); //Process is added back to Ready Queue for CPU
                    Ready_Queue.add(process);
                    io.setDone();
                }

            }
        }

        reporting();

    }

    /*
    Round Robin Algorithm is used to give all processes equal time
     */

    private void algorithm_RR()
    {
        CPU cpu = new CPU(2); //CPU with TimeSlice of 2
        IODevice io = new IODevice();

        Process process = null;

        System.out.println("Round Robin");

        while (processCounter > Terminated_Queue.size())
        {
            if (!cpu.CPUisBusy()) //Proceeds if CPU is not busy
            {
                if (cpu.getProcess() == null) //CPU is idle, new process is added
                {
                    process = Ready_Queue.poll();
                    process.setState("RUNNING");
                    cpu.setProcess(process);
                    cpu.run();
                }
                else if (cpu.getProcess() != null) //CPU has a process on it
                {
                    if (cpu.isDone() == 1 && cpu.getProcess().isProcessComplete()) //Process is complete and ready to Terminate
                    {
                        process = cpu.getProcess();
                        process.setState("TERMINATED");
                        process.getImage().getPcb_data().setEndTime();
                        process.setCPUThroughput(cpu.calculateThroughput());
                        Terminated_Queue.add(process);
                        cpu.setProcess(null);
                    }
                    else if (cpu.isDone() == 1) //Process is complete
                    {
                        process = cpu.getProcess();
                        if (process.getImage().getPcb_data().getPositionOfNextInstruction() == 0) //check to see if registry is 0
                        {                                                                         //if so, move to Wait Queue
                            process.updateBurst();
                            process.setState("WAITING");
                            Wait_Queue.add(process);
                            cpu.setProcess(null);
                        }
                        else
                            {
                                process.setState("READY"); //Process moves back to Ready Queue because Register value
                                Ready_Queue.add(process);  //is above zero, which means it still has a CPU burst before
                                cpu.setProcess(null);      //it can move to the IO Device
                            }
                    }
                }
            }

            if (!io.IOisBusy()) //Proceed if IO Device is not busy
            {
                if (!Wait_Queue.isEmpty())
                {
                    process = Wait_Queue.poll();
                    io.setIOBurst(process.getBurstValue()); //Adds new IO Burst to IO Device
                    io.run();
                }
                if (io.isDone() == 1 && Wait_Queue.isEmpty()) //Burst is complete and ready to head back to CPU
                {
                    if (process.getImage().isFirstIOBurst())
                        process.getImage().getPcb_data().setEndTimeIO();
                    process.updateBurst();
                    process.setState("READY");
                    Ready_Queue.add(process);
                    io.setDone();
                }

            }
        }

        reporting();

    }

    /*
    Comparator for Static Priority Algorithm
     */

    class ArrivalPriorityChecker implements Comparator<Process>
    {
        @Override
        public int compare(Process o1, Process o2) {
            if (o1.getArrivalOrder() < o2.getArrivalOrder())
                return -1;
            else if (o1.getArrivalOrder() > o2.getArrivalOrder())
                return 1;
            else
                return 0;
        }
    }

    /*
    Static Priority Algorithm is used so the Highest Priority Process get completed first and remaining processes get
    a Round Robin style completion
     */

    private void algorithm_SP()
    {
        Comparator<Process> priorityRanker = new ArrivalPriorityChecker();

        /*
        Priority Queue is used to maintain order of Processes by Arrival Order
        This is to aid in maintaining order with the TimeSlice, so a process added
        back to the Ready Queue won't go to the end, but rather beginning again
         */

        PriorityQueue<Process> tempReady = new PriorityQueue<Process>(Ready_Queue.size(), priorityRanker);

        CPU cpu = new CPU(0);
        IODevice io = new IODevice();

        Process process = null;

        System.out.println("Static Priority");

        while (!Ready_Queue.isEmpty()) //Sets up Temp Ready Queue for use
        {
            tempReady.add(Ready_Queue.poll());
        }

        Ready_Queue.clear();

        while (processCounter > Terminated_Queue.size())
        {
            if (!cpu.CPUisBusy()) //Proceed if CPU is not busy
            {
                if (cpu.getProcess() == null) //CPU is idle
                {
                    process = tempReady.poll();
                    process.setState("RUNNING");

                    /*
                    Checks current process against next process in Priority Queue to determine if
                    the process in the Queue has a higher priority. If it does then a TimeSlice is setup
                    to use with the current process
                     */

                    if (!tempReady.isEmpty())
                    {
                        if (process.getPriority() < tempReady.peek().getPriority())
                            cpu.setTimeSlice(tempReady.peek().getArrivalOrder());
                    }

                    cpu.setProcess(process);
                    cpu.run();
                }
                else if (cpu.getProcess() != null) //CPU has a process on it
                {
                    if (cpu.isDone() == 1 && cpu.getProcess().isProcessComplete()) //Process is complete and ready to Terminate
                    {
                        process = cpu.getProcess();
                        process.setState("TERMINATED");
                        process.getImage().getPcb_data().setEndTime();
                        cpu.setTimeSlice(0); //TimeSlice is reset to 0 for new Processes
                        process.setCPUThroughput(cpu.calculateThroughput());
                        Terminated_Queue.add(process);
                        cpu.setProcess(null);
                    }
                    else if (cpu.isDone() == 1) //Process is done
                    {
                        process = cpu.getProcess();
                        if (process.getImage().getPcb_data().getPositionOfNextInstruction() == 0) //Check to see if registry is 0
                        {
                            process.updateBurst();
                            process.setState("WAITING");
                            Wait_Queue.add(process);
                            cpu.setProcess(null);
                        }
                        else if (!tempReady.isEmpty())
                        {
                            /*
                            Checks to see if the current Process has a lower Priority than the one in the Queue.
                            If so, then the current process in put back on the queue to retrieve the one with the
                            higher priority
                             */

                            if (tempReady.peek().getPriority() > cpu.getProcess().getPriority())
                            {
                                Process temp = tempReady.poll();
                                process = cpu.getProcess();
                                process.setState("READY");
                                tempReady.add(process);
                                cpu.setProcess(temp);
                            }
                        }
                        else
                        {
                            /*
                            Adds a process back to the queue which has not completed its timeslice
                            (Still has a register value > 0)
                             */

                            if (!tempReady.isEmpty())
                            {
                                Process temp = tempReady.poll();
                                process.setState("READY");
                                tempReady.add(process);
                                cpu.setTimeSlice(0);
                                cpu.setProcess(temp);
                            }
                            else
                            {
                                process.updateBurst(); //Process ready to IO Device
                                process.setState("WAITING");
                                Wait_Queue.add(process);
                                cpu.setProcess(null);
                            }
                        }
                    }
                }
            }

            if (!io.IOisBusy()) //Proceeds if IO Device is not busy
            {
                if (!Wait_Queue.isEmpty())
                {
                    process = Wait_Queue.poll();
                    io.setIOBurst(process.getBurstValue()); //Adds Burst for IO
                    io.run();
                }
                if (io.isDone() == 1 && Wait_Queue.isEmpty())
                {
                    if (process.getImage().isFirstIOBurst())
                        process.getImage().getPcb_data().setEndTimeIO();
                    process.updateBurst();
                    process.setState("READY"); //Ready for another CPU round
                    tempReady.add(process);
                    io.setDone();
                }

            }
        }

        reporting();

    }

    /*
    Reads info from file and creates Processes
     */

    private void readFile()
    {
        File burstFile = new File("tasks.txt");

        try{
            Scanner scany = new Scanner(burstFile);

            while (scany.hasNextLine())
            {
                Process process = new Process(scany.nextLine());
                process.setState("NEW");
                New_Queue.add(process);
                processCounter++;
            }

        } catch (Exception e) {
            System.out.println("No file found!");
        }

        updateReadyQueue();
    }

    private void updateReadyQueue() //Updates ready queue with new Processes
    {
        for (int i = 0; i < processCounter; i++)
        {
            Process temp;
            temp = New_Queue.poll();
            temp.setState("READY");
            temp.getImage().getPcb_data().setStartTime();
            Ready_Queue.add(temp);
        }
    }

    /*
    Reporting section with data
     */

    private void reporting()
    {
        System.out.println("\nREPORT");
        System.out.println("-------\n");

        ArrayList<Long> totalTimes = new ArrayList<>();
        ArrayList<Long> ioTimes = new ArrayList<>();
        ArrayList<Integer> cpuThroughput = new ArrayList<>();
        Toolset toolset = new Toolset();

        while (!Terminated_Queue.isEmpty())
        {
            Process p = Terminated_Queue.poll();
            totalTimes.add(p.calculateTimePCB());
            ioTimes.add(p.calculateIOTimePCB());
            cpuThroughput.add(p.getCPUThroughput());

            System.out.println("Process ID: " + p.getId());
            System.out.println("Latency (ms): " + p.calculateTimePCB());
            System.out.println("Response (ms): " + p.calculateIOTimePCB());
            System.out.println("CPU Throughput: " + p.getCPUThroughput() + " per (ms)");
            System.out.println();
        }

        System.out.println("\nStandard Deviation of Latency (ms): " + toolset.standardDeviation(totalTimes));
        System.out.println("Standard Deviation of Response (ms): " + toolset.standardDeviation(ioTimes));
        System.out.println("Standard Deviation of CPU Throughput: " + toolset.standardDeviation(cpuThroughput) + " per (ms)");

        System.out.println("\nAverage of Latency (ms): " + toolset.average(totalTimes));
        System.out.println("Average of Response (ms): " + toolset.average(ioTimes));
        System.out.println("Average of CPU Throughput: " + toolset.average(cpuThroughput) + " per (ms)");

        System.out.println("\nMin Latency (ms): " + getMin(totalTimes));
        System.out.println("Min Response (ms): " + getMin(ioTimes));
        System.out.println("Min CPU Throughput: " + getIntMin(cpuThroughput) + " per (ms)");

        System.out.println("\nMax Latency (ms): " + getMax(totalTimes));
        System.out.println("Max Response (ms): " + getMax(ioTimes));
        System.out.println("Max CPU Throughput: " + getIntMax(cpuThroughput) + " per (ms)");
    }

    private double getMin(ArrayList<Long> input)
    {
        Long min = input.get(0);
        for (Long x: input)
            if (x < min)
                min = x;
        return min;
    }

    private double getMax(ArrayList<Long> input)
    {
        Long max = input.get(0);
        for (Long x: input)
            if (x > max)
                max = x;
        return max;
    }

    private int getIntMin(ArrayList<Integer> input)
    {
        int min = input.get(0);
        for (int x: input)
            if (x < min)
                min = x;
        return min;
    }

    private int getIntMax(ArrayList<Integer> input)
    {
        int max = input.get(0);
        for (int x: input)
            if (x > max)
                max = x;
        return max;
    }
}

