package app;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        var cwd = Paths.get("").toAbsolutePath();

        ReadDir browse = new ReadDir();
        browse.readDir(cwd);
    }
}
