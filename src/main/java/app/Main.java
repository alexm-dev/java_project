package app;

import app.cli.TerminalApp;
import app.database.Database;
import app.service.SessionService;
import app.util.Logger;

public class Main {
    public static void main(String[] args) throws Exception {
        Database.initialize();
        Logger.info("ShareSpace started");

        SessionService session = new SessionService();
        if (session.restoreSession() != null) {
            Logger.info("restored session for: " + session.getActiveUser().getUsername());
        }

        new TerminalApp(session).run();
    }
}
