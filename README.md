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
- 회원가입, 본인인증, 로그인/로그아웃, 아이디/비밀번호 찾기(임시 메일)
- 마이 페이지, 프로필 사진 기능을 구현했습니다.

### 관리자 기능
- 공지사항, 회원 관리, 스토어 관리, 상품 관리를 구현했습니다.

### 전자 상거래 기능
#### - 스토어 기능
- 스토어 등록, 스토어 수정
- 배너 등록, 배너 삭제
- 카테고리 등록, 카테고리 수정, 카테고리 순서 변경
- 스토어 목록, 스토어 페이지를 구현했습니다.

#### - 상품 기능
- 상품 등록, 상품 수정, 상품 삭제
- 상품 페이지, 상품 구매 기능을 구현했습니다.
- 상품 페이지에서는 해당 상품에 쓰여진 리뷰를 보여주는 기능도 있습니다.
- 그 외 평점과 리뷰에 등록된 사진도 같이 볼 수 있게 구현했습니다.

#### - 주문목록 기능
- 상품 구매 후 구매한 상품에 대한 상세보기 기능을 구현했습니다.
- 자신이 구매한 상품의 목록을 구현했습니다.
- 상세보기, 목록에서 구매 확정, 주문 취소, 리뷰쓰기(평점 포함) 기능을 구현했습니다.

#### - 장바구니
- 자신이 원하는 상품을 장바구니에 담는 기능을 구현했습니다.

#### - 찜하기
- 자신이 좋아하는 스토어, 상품을 찜 하는 기능을 구현했습니다.

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
