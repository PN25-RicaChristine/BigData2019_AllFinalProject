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

public class N1_MDB200 {

    public static void main(String[] args) {
        System.out.println("no 1. MDB with connection 200 times\n\n");
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
                        Col col = new Col(i, i + 1, i + 2, i + 3, i + 4);
                        DBObject doc = createDBObject(col);
                        MongoClient mongo = new MongoClient("localhost", 27017);
                        DB db = mongo.getDB("ricabigdata");
                        DBCollection column = db.getCollection("columns200");

                        //create col
                        WriteResult result = column.insert(doc);
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
                        Logger.getLogger(N1_MDB200.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(N1_MDB200.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        };
        thread.start();
    }

    private static DBObject createDBObject(Col col) {
        BasicDBObjectBuilder docBuilder = BasicDBObjectBuilder.start();

        docBuilder.append("col1", col.getCol1());
        docBuilder.append("col2", col.getCol2());
        docBuilder.append("col3", col.getCol3());
        docBuilder.append("col4", col.getCol4());
        docBuilder.append("col5", col.getCol5());
        return docBuilder.get();
    }

    private static Col createCol(int col1, int col2, int col3, int col4, int col5) {
        Col col = new Col(col1, col2, col3, col4, col5);
        return col;
    }

}
