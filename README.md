## Java project

```sh
git clone https://github.com/alexm-dev/ShareSpace
```

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

