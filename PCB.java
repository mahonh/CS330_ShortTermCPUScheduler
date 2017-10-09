public class PCB
{
    //To do: PCB data structure of a process
    //for example: Process_id, Arrive_time, state, PositionOfNextInstructionToExecute(PC value)
    //and so on

    private int id;
    private int priority;
    private int positionOfNextInstruction; //register value partial completion
    //private int positionOfNextBurst; //array position
    private int arrivalOrder;
    private long startTime;
    private long endTime;
    private long startTimeIO;
    private long endTimeIO;
    private String state;

    public PCB(String state, int id, int priority, int arrivalOrder)
    {
        this.setState(state);
        this.setId(id);
        this.setPriority(priority);
        this.setArrivalOrder(arrivalOrder);
        setPositionOfNextInstruction(0);
    }

    public PCB(String state, int id, int priority, int nextPosition, int arrivalOrder)
    {
        this.setState(state);
        this.setId(id);
        this.setPriority(priority);
        this.setArrivalOrder(arrivalOrder);
        setPositionOfNextInstruction(nextPosition);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public void setStartTime()
    {
        this.startTime = System.currentTimeMillis();
    }

    public long getEndTime()
    {
        return endTime;
    }

    public void setEndTime()
    {
        this.endTime = System.currentTimeMillis();
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public int getPositionOfNextInstruction()
    {
        return positionOfNextInstruction;
    }

    public void setPositionOfNextInstruction(int positionOfNextInstruction)
    {
        this.positionOfNextInstruction = positionOfNextInstruction;
    }

    public int getArrivalOrder()
    {
        return arrivalOrder;
    }

    public void setArrivalOrder(int arrivalOrder)
    {
        this.arrivalOrder = arrivalOrder;
    }

    public long getStartTimeIO()
    {
        return startTimeIO;
    }

    public void setStartTimeIO()
    {
        this.startTimeIO = System.currentTimeMillis();
    }

    public long getEndTimeIO()
    {
        return endTimeIO;
    }

    public void setEndTimeIO()
    {
        this.endTimeIO = System.currentTimeMillis();
    }

//    public int getPositionOfNextBurst()
//    {
//        return positionOfNextBurst;
//    }
//
//    public void setPositionOfNextBurst(int positionOfNextBurst)
//    {
//        this.positionOfNextBurst = positionOfNextBurst;
//    }
}
