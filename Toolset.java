import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Toolset<T extends Number>
{
    /**
     * This method calculates the standard deviation for a Collection of items.
     * @param collection Accepts any collection
     * @return Returns a double primitive
     */
    public double standardDeviation(Collection<T> collection)
    {
        double average = average(collection);
        double temp = 0;
        for (Number x : collection)
            temp += (x.doubleValue() - average) * (x.doubleValue() - average);
        return Math.sqrt(temp / (collection.size()-1));
    }

    /**
     * This method calculates the average for a Collection of items
     * @param collection Accepts any collection
     * @return Returns a double primitive
     */
    public double average(Collection<T> collection)
    {
        double sum = 0;
        for (Number x : collection)
            sum += x.doubleValue();
        return sum / collection.size();
    }

    /**
     * This method calculates the time to complete a specified method. It is
     * currently setup to work with the testFunction method.
     * @return Returns a long primitive
     */
    public long timer()
    {
        long startTime = System.nanoTime();
        testFunction();
        long finalTime = System.nanoTime();
        return finalTime - startTime;
    }

    /**
     * This method calculates the Standard Deviation and Average of the time taken
     * to complete a function for a specified number of times.
     * @return Returns a String with Standard Deviation and Average
     */
    public String repeat(int count)
    {
        ArrayList<Long> times = new ArrayList<>();
        int x = 0;
        do {
            times.add(timer());
            x++;
        } while (x < count);

        return "Standard Deviation: " + ((standardDeviation((Collection<T>) times))/1000000) + " ms" + "\n"
                + "Average: " + ((average((Collection<T>) times))/1000000) + " ms";
    }

    /**
     * This method generates 1,000 random integers and uses a bubble sort algorithm
     * to sort them. It is used with the timer method.
     */
    private void testFunction()
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
}
