/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MongoDB;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Retrieve {

    public void retrieveAvg() throws UnknownHostException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Instant fore = Instant.now();
        System.out.println("Time started: " + formatter.format(date));
        try {
            Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
            mongoLogger.setLevel(Level.SEVERE);
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB db = mongoClient.getDB("ricabigdata");
         
            for (int i = 1; i < 6; i++) {
                DBObject groupFields = new BasicDBObject("_id", null);
                groupFields.put("Average", new BasicDBObject("$avg", "$col" + i));
                DBObject group = new BasicDBObject("$group", groupFields);
                AggregationOutput output = db.getCollection("columns1000").aggregate(group);
                Iterable<DBObject> list = output.results();
                System.out.println(list);
            }
            Instant ter = Instant.now();
            Date date1 = new Date();
            Duration duration = Duration.between(fore, ter);
            System.out.println("Time ended: " + formatter.format(date1) + "\n" + "Duration: " + duration.getSeconds() + " second/s");

        } catch (SecurityException e) {
            System.out.println(e);
        }

    }


    public void retrieveAvgWC() throws UnknownHostException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Instant fore = Instant.now();
        System.out.println("Time started: " + formatter.format(date));
        try {
            int start = 1;
            Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
            mongoLogger.setLevel(Level.SEVERE);
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB db = mongoClient.getDB("ricabigdata");
            System.out.println("Connected to database!");
            DBObject groupFields = new BasicDBObject("_id", null);
            groupFields.put("Average", new BasicDBObject("$avg", "$col" + start));
            DBObject group = new BasicDBObject("$group", groupFields);
            AggregationOutput output = db.getCollection("columnsNO1000").aggregate(group);
            Iterable<DBObject> list = output.results();
            for (int i = 1; i < 6; i++) {
                System.out.println(list);
            }
            Instant ter = Instant.now();
            Date date1 = new Date();
            Duration duration = Duration.between(fore, ter);
            System.out.println("Time ended: " + formatter.format(date1) + "\n" + "Duration: " + duration.getSeconds() + " second/s");

        } catch (SecurityException e) {
            System.out.println(e);
        }
    }
    public static void main(String[] args) throws UnknownHostException {
        Retrieve ret = new Retrieve();
        ret.retrieveAvgWC();
    }
}