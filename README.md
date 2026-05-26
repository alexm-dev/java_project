# ShareSpace

## Environment setup:

- JDK: use either temurin or OpenJDK. Needs to be version 25

    - Prefer temurin:
    [https://adoptium.net/temurin/]
    > See `Other Downloads` for more specific downloads.

- IDE: use whatever but remember to set the used JDK in the settings
> Always import project as a `Existing Maven project`

> **IDE run configuration note:** if you run the app directly from your IDE (green play button)
> and see a warning about `java.lang.System::load`, add this to your run configuration's VM arguments:
> `--enable-native-access=ALL-UNNAMED`
> (IntelliJ: Run > Edit Configurations > VM options. Eclipse: Run Configurations > Arguments > VM arguments)
> This is not needed when running via Maven (`mvn exec:java`) as it is already set in `.mvn/jvm.config`.

If you prefer the terminal:

- Install `Maven` as well
- Usage:

```sh
# Compile with maven
mvn compile

# Run with maven
mvn exec:java
```

Our Stack so far:  

Backend:  
Sqlite  
JDBC  

Frontend:  
JavaFX  

## JavaDoc

Generate and view the project JavaDoc HTML:

- CLI / Maven:
  - Linux/macOS: ./scripts/build-docs.sh
  - Windows PowerShell: .\scripts\build-docs.ps1
  - Output: target/reports/apidocs/index.html

- IntelliJ: Tools → Generate JavaDoc... → set Output directory to <project>/target/reports/apidocs
- Eclipse: Project → Generate Javadoc... → choose packages and Destination (use <project>/target/reports/apidocs). Point to the JDK javadoc executable if prompted and add the same extra options.
- Submission: the build scripts include the generated docs in the submission archive (target/reports/apidocs). You can also zip the apidocs folder for manual uploads.

These scripts ensure everyone produces identical output used in reviews.
