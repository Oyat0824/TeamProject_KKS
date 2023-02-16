# TeamProject_KKS
코리아IT아카데미 웹&amp;앱 개발, 팀 프로젝트

- [프로젝트 기여율](https://github.com/Oyat0824/TeamProject_KKS/graphs/contributors)

# 사용한 기술
- HTML/CSS
- JavaScript
- Jquery
- Java
- Spring Boot
- JSP
- MariaDB(MySql)

# 기능 구현
### 회원 기능
- 회원가입, 본인인증
- 로그인/로그아웃
- 아이디/비밀번호 찾기 (메일을 통해 임시 비밀번호 전송)
- 마이페이지 (회원정보 보기 및 수정)
- 프로필 사진

### 관리자 기능
- 공지사항 작성
- 회원 관리
- 스토어 관리
- 상품 관리

### 전자 상거래 기능
#### - 스토어 기능
- 스토어 등록 및 수정
- 스토어 배너 등록 및 삭제
- 상품 카테고리 등록, 수정, 삭제
- 스토어 목록, 스토어 상세보기
- 관심 있는 스토어 관심 스토어에 담기

#### - 상품 기능
- 상품 등록, 수정, 삭제
- 상품 목록, 상품 상세보기
- 상품 구매, 상품 리뷰 및 평점
- 장바구니에 상품 담기
- 관심 있는 상품 찜 목록에 담기

#### - 주문조회 기능
- 주문 내역 목록 보기
- 주문 내역 상세 보기
- 주문 내역 취소, 구매 확정
- 구매 확정 후 리뷰 쓰기
- 판매자 주문 확인 및 송장 번호 작성

#### - 검색 및 정렬 기능
- 스토어 및 상품 검색
- 번호, 등록일, 리뷰 많은, 구매 많은 순으로 정렬 기능

# 사용한 라이브러리
> 메일 발송 및 파일 관리, JSP를 위해 사용한 라이브러리입니다.
- [JSTL](https://mvnrepository.com/artifact/javax.servlet/jstl)
- [Tomcat Embed Jasper](https://mvnrepository.com/artifact/org.apache.tomcat.embed/tomcat-embed-jasper)
- [Spring Context Support](https://mvnrepository.com/artifact/org.springframework/spring-context-support)
- [JavaMail API](https://mvnrepository.com/artifact/com.sun.mail/javax.mail)
- [Guava: Google Core Libraries For Java](https://mvnrepository.com/artifact/com.google.guava/guava)
- [Project Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok)

> 디자인과 텍스트 에디터, 슬라이드를 위해 사용한 라이브러리입니다.
- [제이쿼리](https://jquery.com/)
- [폰트어썸](https://fontawesome.com/)
- [슬릭](https://kenwheeler.github.io/slick/)
- [테일윈드](https://tailwindcss.com/)
- [데이지UI](https://daisyui.com/)
- [토스트UI](https://ui.toast.com/)

# 사용한 API
- [아임포트](https://portone.io/korea/ko)
  - 휴대폰 본인 인증 및 결제 시스템을 위해 사용한 API
- [도로명 주소 검색](https://business.juso.go.kr/addrlink/openApi/apiExprn.do)
  - 회원 가입 시 주소를 받거나, 구매 완료 후 배송지를 정하기 위해 사용한 API
- [스마트 택배](https://tracking.sweettracker.co.kr/)
  - 상품 등록 시 어느 택배사를 사용할지 택배사 목록을 보여주기
  - 상품 구매 후 판매자가 운송장 번호를 입력해준다면 해당 운송장 번호를 통해 조회하는 API
