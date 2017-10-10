import java.util.*;

public class Algorithms
{
    private Queue<String> FCFS = new LinkedList();
    private Queue<String> RR = new LinkedList<>();

    //private PriorityQueue<String> Static = new PriorityQueue<>();

    private Timer timer = new Timer();

    public void firstComeFirstServeAdd(String object)
    {
        FCFS.add(object);
    }

    public String firstComeFirstServeRemove()
    {
        return FCFS.remove();
    }

    public void RoundRobin()
    {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                //Execute Task
                //Check to see if complete
                //If so, move on to next
                //If not, move on to next item in queue

            }
        },0, 20);

        if (RR.isEmpty())
            timer.cancel();
    }

    public void staticPriority()
    {
        //Check to see priority
        //If priority is 3 or greater
        //Round robin
    }

}
