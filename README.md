# ShareSpace

## Environment setup:

- JDK: use either temurin or OpenJDK. Needs to be version 25

    - Prefer temurin:
    [https://adoptium.net/temurin/]
    > See `Other Downloads` for more specific downloads.

- IDE: use whatever but remember to set the used JDK in the settings
> Always import project as a `Existing Maven project`

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

Generate and view the project JavaDoc HTML (used in reviews):

- CLI / Maven:
  - Linux/macOS: ./scripts/build-docs.sh
  - Windows PowerShell: .\scripts\build-docs.ps1
  - Output: target/reports/apidocs/index.html

- IntelliJ: Tools → Generate JavaDoc... → set Output directory to <project>/target/reports/apidocs
- Eclipse: Project → Generate Javadoc... → choose packages and Destination (use <project>/target/reports/apidocs). Point to the JDK javadoc executable if prompted and add the same extra options.
- Submission: the build scripts include the generated docs in the submission archive (target/reports/apidocs). You can also zip the apidocs folder for manual uploads.

These scripts ensure everyone produces identical output used in reviews.
