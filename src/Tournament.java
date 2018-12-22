import java.util.List;
import java.util.Random;

public class Tournament {

     private List<Schedule> schedules ;
     private Random random;

     public Tournament(List<Schedule> schedules) {
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

             if(schedules.get(idxSchedule1).goalFunction >= schedules.get(idxSchedule2).goalFunction){
                 schedules.remove(idxSchedule1);
             }
             else {
                 schedules.remove(idxSchedule2);
             }
         }

     }

}
