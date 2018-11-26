import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PheromoneMatrix {

    int size;
    double [][] matrix;

    public PheromoneMatrix(int n){
        this.size=n;
        this.matrix=new double [size][size];
    }

    public void fillMatrix(List<Job> schedule,double value){
        for(int i=0;i<size-1;i++){
            matrix[schedule.get(i).getPosition()][schedule.get(i+1).getPosition()]+=value;

        }
    }


    public void displayContent() {
        for (int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

    }

    public List<Integer> order (int startJobNumber){
        List<Integer> order = new ArrayList<>();
        List<Integer> used = new ArrayList<>();
        used.add(startJobNumber);
        order.add(startJobNumber);

        boolean[] usedTask = new boolean[this.matrix.length];
        usedTask[startJobNumber] = true;

        double suma;

        double[][] matrixTmp = new double[this.matrix.length][this.matrix.length];
        int lastTask = startJobNumber;
        int numberOfChoosen = 1;

        for(int i=0; i<this.matrix.length; i++){
            suma = 0.0;
            for(int j=0;j<this.matrix.length; j++){
                suma+=this.matrix[i][j]+1;
                matrixTmp[i][j]=suma;
            }
        }

        int iter = 0;
        double x;
        double randomed;

        while(numberOfChoosen <= this.matrix.length){
            x = matrixTmp[lastTask][this.matrix.length - 1];
            iter++;
            if(iter > 50){
                iter = 0;
                numberOfChoosen++;
                for(int i=0;i<this.matrix.length;i++){
                    if(!usedTask[i]){
                        used.add(i);
                        usedTask[i]=true;
                        lastTask = i;
                        order.add(i);
                        break;
                    }
                }
            }
            else{
                Random generator = new Random();
                randomed = generator.nextInt()*x;
                for(int i=0;i<this.matrix.length;i++){
                    if(!usedTask[i] && matrixTmp[lastTask][i]>(double)randomed){
                        used.add(i);
                        numberOfChoosen++;
                        usedTask[i]=true;
                        lastTask=i;
                        order.add(i);
                        break;
                    }
                }

            }
        }

        return order;

    }
}
