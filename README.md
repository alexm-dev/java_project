# ShareSpace

## Environment setup:

- JDK: use either temurin or OpenJDK. Needs to be version 25

    - Prefer temurin:
    [https://adoptium.net/temurin/]
    > See `Other Downloads` for more specific downloads.

- IDE: use whatever but remember to set the used JDK in the settings
> Always import project as a `Existing Maven project`

> **Recommended: run via Maven in your IDE**  
> This picks up `.mvn/jvm.config` automatically and requires no per-run setup.
>
> **IntelliJ IDEA:** Run → Edit Configurations → + → Maven → set Working directory to the project root → set Command line to `exec:java` → OK. Use this config to run the app.  
> **Eclipse:** Right-click project → Run As → Maven build... → set Goals to `exec:java` → Apply → Run. Save it as a named launch config and use it going forward.

> **If you use the green play button instead:**  
> You may see a warning about `java.lang.System::load`. Add this VM argument once to silence it:  
> `--enable-native-access=ALL-UNNAMED`
>
> **IntelliJ:** First run `Main.java` once via right-click → **Run 'Main.main()'** to generate the config, then Run → Edit Configurations → select the `Main` entry → expand **Modify options** → tick **Add VM options** → paste the argument → Apply  
> **Eclipse:** Run → Run Configurations → select your configuration → **Arguments** tab → paste into **VM arguments** → Apply  

If you prefer the terminal:

- Install `Maven` as well
- Usage:

```sh
# Compile with maven
mvn compile

# Run with maven
mvn exec:java
```

## Tech Stack

| Layer    | Technology                                     |
|----------|------------------------------------------------|
| Language | Java 25                                        |
| Build    | Maven (shade, surefire, javadoc, exec plugins) |
| Database | SQLite via JDBC                                |
| Security | BCrypt (jbcrypt) password hashing              |
| UI       | JavaFX (planned)                               |
| Testing  | JUnit 5                                        |
| CI/CD    | GitHub Actions                                 |

**Architecture:** Models → DAOs → Services (planned) → JavaFX UI (planned)  
No frameworks; pure Java OOP.

## JavaDoc

Generate and view the project code documentaion JavaDoc HTML:

- CLI / Maven:
  - Linux/macOS: ./scripts/build-docs.sh
  - Windows PowerShell: .\scripts\build-docs.ps1
  - Output: target/reports/apidocs/index.html

- IntelliJ: View → Tool Windows → Maven → Plugins → javadoc → double-click `javadoc:javadoc`
- Eclipse: right-click project → Run As → Maven build... → set Goals to `javadoc:javadoc` → Run
- **Do not use** the IDE built-in Generate Javadoc wizard — it ignores pom.xml and will fail on Java 25
- Submission: the build scripts include the generated docs in the submission archive (target/reports/apidocs). You can also zip the apidocs folder for manual uploads.

These scripts ensure everyone produces identical output used in reviews.

You can then open the index.html inside output directory. Default: `target/reports/apidocs/index.html`
