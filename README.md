# 기대주 분석 플랫폼
> 산학 실전 캡스톤 1인 프로젝트


## 🚩 프로젝트 개요
> 현재 관심 있는 키워드의 뉴스 기사의 감정 분석과 
<br>
영업 이익과 같은 여러 재무제표 정보를 통해 
<br>
주식 투자 가치가 있는지 알려주는 기대주 분석 플랫폼

### 📚 Teck Stack & Tools
![PyTorch Lightning](https://img.shields.io/badge/pytorch-lightning-blue.svg?logo=PyTorch%20Lightning)
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/Kafka-231F20?style=for-the-badge&logo=Apache Kafka&logoColor=white"/> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"/>
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"/>
<img src="https://img.shields.io/badge/logstash-7.15.1-00bfb3?style=for-the-badge&logo=Docker&logoColor=white"/>
<img src="https://img.shields.io/badge/kibana-7.15.1-00bfb3?style=for-the-badge&logo=Docker&logoColor=white"/>
<img src="https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2F900gle%2F900gle&style=for-the-badge&logoColor=white"/>


## 🚩 기술 분석

###✔️ 크롤링
  - 주식 데이터를 수집하기 위한 크롤링
  - 검색 테마주 관련 주식 정보 크롤링
  - 해당 주식 재무 정보 크롤링
  
###✔️ Redis
  - 실시간 순위를 캐시에 저장
  - Redisson Lock을 통해 분산 처리에 따른 데이터 무결성 유지
  
###✔️ Elastic Search
  - 루씬 검색엔진 기반 빠른 데이터 검색
  - 단어 유사성 테스트

###✔️ Jupyter
  - 허깅 페이스의 Korean-sentiment Model을 활용한 뉴스 기사 감정 분석

###✔️ Kafka
  - 실시간 주식 크롤링부터 테마주, 관련 주식 정보까지 모든 크롤링을 분산처리

## 🚩 Application Preview

#### 메인화면
<img width="1384" alt="스크린샷 2023-06-15 오후 3 54 13" src="https://github.com/tjdwns4537/StockTotalService/assets/63576379/9cd51253-a78e-487b-b627-e268cc468230">

#### 검색결과 화면
<img width="923" alt="스크린샷 2023-06-15 오후 3 55 32" src="https://github.com/tjdwns4537/StockTotalService/assets/63576379/d053ab89-f0bf-4a64-82d3-da86e77a0699">
<img width="923" alt="스크린샷 2023-06-15 오후 3 55 42" src="https://github.com/tjdwns4537/StockTotalService/assets/63576379/d0bbd2bc-bc47-42ee-9062-c0ac6b487c92">
<img width="923" alt="스크린샷 2023-06-15 오후 3 55 50" src="https://github.com/tjdwns4537/StockTotalService/assets/63576379/f9c87d90-e4ce-4941-acc5-2317bb876080">

### 📌 PMP 문서
https://docs.google.com/presentation/d/1QMBBblm1YjWw7Bjly6ThhGGXV86cxrUAzUoeHN1-w3c/edit?usp=sharing

### 📌 Architecture
<img width="913" alt="스크린샷 2023-06-15 오후 4 07 34" src="https://github.com/tjdwns4537/StockTotalService/assets/63576379/17f6aea3-46e2-41bf-9070-5d9da318f1aa">

## 🚩 주요기능 및 작업내역

- 엘라스틱 서치 자료 조사 : https://github.com/tjdwns4537/StockTotalService/wiki/%EC%97%98%EB%9D%BC%EC%8A%A4%ED%8B%B1-%EC%84%9C%EC%B9%98-%EC%9E%90%EB%A3%8C%EC%A1%B0%EC%82%AC
- 도커 환경 구성 : https://github.com/tjdwns4537/StockTotalService/wiki/Docker%EC%97%90-ElasticSearch-%EC%84%A4%EC%B9%98
- 엘라스틱 서치 서버 구성 및 연결 테스트 : https://github.com/tjdwns4537/StockTotalService/wiki/Elastic-Search-%EC%A0%81%EC%9A%A9-%ED%85%8C%EC%8A%A4%ED%8A%B8
- 엘라스틱 서치 환경 구성 및 개발 : https://github.com/tjdwns4537/StockTotalService/wiki/%EC%97%98%EB%9D%BC%EC%8A%A4%ED%8B%B1-%EC%84%9C%EC%B9%98-%ED%99%98%EA%B2%BD-%EA%B5%AC%EC%84%B1
- 크롤링 구현 : https://github.com/tjdwns4537/StockTotalService/wiki/%ED%81%AC%EB%A1%A4%EB%A7%81-%EA%B5%AC%ED%98%84
- 실시간 순위 레디스 구현 : https://github.com/tjdwns4537/StockTotalService/wiki/%EB%A0%88%EB%94%94%EC%8A%A4-%EC%84%9C%EB%B2%84-%EA%B5%AC%EC%B6%95-%EB%B0%8F-%EC%8B%A4%EC%8B%9C%EA%B0%84-%EC%88%9C%EC%9C%84-%EC%82%AC%EC%9D%B4%ED%8A%B8-%EA%B5%AC%ED%98%84
- 엘라스틱 서치 초기 구성 : https://github.com/tjdwns4537/StockTotalService/wiki/%EC%97%98%EB%9D%BC%EC%8A%A4%ED%8B%B1-%EC%84%9C%EC%B9%98-%EB%89%B4%EC%8A%A4-%EA%B8%B0%EC%82%AC-%EB%B6%84%EC%84%9D%EC%9D%84-%EC%9C%84%ED%95%9C-%EA%B5%AC%EC%84%B1
- 아키텍처 중간 수정 : https://github.com/tjdwns4537/StockTotalService/wiki/%EA%B8%B0%EB%8A%A5-%EA%B5%AC%EC%84%B1-(%EC%A4%91%EA%B0%84%ED%8F%89%EA%B0%80)
- 구글 코랩 연동 : https://github.com/tjdwns4537/StockTotalService/wiki/%EA%B5%AC%EA%B8%80-%EC%BD%94%EB%9E%A9-%EC%97%B0%EB%8F%99
- 구글 AWS SageMaker, AWS Search Service 활용 : https://github.com/tjdwns4537/StockTotalService/wiki/AWS-SageMaker-%ED%99%9C%EC%9A%A9
- 키워드 감정 분석 및 엘라스틱 서치의 유사성 분석 : https://github.com/tjdwns4537/StockTotalService/wiki/%ED%82%A4%EC%9B%8C%EB%93%9C-%EB%B6%84%EC%84%9D-%EC%82%AC%EC%9A%A9-%EB%B0%A9%EB%B2%95
- 카프카 구성 및 코드 개발 :https://github.com/tjdwns4537/StockTotalService/wiki/%EC%B9%B4%ED%94%84%EC%B9%B4-%ED%99%98%EA%B2%BD-%EA%B5%AC%EC%84%B1%EA%B3%BC-%EA%B4%80%EB%A0%A8-%EC%BD%94%EB%93%9C
