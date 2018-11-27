import javafx.util.Pair;
import java.util.List;
import java.util.Random;

public class Tournament {

     private List<Pair> schedules ;
     private Random random;

     public Tournament(List<Pair> schedules) {
         this.schedules=schedules;
         this.random=new Random();
     }

     public void makeCompetition(int numberOfWinners){
         int idxSchedule1;
         int idxSchedule2;
         int sizeBeforeTournament=schedules.size();

         for (int i=0;i<sizeBeforeTournament-numberOfWinners;i++){
             idxSchedule1=random.nextInt(schedules.size());
             idxSchedule2=random.nextInt(schedules.size());
             while(idxSchedule1==idxSchedule2) idxSchedule2=random.nextInt(schedules.size());

             if(Double.parseDouble(schedules.get(idxSchedule1).getValue().toString()) >= Double.parseDouble(schedules.get(idxSchedule2).getValue().toString())){
                 schedules.remove(idxSchedule1);
             }
             else {
                 schedules.remove(idxSchedule2);
             }

         }

     }


}
