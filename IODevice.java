import java.util.ArrayList;
import java.util.Random;

public class IODevice implements Runnable
{
    private boolean BusyOrNot;
    private int IO_burst;
    private int done;

    public IODevice()
    {
        BusyOrNot = false;
        done = 0;
    }

    public void execute(int IO_burst)
    {
        BusyOrNot = true;
        //Call Bubble Sort() for IO_burst times and then return “ready”;

        for (int i = 0; i == IO_burst; i++)
            BubbleSort();

        BusyOrNot = false;

        done = 1;
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

    public int isDone()
    {
        return done;
    }

    public void setDone()
    {
        done = 0;
    }

    public boolean IOisBusy()
    {
        return BusyOrNot;
    }

    public void setIOBurst(int IO_burst)
    {
        this.IO_burst = IO_burst;
    }

    public int getIOBurst()
    {
        return IO_burst;
    }

    public void run()
    {
        execute(getIOBurst());
    }
}
