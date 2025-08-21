# 바로 인턴 16기 - 로그인 API 과제 (Spring Boot + JWT)

## 📖 개요 (Overview)

Spring Boot 기반 JWT 인증/인가 구현 프로젝트입니다.

* 회원가입, 로그인, 관리자 권한 부여 API 개발
* JUnit 테스트 코드 작성
* Swagger로 API 문서화
* AWS EC2 배포 완료

---

## 🔗 제출 정보

* **GitHub Repository:** \[ https://github.com/YejinY00n/login-api.git ]
* **Swagger UI 주소:** \[ http://15.164.90.92/swagger-ui/index.html ]
* **AWS EC2 엔드포인트 URL:** \[ http://15.164.90.92/ ]

---

## ⚙ 실행 방법 (How to Run)

* **AWS EC2 배포 주소 :** \[ http://15.164.90.92/ ]

---

## 📌 API 명세

Swagger UI를 통해 확인할 수 있으며, 주요 엔드포인트는 아래와 같습니다.

### 회원가입 (Signup)

* **POST** `/signup`

```json
{
  "username": "test1234",
  "password": "12341234",
  "nickname": "Mentos"
}
```

### 로그인 (Login)

* **POST** `/login`

```json
{
  "username": "test1234",
  "password": "12341234"
}
```

### 관리자 권한 부여 (Update User Role)

* **PATCH** `/admin/users/{userId}/roles`

---

## ✅ 요구사항 충족 사항

* JWT 기반 인증/인가 로직 구현 완료
* 회원가입, 로그인, 관리자 권한 부여 API 구현
* Swagger UI 문서화 완료
* AWS EC2 배포 및 실행 확인
* 인메모리 DB를 사용하므로 재시작 시 DB drop
