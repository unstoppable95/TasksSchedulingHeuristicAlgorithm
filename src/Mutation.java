import java.util.*;

public class Mutation {

    private Job [] jobList;
    private int listSize;
    private int d;
    private int r;

    public Mutation(Schedule example){
        this.jobList=example.jobList.clone();
        this.listSize=jobList.length;
        this.d = example.d;
        this.r = example.r;
    }

    public static final <T> void swap (T[] a, int i, int j) {
        T t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public Job[] simpleSwap(){
        int idxJobForSwap;
        int idxJobForSwap2;

        do{
            idxJobForSwap=new Random().nextInt(listSize);
            idxJobForSwap2=new Random().nextInt(listSize);
        }
        while(idxJobForSwap==idxJobForSwap2);

        Job [] swapList = jobList;
        swap(swapList, idxJobForSwap, idxJobForSwap2);

        return swapList;
    }

    public Job [] maxABSwap(){
        Job[] swapList = jobList;
        List<Integer> endTimeJob = findEndTimeJob(swapList);
        int dTaskIdx = findDTaskIdx(swapList, d);

        Job [] firstHalf=Arrays.copyOfRange(swapList,0,dTaskIdx);
        Job [] secondHalf=Arrays.copyOfRange(swapList,dTaskIdx,swapList.length);
        int idxMaxA = findMaxA(firstHalf, endTimeJob);
        int idxMaxB = findMaxB(secondHalf, endTimeJob);

        swap(swapList, idxMaxA, idxMaxB);
        return swapList;
    }

    public Job[] minABSwap(){
        Job [] swapList =jobList;
        int dTaskIdx = findDTaskIdx(swapList, d);

        Job [] firstHalf=Arrays.copyOfRange(swapList,0,dTaskIdx);
        Job [] secondHalf=Arrays.copyOfRange(swapList,dTaskIdx,swapList.length);
        int idxMinA = findMinA(firstHalf);
        int idxMinB = findMinB(secondHalf);

        swap(swapList, idxMinA, idxMinB);
        return swapList;
    }

    private int findMinA(Job [] jobList){
        int aIdx = 0;
        int aMin = Integer.MAX_VALUE;

        for(int i=0;i<jobList.length;i++){
            int a = jobList[i].getA();
            if(a <= aMin) {
                aMin=a;
                aIdx=i;
            }
        }

        return aIdx;
    }

    private int findMinB(Job [] jobList){
        int bIdx = 0;
        int bMin = Integer.MAX_VALUE;

        for(int i=0;i<jobList.length;i++){
            int b = jobList[i].getB();
            if(b <= bMin) {
                bMin=b;
                bIdx = i;
            }
        }
        return bIdx;
    }

    private int findDTaskIdx(Job [] jobList, int d){
        int taskNo = 0;
        int tmp =0;
        for(Job j: jobList){
            tmp+=j.getP();
            if(tmp>=d) return taskNo;
            else taskNo++;
        }
        return taskNo;
    }

    private int findMaxA(Job [] jobList, List<Integer> endTimeJob){
        int maxA=0;
        int a;
        int biggestA = 0;

        for(int i=0; i<jobList.length; i++){
            a = jobList[i].getA() * Math.max(d - endTimeJob.get(i), 0);
            if(a >= biggestA){
                biggestA = a;
                maxA = i;
            }
        }
        return maxA;
    }

    private int findMaxB(Job [] jobList, List<Integer> endTimeJob){
        int maxB =0;
        int b;
        int biggestB=0;

        for (int i=0; i<jobList.length;i++)
        {
            b = jobList[i].getB() * Math.max(endTimeJob.get(i) - d, 0);
            if(b >= biggestB){
                biggestB = b;
                maxB = i;
            }
        }
        return maxB;
    }

    private List<Integer> findEndTimeJob(Job [] jobList){
        List<Integer> endTimeJob = new ArrayList<>();
        int currentTime = r;

        for(int i=0;i<jobList.length;i++){
            int jobEndTime = currentTime + jobList[i].getP();
            endTimeJob.add(jobEndTime);
            currentTime = currentTime + jobList[i].getP();
        }
        return endTimeJob;
    }



}