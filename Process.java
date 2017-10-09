public class Process
{
    private int id;
    private int priority;
    private int ordering;
    private ProcessImage image;
    private Pair pair;

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
}
