public class Pair<K , V>
{
    private K element0;
    private V element1;

    public Pair(K element0, V element1)
    {
        this.element0 = element0;
        this.element1 = element1;
    }

    public K getElement0()
    {
        return element0;
    }

    public V getElement1()
    {
        return element1;
    }

    public void setElement0(K element)
    {
        element0 = element;
    }

    public void setElement1(V element)
    {
        element1 = element;
    }
}
