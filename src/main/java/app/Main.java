package app;

import app.database.Database;

public class Main {
    public static void main(String[] args) throws Exception {
        Database.initialize();
        System.out.println("ShareSpace started.");
    }
}
