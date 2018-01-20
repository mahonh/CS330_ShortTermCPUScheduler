import java.util.Random;

/*
IO Device class acts like an IO Device and accepts bursts from OS
 */

public class IODevice implements Runnable
{
    private boolean BusyOrNot;
    private int IO_burst;
    private int done;

    public IODevice()
    {
        setBusyOrNot(false);
        setDone(0);
    }

    public void run() //Thread is used for IO Device so it does not interfere with Main thread
    {
        execute(getIOBurst());
    }

    /*
    Acts like IO Device and runs BubbleSort for set number of times
     */
    public void execute(int IO_burst)
    {
        setBusyOrNot(true);

        for (int i = 1; i < IO_burst; i++)
            BubbleSort();

        setBusyOrNot(false);

        setDone(1); //Used to signal the IO Device is complete
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
        return getDone();
    }

    public void setDone()
    {
        setDone(0);
    }

    public boolean IOisBusy()
    {
        return isBusyOrNot();
    }

    public void setIOBurst(int IO_burst)
    {
        this.setIO_burst(IO_burst);
    }

    public int getIOBurst()
    {
        return getIO_burst();
    }

    public boolean isBusyOrNot()
    {
        return BusyOrNot;
    }

    public void setBusyOrNot(boolean busyOrNot)
    {
        BusyOrNot = busyOrNot;
    }

    public int getIO_burst()
    {
        return IO_burst;
    }

    public void setIO_burst(int IO_burst)
    {
        this.IO_burst = IO_burst;
    }

    public int getDone()
    {
        return done;
    }

    public void setDone(int done)
    {
        this.done = done;
    }
}
