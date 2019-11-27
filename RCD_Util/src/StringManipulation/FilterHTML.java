/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StringManipulation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author debuayanri_sd2082
 */
public class FilterHTML {

    public String schoolName;

    public FilterHTML() {
    }

    public String removeTags(String str) {
        str = str.replaceAll("\\<.*?\\>", "");
        return str;
    }

    public String countWords(String str) {
        //GETTING THE TAGS OF THE WEBSITE
        ArrayList<String> tags = new ArrayList<>();
        //GETTING THE WORDS OF THE WEBSITE
        ArrayList<String> words = new ArrayList<>();
        //COUNTING THE WORD OF THE WEBSITE
        ArrayList<String> wordcount = new ArrayList<>();

        String strs = "";

        //GETTING THE TAGS OF THE WEBSITE
        Pattern p = Pattern.compile("<([^\\s>/]+)");
        Matcher m = p.matcher(str);
        while (m.find()) {
            String tag = m.group(1);
            tags.add(tag);
        }
//-------------------------------------------------------------------------------------------------//
        //GETTING THE WORDS OF THE WEBSITE
        Pattern w = Pattern.compile("[a-zA-Z]+");
        Matcher m1 = w.matcher(str);

        while (m1.find()) {
            words.add(m1.group());
        }

        //-------------------COUNT THE WORDS OF THE WEBSITE----------------------------//
        //COUNTING THE WORD OF THE 
        for (String word : words) {
            if (tags.contains(word)) {
                continue;
            } else {
                wordcount.add(word);
            }
        }
        //----------THE OUTPUT OF HOW MANY TIMES THE WORD IN THE WEBSITE APPEARS---------//
        Collections.sort(wordcount);
        Set<String> distinct = new HashSet<>(wordcount);
        for (String s : distinct) {

            strs += s + ": " + Collections.frequency(wordcount, s) + "\n";

            try {
              // create a mysql database connection

                //com.mysql.cj.jdbc.Driver
                String myDriver = "com.mysql.jdbc.Driver";

                String myUrl = "jdbc:mysql://localhost/rica_schooldata";
//                String myUrl = "jdbc:mysql://172.16.3.17/zrica";
                Class.forName(myDriver);
                Connection conn = DriverManager.getConnection(myUrl, "root", "");
//                Connection conn = DriverManager.getConnection(myUrl, "christian", "guapo");

                // create a sql date object so we can use it in our INSERT statement
                Calendar calendar = Calendar.getInstance();
                java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

                // the mysql insert statement
                String query = " insert into countword(word,count,school)"
                        + " values (?,?,?)";

                // create the mysql insert preparedstatement
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setString(1, s);
                preparedStmt.setInt(2, Collections.frequency(wordcount, s));
                preparedStmt.setString(3, schoolName);

                // execute the preparedstatement
                preparedStmt.execute();

                conn.close();
            } catch (Exception e) {
                System.err.println("Got an exception!");
                System.err.println(e.getMessage());
            }
        }
        return strs;
    }

    public String countTags(String html) {
        //GETTING THE TAGS OF THE WEBSITE
        ArrayList<String> tags = new ArrayList<>();
        //GETTING THE WORDS OF THE WEBSITE
        ArrayList<String> words = new ArrayList<>();

        String toSearch = html;
        String str = "Tags:\n";

        Pattern p = Pattern.compile("<([^\\s>/]+)");
        Matcher m = p.matcher(toSearch);
        while (m.find()) {
            String tag = m.group(1);
            if (tag.contains("!-") == false && tag.length() < 8) {
                tags.add(tag);
            }
        }

        Collections.sort(tags);
        Set<String> distinct = new HashSet<>(tags);
        for (String s : distinct) {
            str = str + s + ": " + Collections.frequency(tags, s) + "\n";
            try {
              // create a mysql database connection

                //com.mysql.cj.jdbc.Driver
                String myDriver = "com.mysql.jdbc.Driver";

                String myUrl = "jdbc:mysql://localhost/rica_schooldata";
//                String myUrl = "jdbc:mysql://172.16.3.17/zrica";
                Class.forName(myDriver);
                Connection conn = DriverManager.getConnection(myUrl, "root", "");
//                Connection conn = DriverManager.getConnection(myUrl, "christian", "guapo");

                // create a sql date object so we can use it in our INSERT statement
                Calendar calendar = Calendar.getInstance();
                java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

                // the mysql insert statement
                String query = " insert into tagcount(tag,count,school)"
                        + " values (?,?,?)";

                // create the mysql insert preparedstatement
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setString(1, s);
                preparedStmt.setInt(2, Collections.frequency(tags, s));
                preparedStmt.setString(3, schoolName);

                // execute the preparedstatement
                preparedStmt.execute();

                conn.close();
            } catch (Exception e) {
                System.err.println("Got an exception!");
                System.err.println(e.getMessage());
            }
        }

        return str;
    }

    public static void main(String[] args) {
        FilterHTML d = new FilterHTML();

    }
}
