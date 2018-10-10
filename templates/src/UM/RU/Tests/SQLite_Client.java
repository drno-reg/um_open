package UM.RU.Tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class SQLite_Client {


    public static  void main(String[] args) throws Exception {
        System.out.println("Start JavaAPP SQLite");

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite://home//drno//databases//sqlite//example.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT id, firstname, lastname, country FROM student");

            while ( rs.next() ) {
                int id = rs.getInt("id");
                String  firstname = rs.getString("firstname");

                System.out.println( "id = " + id );
                System.out.println( "firstname = " + firstname );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }


    }

