import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.io.File;
import java.util.*;

public class OS
{
    private int userChoice;
    private int processCounter;
    private CPU cpu;
    private IODevice io;
    private boolean isCPUAvailable;
    private ProcessImage processImage;
    private Queue<Process> New_Queue;
    private Queue<Process> Ready_Queue;
    private Queue<Process> Wait_Queue;
    private Queue<Process> Terminated_Queue;

    //Read the txt input file, for each line, create a process and record its arrivaltime
    //Put each process in New_Q queue initially then put them in Ready_Q //Always check whether the
    // CPU is idle or not; if yes, use your scheduleralgorithm to select a process from the Ready_Queue for CPU execution\
    // According to the return value of CPU execute(), put the process into the corresponding queue.
    //Record the time of every operation for computing your latency and response

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

    private void algorithm_FCFS()
    {
        CPU cpu = new CPU(0);
        IODevice io = new IODevice();

        Process process = null;

        System.out.println("First Come First Serve");

        while (processCounter > Terminated_Queue.size())
        {
            if (!cpu.CPUisBusy()) //cpu is not busy
            {
                if (cpu.getProcess() == null) //cpu is empty - idle
                {
                    process = Ready_Queue.poll();
                    process.setState("RUNNING");
                    process.getImage().getPcb_data().setStartTime();
                    cpu.setProcess(process);
                    cpu.run();
                }
                else if (cpu.getProcess() != null) //cpu is has process on it
                {
                    if (cpu.isDone() == 1 && cpu.getProcess().isProcessComplete())
                    {
                        process = cpu.getProcess();
                        process.setState("TERMINATED");
                        process.getImage().getPcb_data().setEndTime();
                        Terminated_Queue.add(process);
                        cpu.setProcess(null);
                    }
                    else if (cpu.isDone() == 1) //cpu is done with process
                    {
                        process = cpu.getProcess();
                        process.updateBurst();
                        process.setState("WAITING");
                        Wait_Queue.add(process);
                        cpu.setProcess(null);
                    }
//                    else if (cpu.getProcess().isProcessComplete()) //process is complete
//                    {
//                        process = cpu.getProcess();
//                        process.setState("TERMINATED");
//                        Terminated_Queue.add(process);
//                    }
                }
            }

             //io time
            if (!io.IOisBusy())
            {
                if (!Wait_Queue.isEmpty())
                {
                process = Wait_Queue.poll();
                process.getImage().getPcb_data().setStartTimeIO();
                io.setIOBurst(process.getBurstValue());
                io.run();
                }
                if (io.isDone() == 1 && Wait_Queue.isEmpty())
                {
                    process.updateBurst();
                    process.setState("READY");
                    process.getImage().getPcb_data().setEndTimeIO();
                    Ready_Queue.add(process);
                    io.setDone();
                }

            }
        }

        reporting();

    }

    private void algorithm_RR()
    {
        CPU cpu = new CPU(2);
        IODevice io = new IODevice();

        Process process = null;

        System.out.println("Round Robin");

        while (processCounter > Terminated_Queue.size())
        {
            if (!cpu.CPUisBusy()) //cpu is not busy
            {
                if (cpu.getProcess() == null) //cpu is empty - idle
                {
                    process = Ready_Queue.poll();
                    process.setState("RUNNING");
                    process.getImage().getPcb_data().setStartTime();
                    cpu.setProcess(process);
                    cpu.run();
                }
                else if (cpu.getProcess() != null) //cpu is has process on it
                {
                    if (cpu.isDone() == 1 && cpu.getProcess().isProcessComplete())
                    {
                        process = cpu.getProcess();
                        process.setState("TERMINATED");
                        process.getImage().getPcb_data().setEndTime();
                        Terminated_Queue.add(process);
                        cpu.setProcess(null);
                    }
                    else if (cpu.isDone() == 1) //cpu is done with process
                    {
                        process = cpu.getProcess();
                        if (process.getImage().getPcb_data().getPositionOfNextInstruction() == 0) //check to see if registry is 0
                        {
                            process.updateBurst();
                            process.setState("WAITING");
                            Wait_Queue.add(process);
                            cpu.setProcess(null);
                        }
                        else
                            {
                                process.setState("READY");
                                Ready_Queue.add(process);
                                cpu.setProcess(null);
                            }
                    }
//                    else if (cpu.getProcess().isProcessComplete()) //process is complete
//                    {
//                        process = cpu.getProcess();
//                        process.setState("TERMINATED");
//                        Terminated_Queue.add(process);
//                    }
                }
            }

            //io time
            if (!io.IOisBusy())
            {
                if (!Wait_Queue.isEmpty())
                {
                    process = Wait_Queue.poll();
                    process.getImage().getPcb_data().setStartTimeIO();
                    io.setIOBurst(process.getBurstValue());
                    io.run();
                }
                if (io.isDone() == 1 && Wait_Queue.isEmpty())
                {
                    process.updateBurst();
                    process.setState("READY");
                    process.getImage().getPcb_data().setEndTimeIO();
                    Ready_Queue.add(process);
                    io.setDone();
                }

            }
        }

        reporting();

    }

//    class PriorityChecker implements Comparator<Process>
//    {
//        @Override
//        public int compare(Process o1, Process o2) {
//            if (o1.getPriority() < o2.getPriority())
//                return 1;
//            else if (o1.getPriority() > o2.getPriority())
//                return -1;
//            else
//                return 0;
//        }
//    }

