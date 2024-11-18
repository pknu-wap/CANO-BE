# CANO - BE

## 📝 소개
CANO 애플리케이션 백엔드 리포지토리입니다.

## application.properties
src/resources 에 추가
```properties
# MySQL
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/cano
spring.datasource.username=root
spring.datasource.password={ROOT_PASSWORD}

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# JWT
jwt.secret={SECRET_KEY}
```

## 🗃 팀 위키

- [프로젝트 환경 설정(Getting Started) - (준비 중)]()
- [코딩 컨벤션 - (준비 중)]()
- [커밋 컨벤션 - (준비 중)]()
- [깃 브랜치 전략 - (준비 중)]()

## 🔧 기술 스택

- Java
- Spring Boot
- MySQL
- AWS EC2
- Docker

## 💁‍♂️ 프로젝트 팀원

<div align="center">

|  [박계현](https://github.com/gyehyun-bak) | [윤회성](https://github.com/squareCaaat) |
| :-----------------------------------------: | :------------------------------------: |
| <img src="https://github.com/gyehyun-bak.png" width="100"> | <img src="https://github.com/squareCaaat.png" width="100"> |
