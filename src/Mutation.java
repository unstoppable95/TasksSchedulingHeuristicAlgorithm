import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Mutation {

    private List<Job> jobList;
    private int listSize;
    private int d;
    private int r;

    public Mutation(Schedule example){
        this.jobList=new ArrayList<>(example.jobList);
        this.listSize=jobList.size();
        this.d = example.d;
        this.r = example.r;
    }

    public List<Job> simpleSwap(){
        int idxJobForSwap;
        int idxJobForSwap2;

        do{
            idxJobForSwap=new Random().nextInt(listSize);
            idxJobForSwap2=new Random().nextInt(listSize);
        }
        while(idxJobForSwap==idxJobForSwap2);

        List<Job> swapList = new ArrayList<>(jobList);
        Collections.swap(swapList, idxJobForSwap, idxJobForSwap2);

        return swapList;
    }

    public List<Job> maxABSwap(){
        List<Job> swapList = new ArrayList<>(jobList);
        List<Integer> endTimeJob = findEndTimeJob(swapList);
        int dTaskIdx = findDTaskIdx(swapList, d);

        int idxMaxA = findMaxA(new ArrayList<>(swapList.subList(0, dTaskIdx)), endTimeJob);
        int idxMaxB = findMaxB(new ArrayList<>(swapList.subList(dTaskIdx, swapList.size())), endTimeJob);

        Collections.swap(swapList, idxMaxA, idxMaxB);
        return swapList;
    }

    public List<Job> minABSwap(){
        List<Job> swapList = new ArrayList<>(jobList);
        int dTaskIdx = findDTaskIdx(swapList, d);

        int idxMinA = findMinA(new ArrayList<>(swapList.subList(dTaskIdx, swapList.size())));
        int idxMinB = findMinB(new ArrayList<>(swapList.subList(0, dTaskIdx)));

        Collections.swap(swapList, idxMinA, idxMinB);
        return swapList;
    }

    private int findMinA(List<Job> jobList){
        int aIdx = 0;
        int aMin = Integer.MAX_VALUE;

        for(int i=0;i<jobList.size();i++){
            int a = jobList.get(i).getA();
            if(a <= aMin) {
                aMin=a;
                aIdx=i;
            }
        }

        return aIdx;
    }

    private int findMinB(List<Job> jobList){
        int bIdx = 0;
        int bMin = Integer.MAX_VALUE;

        for(int i=0;i<jobList.size();i++){
            int b = jobList.get(i).getB();
            if(b <= bMin) {
                bMin=b;
                bIdx = i;
            }
        }
        return bIdx;
    }

    private int findDTaskIdx(List<Job> jobList, int d){
        int taskNo = 0;
        int tmp =0;
        for(Job j: jobList){
            tmp+=j.getP();
            if(tmp>=d) return taskNo;
            else taskNo++;
        }
        return taskNo;
    }

    private int findMaxA(List<Job> jobList, List<Integer> endTimeJob){
        int maxA=0;
        int a;
        int biggestA = 0;

        for(int i=0; i<jobList.size(); i++){
            a = jobList.get(i).getA() * Math.max(d - endTimeJob.get(i), 0);
            if(a >= biggestA){
                biggestA = a;
                maxA = i;
            }
        }
        return maxA;
    }

    private int findMaxB(List<Job> jobList, List<Integer> endTimeJob){
        int maxB =0;
        int b;
        int biggestB=0;

        for (int i=0; i<jobList.size();i++)
        {
            b = jobList.get(i).getB() * Math.max(endTimeJob.get(i) - d, 0);
            if(b >= biggestB){
                biggestB = b;
                maxB = i;
            }
        }
        return maxB;
    }

    private List<Integer> findEndTimeJob(List<Job> jobList){
        List<Integer> endTimeJob = new ArrayList<>();
        int currentTime = r;

        for(int i=0;i<jobList.size();i++){
            int jobEndTime = currentTime + jobList.get(i).getP();
            endTimeJob.add(jobEndTime);
            currentTime = currentTime + jobList.get(i).getP();
        }
        return endTimeJob;
    }



}