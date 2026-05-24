package app;

import app.database.Database;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello world!");
        Database.initialize();
        TestDB.run();
    }
}
