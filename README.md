# MyChess - Chess Tournament Management System
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)
![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white)
## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Microservices](#microservices)
- [Tech Stack](#tech-stack)
- [Deployment](#deployment)
- [License](#license)
## Overview
**MyChess** is a web application for organizing and participating in chess tournaments. It allows players to join tournaments, track progress, and calculate ratings using the **Glicko-2** system. Built using a microservices architecture, the app offers independent services for handling key functionalities such as player management, tournaments, and authentication.
## Features
- **Player Authentication**: Secure login and registration using **JWT tokens**.
- **Tournament Management**: Create and join customizable chess tournaments.
- **Matchmaking**: Automatic pairing of players in tournaments.
- **Glicko-2 Rating System**: Real-time rating updates after matches.
- **Admin Panel**: Admin features for managing users, including blacklisting and whitelisting.
- **Email Notifications**: Automated notifications for account verification and status updates.
## Microservices
- **Auth Service**: Manages user authentication and JWT issuance.
- **Match Service**: Handles match results and rating calculations.
- **Tournament Service**: Manages tournament creation and participation.
- **Player Service**: Manages player profiles and ratings.
- **Admin Service**: Admin functionality for blacklisting/whitelisting players.
- **Email Service**: Sends account verification and notification emails.
## Tech Stack
- **Backend**:
    - [Java](https://www.java.com)
    - [Spring Boot](https://spring.io/projects/spring-boot)
- **Frontend**:
    - [React](https://reactjs.org)
- **Database**:
    - [MySQL](https://www.mysql.com)
- **Containerization**:
    - [Docker](https://www.docker.com)
- **CI/CD**:
    - [GitHub Actions](https://github.com/features/actions)
## Deployment
The app runs on AWS EC2, with each microservice containerized using Docker. CI/CD is handled via GitHub Actions, automating the build, testing, and deployment process.
- **AWS EC2**: Services are deployed on EC2 instances behind an Elastic Load Balancer.
- **Docker**: All microservices are containerized.
- **GitHub Actions**: Automates testing, building, and pushing Docker images to production.
```mermaid
graph TD
    A([Source Code Repository]) --> B([Checkout Code])
    B --> C[Set up Node.js]
    C --> D[Install Frontend Dependencies]
    D --> E[Build React Application]
    E --> F{Skip Backend?}
    F -->|Yes| M[Build Frontend Docker Image]
    M --> N[Push Frontend Docker Image]
    F -->|No| G[Set up JDK 17]
    G --> H[Build and Test Spring Boot Services]
    H --> I[Install Docker Compose]
    I --> J[Build Backend Docker Images]
    J --> K[Push Backend Docker Images]
    K --> L[Build Frontend Docker Image]
    L --> N[Push Frontend Docker Image]
    
    N --> O[Deploy to EC2 Instances]
    O --> P[Deploy Backend Services]
    O --> Q[Deploy Frontend Service]
    
    subgraph Backend Deployment
        P1[Deploy Auth Service]
        P2[Deploy Admin Service]
        P3[Deploy Player Service]
        P4[Deploy Email Service]
        P5[Deploy Tournament Service]
        P6[Deploy Match Service]
        P --> P1
        P --> P2
        P --> P3
        P --> P4
        P --> P5
        P --> P6
    end
    Q --> F1[Deploy Frontend Service to EC2]
```
## License
This project is licensed under the **GNU General Public License v3.0**. You are free to modify, distribute, and use this project as long as the same license applies to any derivative work.
For more details, see the [LICENSE](LICENSE) file.
[GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.en.html)
