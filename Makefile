# Makefile for Kotlin Spring application with MongoDB

# Define the name of your Kotlin application JAR file
JAR_FILE := build/libs/challenge-0.0.1-SNAPSHOT.jar

# Gradle Wrapper script path within the demo directory
GRADLEW := ./gradlew

# Build the Java application using Gradle
build:
	$(GRADLEW) build

test:
	$(GRADLEW) test && \
	$(GRADLEW) jacocoTestReport

# Run the java application
run:
	java -jar $(JAR_FILE)

# Clean and restart
re: clean-all all

# Clean the build directory
clean:
	$(GRADLEW) clean

.PHONY: all build start-mongodb stop-mongodb run clean re test


