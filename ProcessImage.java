public class ProcessImage
{
    /*
    This class is the Image of the Process. It acts like a snapshot and contains information between the actual process
    and the PCB
     */
    private PCB Pcb_data;
    private String code; //Original CPU_IO_Sequence
    private int positionCounter; //Position in array
    private int[] burstData;

    public ProcessImage(String process)
    {
        positionCounter = 0;
        setCode(process);
        constructData();
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
            temp[i] = Character.getNumericValue(burstSequence.charAt(i));

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

    public void updateBurstValue() //moves forward one in burst array
    {
        positionCounter++;
    }

    public boolean isFinalCPUBurst()
    {
        if ((positionCounter + 1) >= totalBurst())
            return true;
        else
            return false;
    }

    public boolean isFirstIOBurst()
    {
        if (positionCounter == 1)
            return true;
        else
            return false;
    }

    public void setState(String state)
    {
        getPcb_data().setState(state);
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
