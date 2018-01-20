import java.util.Random;

/*
CPU class acts like a CPU and accepts bursts from the OS
 */

public class CPU implements Runnable
{
    private boolean BusyOrNot;
    private int PC; //Register for CPU
    private int done;
    private int processCount;
    private int timeslice;
    private long totalProcessingTime;
    private Process process;

    public CPU(int settimeslice)
    {
        timeslice = settimeslice;
        BusyOrNot = false;
        done = 0;
    }

    public void run() //Thread is used for CPU so it does not interfere with Main Thread
    {
        execute(process);
    }

    private void execute(Process process)
    {
        BusyOrNot = true;

        long startTime = System.currentTimeMillis();

        int cycles = 0;
        int cyclesRemaining = 0;

        if (process.getRegisterValue() != 0) //Checks to see what the register value is in PCB
        {                                    //If the value is 0, it is not loaded
            PC = process.getRegisterValue();
            cycles = PC;
            if (PC - timeslice <= 0)
                cyclesRemaining = 0;
            else
                cyclesRemaining = PC - timeslice; //Calculates how many burst remain from register value
        }

        else
            {
                if (timeslice != 0) //Timeslice is used to simulate time and how many burst to process at a time
                    cycles = timeslice;

                else
                    cycles = process.getBurstValue();

                cyclesRemaining = process.getBurstValue() - cycles;
            }

        for (int i = 1; i < cycles; i++) //Executes BubbleSort
            BubbleSort();

        process.adjustBurst(cyclesRemaining);

        BusyOrNot = false;

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        totalProcessingTime += totalTime;

        processCount++;

        done = 1; //Signals CPU is done
    }

    private void BubbleSort()
    {
        int[] bubbleSort = new int[1000];
        Random random = new Random();
        int x;
        //Creates the random 1,000 integers
        for (int i = 0; i < 1000; i++) {
            x = random.nextInt(10000) + 1;
            bubbleSort[i] = x;
        }
        //Bubble sort algorithm
        int temp;
        for (int i = 0; i < bubbleSort.length; i++){
            for (int j = 1; j < bubbleSort.length; j++){
                if (bubbleSort[j-1] > bubbleSort[j]){
                    temp = bubbleSort[j-1];
                    bubbleSort[j-1] = bubbleSort[j];
                    bubbleSort[j] = temp;
                }
            }
        }
    }

    public int calculateThroughput()
    {
        Long temp = getTotalProcessingTime() / processCount;
        processCount = 0;
        setTotalProcessingTime(0);
        return temp.intValue();
    }

    public Long getTotalProcessingTime()
    {
        return totalProcessingTime;
    }

    public void setTotalProcessingTime(int value)
    {
        totalProcessingTime = value;
    }

    public void setProcess(Process process)
    {
        this.process = process;
    }

    public Process getProcess()
    {
        return process;
    }

    public void setTimeSlice(int newSlice)
    {
        timeslice = newSlice;
    }

    public Boolean CPUisBusy()
    {
        return BusyOrNot;
    }

    public int isDone()
    {
        return done;
    }
}
