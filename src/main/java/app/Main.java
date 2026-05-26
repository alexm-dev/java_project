package app;

import app.cli.TerminalApp;
import app.database.Database;
import app.util.Logger;

public class Main {
    public static void main(String[] args) throws Exception {
        Database.initialize();
        Logger.info("ShareSpace started");
        new TerminalApp().run();
    }
}
