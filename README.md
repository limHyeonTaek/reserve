# 매장 예약 서비스 API

## 구현 리스트
✅ Mission 1
- 공통 인증 구현
- 매장의 점장은 예약 서비스 앱에 상점을 등록한다.(매장 명, 상점위치, 상점 설명)
- 매장을 등록하기 위해서는 파트너 회원 가입이 되어야 한다.(따로, 승인 조건은 없으며 가입 후 바로 이용 가능)

✅ Mission 2
- 매장 이용자는 앱을 통해서 매장을 검색하고 상세 정보를 확인한다.
- 매장의 상세 정보를 보고, 예약을 진행한다. (예약을 진행하기 위해서는 회원 가입이 필수적으로 이루어 져야 한다.)

✅ Mission 3
- 서비스를 통해서 예약한 이후에, 예약 10분전에 도착하여 키오스크를 통해서 방문 확인을 진행한다.
- 예약 및 사용 이후에 리뷰를 작성할 수 있다.
- 서비스 이용 중 애로사항 발생 - 점장은 승인/예약 거절을 할 수 있다.

---
## 개발 환경
- Java 17
- Spring boot 3.1.5
- Spring security 3.1.5
- Gradle 8.4
- Spring data JPA
- Querydsl
- MySQL
---
## 외부 라이브러리 목록

1. io.jsonwebtoken:jjwt 0.12.3 : JWT 발행을 위한 기능을 제공하는 라이브러리
2. org.apache.httpcomponents.client5 5.2.1 : HTTP 클라이언트 기능을 제공하는 라이브러리
3. com.querydsl:querydsl 5.0.0 : SQL 등의 쿼리를 타입에 안전하게 생성 및 관리해주는 프레임워크
4. org.hibernate:hibernate-spatial 6.2.13 : 지리 데이터에 대한 표준 인터페이스를 제공하는 라이브러리
---

## ERD
![erd](erd/reservation-2.png)
