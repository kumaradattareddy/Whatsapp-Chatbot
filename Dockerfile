# 1) Use a lightweight OpenJDK 17 image
FROM eclipse-temurin:24-jdk
# 2) Create and switch to /app
WORKDIR /app

# 3) Copy only the Maven wrapper bits and POM first (for layer caching)
COPY demo/mvnw ./
COPY demo/.mvn/ .mvn/
COPY demo/pom.xml ./

# 4) Copy all source code
COPY demo/src ./src

# 5) Make the wrapper executable
RUN chmod +x mvnw

# 6) Build the JAR (skip tests for speed)
RUN ./mvnw clean package -DskipTests

# 7) Expose Spring Boot’s default port
EXPOSE 8080

# 8) Run the exactly-named JAR from target/
CMD ["java","-jar","target/demo-0.0.1-SNAPSHOT.jar"]
