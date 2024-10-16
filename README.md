# mychess

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