    private void algorithm_SP()
    {
//        Comparator<Process> priorityRanker = new PriorityChecker();
//        PriorityQueue<Process> tempReady = new PriorityQueue<Process>(Ready_Queue.size(), priorityRanker);

        CPU cpu = new CPU(0);
        IODevice io = new IODevice();

        Process process = null;

        System.out.println("Static Priority");

//        while (!Ready_Queue.isEmpty())
//        {
//            tempReady.add(Ready_Queue.poll());
//        }

        while (processCounter > Terminated_Queue.size())
        {
            if (!cpu.CPUisBusy()) //cpu is not busy
            {
                if (cpu.getProcess() == null) //cpu is empty - idle
                {
                    process = Ready_Queue.poll();
                    process.setState("RUNNING");
                    process.getImage().getPcb_data().setStartTime();
                    cpu.setProcess(process);
                    cpu.run();
                }
                else if (cpu.getProcess() != null) //cpu is has process on it
                {
                    if (cpu.isDone() == 1 && cpu.getProcess().isProcessComplete())
                    {
                        process = cpu.getProcess();
                        process.setState("TERMINATED");
                        process.getImage().getPcb_data().setEndTime();
                        Terminated_Queue.add(process);
                        cpu.setProcess(null);
                    }
                    else if (cpu.isDone() == 1) //cpu is done with process
                    {
                        process = cpu.getProcess();
                        if (process.getImage().getPcb_data().getPositionOfNextInstruction() == 0) //check to see if registry is 0
                        {
                            process.updateBurst();
                            process.setState("WAITING");
                            Wait_Queue.add(process);
                            cpu.setProcess(null);
                        }
                        else
                        {
                            process.setState("READY");
                            Ready_Queue.add(process);
                            cpu.setProcess(null);
                        }
                    }
//                    else if (cpu.getProcess().isProcessComplete()) //process is complete
//                    {
//                        process = cpu.getProcess();
//                        process.setState("TERMINATED");
//                        Terminated_Queue.add(process);
//                    }
                }
            }

            //io time
            if (!io.IOisBusy())
            {
                if (!Wait_Queue.isEmpty())
                {
                    process = Wait_Queue.poll();
                    process.getImage().getPcb_data().setStartTimeIO();
                    io.setIOBurst(process.getBurstValue());
                    io.run();
                }
                if (io.isDone() == 1 && Wait_Queue.isEmpty())
                {
                    process.updateBurst();
                    process.setState("READY");
                    process.getImage().getPcb_data().setEndTimeIO();
                    Ready_Queue.add(process);
                    io.setDone();
                }

            }
        }

        reporting();

    }

    private void readFile()
    {
        File burstFile = new File("src/tasks.txt");

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

    private void updateReadyQueue()
    {
        for (int i = 0; i < processCounter; i++)
        {
            Process temp;
            temp = New_Queue.poll();
            temp.setState("READY");
            Ready_Queue.add(temp);
        }
    }

    private void reporting()
    {
        System.out.println("REPORT\n");

        ArrayList<Long> totalTimes = new ArrayList<>();
        ArrayList<Long> ioTimes = new ArrayList<>();
        Toolset toolset = new Toolset();

        while (!Terminated_Queue.isEmpty())
        {
            Process p = Terminated_Queue.poll();
            totalTimes.add(p.calculateTimePCB());
            ioTimes.add(p.getImage().getPcb_data().calculateTotalIOTime());

            System.out.println("Process ID: " + p.getId());
            System.out.println("Latency (ms): " + p.calculateTimePCB());
            System.out.println("Response (ms): " + p.getImage().getPcb_data().calculateTotalIOTime());
        }
    }
}
