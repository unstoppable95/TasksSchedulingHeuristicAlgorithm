import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Mutation {

    private List<Job> jobList;
    private int listSize;

    public Mutation(List<Job> example){
        this.jobList=new ArrayList<>(example);
        this.listSize=jobList.size();
    }

    public List<Job> simpleSwap(){
        int idxJobForSwap=new Random().nextInt(listSize);
        int idxJobForSwap2=new Random().nextInt(listSize);
        List<Job> swapList = new ArrayList<>(jobList);

        Collections.swap(swapList, idxJobForSwap, idxJobForSwap2);

        return swapList;
    }

    public List<Job> maxABSwap(){
        List<Job> swapList = new ArrayList<>(jobList);

        return swapList;
    }


}
