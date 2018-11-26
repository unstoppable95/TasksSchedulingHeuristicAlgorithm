import java.util.Arrays;
import java.util.List;

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
}
