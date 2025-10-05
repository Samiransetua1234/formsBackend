# Forms Backend – Setup & Deployment Guide

This repository contains a basic Spring Boot application with PostgreSQL as the database.  
The guide below explains step-by-step how to set up everything from scratch on a fresh **AWS EC2 (Amazon Linux 2023)** instance, run Docker + PostgreSQL, and deploy the Spring Boot app.

---

## 📌 Prerequisites
- AWS account with EC2 access
- Downloaded `.pem` SSH key for your EC2
- GitHub repo with your Spring Boot code (this repo)
- Basic knowledge of Linux commands

---

## 🚀 Step-by-Step Setup

### 1. Launch EC2 Instance
- AMI: **Amazon Linux 2023**
- Type: **t2.micro** (free tier)
- Security Group inbound rules:
    - **SSH (22)** → My IP
    - **Custom TCP (8080)** → 0.0.0.0/0 (for testing APIs)

---

### 2. Connect to EC2
```bash
chmod 400 your-key.pem
ssh -i "your-key.pem" ec2-user@<EC2_PUBLIC_IP>
```
---
### 3. Install Java 21 on EC2

```bash
sudo dnf update -y  # update the ec2 machine
sudo dnf install java-21-amazon-corretto -y # aws java sdk
java -version # verify the installation
```
---
### 4. Install Docker
```bash
sudo dnf install docker -y
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker ec2-user
newgrp docker
docker version
docker run hello-world
```
---
### 5. Run PostgreSQL in Docker

```bash
docker run --name forms-postgres \
  -e POSTGRES_USER=user \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=formsdb \
  -p 5432:5432 \
  -d postgres:latest
```

#### Check Status:
```bash
docker ps
docker logs forms-postgres -f
```
#### Connect to DB:
```bash
docker exec -it forms-postgres psql -U admin -d formsdb
# exit with \q
```
---
### 6. Clone & Build Project
```bash
sudo dnf install git -y
git clone https://github.com/<your-username>/formsBackend.git
cd formsBackend
chmod +x mvnw
./mvnw clean package -DskipTests
```
---
### 7. Configure `application.properties`
#### Located at `src/main/resources/application.properties:`
```properties
spring.application.name=forms

#if the bd is running on another container the write that container public ip instead of localhost
spring.datasource.url=jdbc:postgresql://localhost:5432/formsdb
spring.datasource.username=admin
spring.datasource.password=admin

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```
---
### 8. Run Spring Boot App
```bash
java -jar target/*.jar
```
#### Test APIs:
```bash
curl http://localhost:8080/api/v1/users
curl http://<EC2_PUBLIC_IP>:8080/api/v1/users
```
---
### 🔧 Useful Commands

- Containers
```bash
docker ps -a              # list all containers
docker start forms-postgres
docker stop forms-postgres
docker logs -f forms-postgres
```
- Maven build
```bash
./mvnw clean package -DskipTests
```
- Run JAR
```bash
java -jar target/forms-0.0.1-SNAPSHOT.jar
```
---
### ⚡ Troubleshooting
- Permission denied for `./mvnw`
```bash
chmod +x mvnw
```
- DB connection error
  - Ensure Postgres container is running (`docker ps`)
  - Verify correct DB URL:`jdbc:postgresql://localhost:5432/formsdb`
  - Test connection inside container:
  ```bash
  docker exec -it forms-postgres psql -U admin -d formsdb
  ```
- `nc` or `telnet` missing
```bash
sudo dnf install nmap-ncat -y
sudo dnf install telnet -y
```
---
### 🛑 Optional: Auto-start on Reboot

Skip if not needed.
Use systemd service files to auto-start Postgres container and Spring Boot app on EC2 reboot.

### ✅ Quick Checklist

 1. Launch EC2 → open ports 22, 8080
 2. SSH → install Java 21
 3. Install Docker → run Postgres container
 4. Clone repo → build with Maven
 5. Configure DB connection → run JAR
 6. Test endpoints via EC2 public IP