/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MongoDB;

/**
 *
 * @author 2ndyrGroupB
 */
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class N3_DeleteMDB200 {

    public static void main(String[] args) {
        System.out.println("no 3. MDB with connection delete 200 times\n\n");
        Thread thread;
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss:SSS");
        String strDate = dateFormat.format(date);
        System.out.println("Time Start : " + strDate);

        thread = new Thread() {
            @Override
            public void run() {
                for (int i = 1; i <= 200; i++) {
                    try {
  
                        MongoClient mongo = new MongoClient("localhost", 27017);
                        DB db = mongo.getDB("ricabigdata");
                        DBCollection column = db.getCollection("columns200");

                        //read column
                        DBObject query = BasicDBObjectBuilder.start().add("col1", i).get();
                        //remove col1
                        WriteResult result = column.remove(query);
                        
                        if (i == 200) {
                            Date d1;
                            Date d2;
                            Date date2 = Calendar.getInstance().getTime();
                            DateFormat format = new SimpleDateFormat("hh:mm:ss:SSS");
                            String strDate2 = format.format(date2);
                            System.out.println("Time Stop : " + strDate2);

                            d1 = format.parse(strDate);
                            d2 = format.parse(strDate2);

                            //in milliseconds
                            long diff = d2.getTime() - d1.getTime();

                            long diffM = diff % 1000;
                            long diffSeconds = diff / 1000 % 60;
                            long diffMinutes = diff / (60 * 1000) % 60;
                            long diffHours = diff / (60 * 60 * 1000) % 24;

                            System.out.print("Total Time Running\n" + diffHours + " hrs, ");
                            System.out.print(diffMinutes + " mins, ");
                            System.out.print(diffSeconds + " secs, ");
                            System.out.print(diffM + " millisecs\n");
                        }

                        mongo.close();
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(N3_DeleteMDB200.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(N3_DeleteMDB200.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        };
        thread.start();
    }

}
