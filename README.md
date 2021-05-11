# Crypto Market Simulator
---
The goal of this project is to simulate crypto market transactions.
You can:
- buy
- sell
- hold currencies in your "wallet". 
  
The goal is (as a user) to make as much profit as possible with **$1000** of starting balance. Note that there are **no transaction fees**.

The prices of the currencies are taken from an API (https://), **the prices might not be accurate** as the market is refreshed every 5 minutes after logging in. Logging out and then in will force the market to refresh all of the currencies.

The project was made with JavaFX (using Java 11).
#### Usage
- Site
  ```mvn site```
- Package
  ```mvn package```
- Compile & exec
  ```mvn compile exec:java```
- Package & run
  ```
  mvn package
  java -jar ./target/crypto-trading-game-1.0-SNAPSHOT.jar
  ```
#### Important
- Database connection details must be configured in advance in the `resources/META-INF/persistence.xml` file
- Not secure (connection details are packaged with the app)