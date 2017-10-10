import java.util.Random;

public class CPU implements Runnable
{
    private boolean BusyOrNot;
    public int PC; //Your CPU only has one register PC
    private int done;
    private Process process;
    private int timeslice;

    private int processCount;

    public CPU(int settimeslice)
    {
        timeslice = settimeslice;
        BusyOrNot = false;
        done = 0;
    }

    private void execute(Process process)
    {
        BusyOrNot = true;

        long startTime = System.currentTimeMillis();

        int cycles = 0;
        int cyclesRemaining = 0;

        if (process.getRegisterValue() != 0)
        {
            PC = process.getRegisterValue();
            cycles = PC;
            if (PC - timeslice <= 0)
                cyclesRemaining = 0;
            else
                cyclesRemaining = PC - timeslice;
        }

        else
            {
                if (timeslice != 0)
                    cycles = timeslice;

                else
                    cycles = process.getBurstValue();

                cyclesRemaining = process.getBurstValue() - cycles;
            }

        for (int i = 0; i <= cycles; i++)
            BubbleSort();

       // if (cyclesRemaining > 0)
        process.adjustBurst(cyclesRemaining);

        BusyOrNot = false;

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        //process.setProcessTime(totalTime);

        done = 1;
        /* read the CPU burst number, say #, from the position PositionOfNextInstructionToExecute of P.
        Repeat calling Bubble Sort() for # times and then continue.
        If the code runs out, return (PositionOfNextInstructionToExecute, “terminated”), then OS put it back to the terminated queue.
        If the slice of time (restricted number of calling Bubble sort() for a process each time) runs out, return
        (PositionOfNextInstructionToExecute+1, “ready”), then OS put it back to the ready queue.
        Otherwise, return (PositionOfNextInstructionToExecute+1, “wait”) (namely, P has an I/O request and then OS remove it from the ready queue and sent it to I/O queue)
        */

                //new Pair(0,0);
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

    public void run()
    {
        execute(process);
        processCount++;
    }
}
