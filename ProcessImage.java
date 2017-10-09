public class ProcessImage
{
    private PCB Pcb_data;
    private String code; //CPU_IO_Sequence
    private int nextBurstPosition;
    private int positionCounter; //Position in array
    private int[] burstData;

    public ProcessImage(String process)
    {
        positionCounter = 0;
        setCode(process);
        constructData();

        //set PCB data, code and others
        //set state as "NEW";
    }

    private void constructData()
    {
        String[] data = getCode().split(" ");

        int id = Integer.parseInt(data[0]);
        int arrivalOrder = Integer.parseInt(data[1]);
        int priority = Integer.parseInt(data[2]);
        String burstSequence = data[3];

        int[] temp = new int[burstSequence.length()];

        for (int i = 0; i < burstSequence.length(); i++)
            temp[i] = burstSequence.charAt(i);

        setBurstData(temp);

        setPcb_data(new PCB("NEW", id, priority, arrivalOrder));
    }


    public int getRegisterValue()
    {
        return getPcb_data().getPositionOfNextInstruction();
    }

    public void setRegisterValue(int valueRemaining)
    {
        getPcb_data().setPositionOfNextInstruction(valueRemaining);
    }


    public int getBurstValue()
    {
        return getBurstData(positionCounter);
    }

    public void updateBurstValue() //moves forward one
    {
        positionCounter++;
    }

    public boolean isBurstCPU()
    {
        if (getBurstValue()%2 == 0)
            return true;
        else
            return false;
    }

    public boolean isFinalCPUBurst()
    {
        if (positionCounter == totalBurst())
            return true;
        else
            return false;
    }


    public void setState(String state)
    {
        getPcb_data().setState(state);
    }

    public String getState()
    {
        return getPcb_data().getState();
    }

    public int getID()
    {
        return getPcb_data().getId();
    }

    public int getPriority()
    {
        return getPcb_data().getPriority();
    }

    public PCB getPcb_data()
    {
        return Pcb_data;
    }

    public void setPcb_data(PCB pcb_data)
    {
        Pcb_data = pcb_data;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public int getBurstData(int position)
    {
        return burstData[position];
    }

    public void setBurstData(int[] burstData)
    {
        this.burstData = burstData;
    }

    public int totalBurst()
    {
        return burstData.length;
    }
}
