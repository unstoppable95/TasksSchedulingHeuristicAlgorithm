public class Job {

    private int p; //time
    private int a; //punishment for earlier execution
    private int b; //punishment for later execution
    private int differencePunishment;
    private int position;
    public Job(int p,int a,int b,int pos){
        this.p=p;
        this.a=a;
        this.b=b;
        this.differencePunishment=b-a;
        this.position=pos;

    }
    public Job(int p,int a,int b){
        this.p=p;
        this.a=a;
        this.b=b;
        this.differencePunishment=b-a;

    }

    public int getPosition() {
        return position;
    }

    public int getP() {
        return p;
    }

    public int getB() {
        return b;
    }

    public int getA() {
        return a;
    }

    public int getDifferencePunishment() {
        return differencePunishment;
    }

    public String toString(){
        return getP() +"\t" +getA() +"\t"+ getB();
    }


}
