public class Process
{
    /*
    This is the Process class and is at the top and communicates to the ProcessImage and PCB
     */
    private int id;
    private int priority;
    private int cpuThroughput;
    private ProcessImage image;

    public Process(String process)
    {
        setImage(new ProcessImage(process));
        setId(getImage().getID());
        setPriority(getImage().getPriority());
    }

    public void adjustBurst(int remainValue) //update with timeslice from CPU (Sets register value in PCB)
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

    public void setCPUThroughput(int value)
    {
        cpuThroughput = value;
    }

    public int getCPUThroughput()
    {
        return cpuThroughput;
    }
}
