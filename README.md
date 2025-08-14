# 13DGT - DataLens

## Overview
This project contains research, survey data, inquiry, and a proposal for my 13DGT outcome known as DataLens. It is developed using Kotlin.

DataLens is a Kotlin desktop application for Windows that helps users search, index, and manage files efficiently. 
It features a modern UI built with Compose for Desktop, customizable search relevance using string similarity algorithms, and a local database for fast file indexing. 
The app uses dependency injection and the strategy pattern for flexible file location logic, and provides settings to adjust matching sensitivity. 
DataLens is designed for privacy, speed, and ease of use.
DataLens requires no internet connection, ensuring user privacy and data security.
The program is built with a focus on performance and user experience, making it suitable for both casual users and power users who need advanced file management capabilities.

## Testing
A suite of unit tests is included to ensure code quality and reliability.
These tests cover various components of the application, including file indexing, search algorithms, and database interactions.
These tests are run automatically during the build process to catch any regressions or issues early.
The project is automatically tested using GitHub Actions, ensuring that all changes are validated.

## Project Structure
The repository includes:
- Source code in Kotlin
- Documentation and survey data
- Proposal for the 13DGT outcome
- Research findings and analysis
- Inquiry results
- Planning for outcome implementation

## Getting Started
To build and run the project:
1. Ensure you have Java >= 11 installed.
2. Clone the repository.
3. Run `gradle build` to compile the project.

## Precompile Executable
You can grab a precompiled executable from the [releases](https://github.com/Apollointhehouse/13DGT/releases)