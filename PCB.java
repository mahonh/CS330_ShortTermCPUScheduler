public class PCB
{
    /*
    This class is for the Process Control Block
     */
    private int id;
    private int priority;
    private int positionOfNextInstruction; //register value from CPU for partial completion
    private int arrivalOrder;
    private long startTime;
    private long endTime;
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

    public int getPositionOfNextInstruction() //acts like register value
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

    public long getEndTimeIO()
    {
        return endTimeIO;
    }

    public void setEndTimeIO()
    {
        this.endTimeIO = System.currentTimeMillis();
    }

    public long calculateTotalTime()
    {
        return getEndTime() - getStartTime();
    }

    public long calculateTotalIOTime()
    {
        return getEndTimeIO() - getStartTime();
    }
}
