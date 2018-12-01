import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PheromoneMatrix {

    int size;
    double [][] matrix;

    public PheromoneMatrix(int n){
        this.size=n;
        this.matrix=new double [size][size];
    }

    public void fillMatrix(List<Job> schedule, double value){
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

        boolean[] usedTask = new boolean[size];
        usedTask[startJobNumber] = true;

        double suma;

        double[][] matrixTmp = new double[size][size];
        int lastTask = startJobNumber;
        int numberOfChoosen = 1;

        for(int i=0; i<size; i++){
            suma = 0.0;
            for(int j=0;j<size; j++){
                suma+=matrix[i][j]+1;
                matrixTmp[i][j]=suma;
            }
        }

        int iter = 0;
        double x;
        double randomed;

        while(numberOfChoosen <= size){
            x = matrixTmp[lastTask][size - 1];
            iter++;
            if(iter > 50){
                iter = 0;
                numberOfChoosen++;
                for(int i=0;i<size;i++){
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
                for(int i=0;i<size;i++){
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

    public void evaporateMatrix ( double evaporationCoefficient){
        for (int i =0;i<size;i++)
            for (int j=0;j<size ; j++)
                matrix[i][j]=matrix[i][j]*(1-evaporationCoefficient);
    }

    public void smoothMatrix( double smoothCoefficient){
        for (int i=0; i<size; i++) {
            double min=matrix[0][i];
            for (int j = 0; j < size; j++) {
                if(matrix[i][j] <min ) min=matrix[i][j];
            }
         if(min<=0) min=0.00001;
            for(int j=0; j<size; j++) {
                if(matrix[i][j]>0) {
                    matrix[i][j]=min*(1*(Math.log10(matrix[i][j]/min)/Math.log10(smoothCoefficient)));
                } else {
                    matrix[i][j]=min*(1*(Math.log10(0.00001/min)/Math.log10(smoothCoefficient)));
                }
            }

        }

    }

}
