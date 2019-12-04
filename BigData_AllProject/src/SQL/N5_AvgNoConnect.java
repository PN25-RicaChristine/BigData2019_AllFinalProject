/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author debuayanri_sd2082
 */
//5. Get average per column using java without connection 1000 
public class N5_AvgNoConnect {

    public static void main(String[] args) {
        System.out.println("5. Get average per column using java without connection 1000 \n");
        Thread thread;
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss:SSS");
        String strDate = dateFormat.format(date);
        System.out.println("Time Start : " + strDate);

        thread = new Thread() {
            @Override
            public void run() {
                try {
                    Statement stmt = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost/rica_schooldata", "root", "");
                    stmt = (Statement) con.createStatement();

                    for (int i = 1; i <= 5; i++) {

//                        ResultSet aveTemp = stmt.executeQuery("SELECT CAST(AVG(col" + i + ") AS DECIMAL(10,3)) FROM sqlno_1000");
                         ResultSet ave = stmt.executeQuery("SELECT AVG(col" + i + ") FROM sqlno_1000");
                        if (ave.next()) {
                            System.out.println("Average Column-" + i + "\t" + ave.getFloat(1));
                        }
                        
                        
                        if (i == 5) {
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
                    }
                    con.close();
                } catch (ClassNotFoundException | SQLException e) {
                    System.out.println("Error!");

                } catch (ParseException ex) {
                    Logger.getLogger(N5_AvgNoConnect.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

}
