# Use official OpenJDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/food-delivery-0.0.1-SNAPSHOT.jar"]
```

---

### **2. Create .dockerignore**

**Location:** `backend-root/.dockerignore`
```
target/
.mvn/wrapper/maven-wrapper.jar
.git
.gitignore
*.md
.env