# 어드민 서비스

게시판 서비스의 관리 도구를 개발해보는 프로젝트입니다.

## 개발 환경

* Intellij IDEA Ultimate 2022.2.3
* Java 17
* Gradle 7.6.1
* Spring Boot 2.7.12

## 기술 세부 스택

Spring Boot

* Spring Boot Actuator
* Spring Data JPA
* Spring Security OAuth 2 Client
* Spring Security
* Thymeleaf
* Spring Web
* Spring WebSocket
* Lombok
* Spring Boot DevTools
* H2 Database
* MySQL Driver
* Spring Configuration Processor

그 외

* AdminLTE 3.2
* webjars-locator-core
* sockjs-client
* stomp-websocket

## 데모 영상

[데모 영상 확인하기](https://drive.google.com/file/d/1WRgSVrnn0kCMgit2mZFjMo-2mzppBg5S/view?usp=sharing)

* 구현 기능
    - 일반 로그인, 카카오 로그인
    - 게시판의 게시글, 댓글 보기 및 삭제
    - 권한 : 사용자, 운영자, 개발자, 관리자
    - 운영자, 개발자, 관리자만 삭제 기능 사용 가능
    - 부가 기능 : Todo 리스트, 다크모드, chat봇, 방문자 집계

## Reference

* 게시판 서비스: https://github.com/hajungIm/project-board
