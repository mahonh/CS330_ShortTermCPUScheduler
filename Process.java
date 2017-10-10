public class Process
{
    private int id;
    private int priority;
    private int ordering;
    private long processTime;
    private ProcessImage image;

    public Process(String process)
    {
        setImage(new ProcessImage(process));
        setId(getImage().getID());
        setPriority(getImage().getPriority());
    }

    public void adjustBurst(int remainValue) //update with timeslice
    {
        getImage().setRegisterValue(remainValue);
    }

    public int getBurstValue()
    {
        return getImage().getBurstValue();
    }

    public int getRegisterValue()
    {
        return getImage().getRegisterValue();
    }

    public void setState(String state)
    {
        getImage().setState(state);
    }

    public boolean isProcessComplete()
    {
        return getImage().isFinalCPUBurst();
    }

    public void updateBurst()
    {
        getImage().updateBurstValue();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getArrivalOrder()
    {
        return getImage().getPcb_data().getArrivalOrder();
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public int getOrdering()
    {
        return ordering;
    }

    public void setOrdering(int ordering)
    {
        this.ordering = ordering;
    }

    public ProcessImage getImage()
    {
        return image;
    }

    public void setImage(ProcessImage image)
    {
        this.image = image;
    }

    public long calculateTimePCB()
    {
        return getImage().getPcb_data().calculateTotalTime();
    }

    public long calculateIOTimePCB()
    {
        return getImage().getPcb_data().calculateTotalIOTime();
    }

    public long getProcessTime()
    {
        return processTime;
    }

    public void setProcessTime(long processTime)  //?
    {
        this.processTime = processTime;
    }
}
