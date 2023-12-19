<div><h1>협업기반의 그룹웨어 CG</h1>
  <img src="https://github.com/Classy-Potatoes/Server_Classy-Groupware/assets/138549312/cdde1935-b8c0-4fab-ae51-812d4ca2a69b" width="300" height="250"/></div>
<br>

## :clipboard: 개요
- 기업메일, 기업메신저, 전자결재 등 여러 협업 기능을 제공하는 업무협업 툴 제작
      <br>
- 개발기간 : 2023.11.13 ~ 2023.12.17 (약 4주)
- 개발인원
  강인선 (https://github.com/popipopipoi) - 대시보드 / 프로젝트 메인 / 프로젝트 게시글 / 프로젝트 업무
  김형수 (https://github.com/brorsoo) - 회원가입 / 로그인 / 마이페이지 / 연락망 / 관리기능
  박우석 (https://github.com/woose0k) - 프로젝트 일정 / 프로젝트 할일 / 캘린더
  박준홍 (https://github.com/tinypig705) - 전자결재
  황주희 (https://github.com/ctmeou) - 쪽지함
<br>
<br>

## 🔗: 프로젝트 산출물
- Notion
  https://classygroupware.notion.site/classygroupware/s-ec48f8bb03bf410ebf829b7728ceb461
<br>

- GitHub
  - BackEnd
    https://github.com/Classy-Potatoes/Server_Classy-Groupware
  - FrontEnd
    https://github.com/Classy-Potatoes/Front_Classy-Groupware

## :clipboard: 개발환경
<div>
<img src="https://github.com/Classy-Potatoes/Server_Classy-Groupware/assets/138549312/fe946948-2040-4b80-92f1-d76277502ecd" width="1000" height="430"/></div>

<br>
<br>

## 주요 기능
- 사용자간의 쪽지 기능으로 업무 커뮤니케이션 효과 증진
- 일정 관리 캘린더를 제공하여 기업의 스케줄 관리
- 간편한 전자결재로 서류 업무 효율 극대화
- 기업에서 진행하는 프로젝트들을 관리 할 수 있다. 
- 인명관리를 통해 기업 인사 관리를 할 수 있다.

<br>
<br>

## 프로젝트 기능 구현

* **강인선** (https://github.com/popipopipoi) - 대시보드 / 프로젝트 메인 / 프로젝트 게시글 / 프로젝트 업무

   * 대시보드
     
      * 공지게시판, 프로젝트, 전자결재, 업무, 받은 쪽지함 상위 게시글 조회.
      * OpenWeather 를 활용한 날씨 조회
        
   * 프로젝트
     
      * 메인
        * 프로젝트 생성, 수정, 삭제
        * 프로젝트 참여자 초대
    
      * 게시글
        * 게시글 생성, 수정, 삭제
        * 첨부파일 추가
        * 댓글 작성, 수정, 삭제

      * 업무글
        * 업무글 생성, 수정, 삭제
        * React-Select로 담당자 추가
        * 댓글 작성, 수정, 삭제
        * datepicker를 활용한 날짜 설정

* **김형수** (https://github.com/brorsoo) - 회원가입 / 로그인 / 마이페이지 / 연락망 / 관리기능

   * 회원 정보
     
      * 로그인
        * 스프링 시큐리티로 계정 권한부여 및 계정 잠금
        * JWT 토큰 활용 로그인
        * 아이디 찾기
        * mailsender를 활용한 비밀번호 찾기
          
      * 회원가입
        * 프로필 이미지 등록
        * 사번 검사로 회원가입 가능
        * BCrypt 해시 함수로 비밀번호 암호화
        * 아이디 중복 검사
        * Kakao지도 활용한 주소찾기
       
      * 마이페이지
        * 회원 정보 조회, 수정
        * 비밀번호 수정
        * 회원반납

      * 연락망
        * 회원 목록 조회
        * 검색으로 회원 조회
        * 회원 상세 조회

   * 회원 관리
     
      * 관리 기능
        * 사전 회원등록(사번 부여)
        * 미등록 회원 목록 조회, 삭제
        * 전체 회원 정보 조회, 수정


* **박우석** (https://github.com/woose0k) - 프로젝트 일정 / 프로젝트 할일 / 캘린더

   * 프로젝트
     
      * 일정글
        * 일정글 생성, 수정, 삭제
        * datepicker를 활용한 날짜 설정
        * 담당자 조회후 추가
        * 댓글 작성, 수정, 삭제

      * 할일글
        * 할일글 생성, 수정, 삭제
        * 행 추가시 할일 추가 생성
        * 버튼으로 완료여부 설정
        * 담당자 조회후 추가
        * 댓글 작성, 수정, 삭제
       
    * 캘린더
     
      * 프로젝트 일정, 개인 일정 구분 후 조회
      * 개인 일정 생성, 수정, 삭제


* **박준홍** (https://github.com/tinypig705) - 전자결재

   * 전자결재
     
      * 결재상신
        * 기안서 작성 - 품의서
           * 검색기능으로 수신 참조자 조회 후 등록
           * beautiful pnp를 활용한 결재자 순서 정렬후 추가
        * 기안서 작성 - 지출결의서
           * 행 추가로 다중 지출 등록
        * 기안서 작성 - 휴가신청서
           * datepicker를 활용한 날짜 설정 및 휴가 등록
        * 상신함
           * 상신한 문서를 상태별(결재대기,결재중,승인,반려,회수)로 조회
           * 결재대기 상태일때만 회수 가능
           * 셀렉트박스로 다중 선택후 회수
        * 결재함
           * 결재자에게 온 문서들을 결재처리 가능
           * 결재함 문서를 상태별(결재중, 승인, 반려)로 조회

* **황주희** (https://github.com/ctmeou) - 쪽지함

   * 쪽지함
     
      * 쪽지를 받은 쪽지, 보낸 쪽지, 중요 쪽지별 조회
      * 쪽지 카테고리 별 검색
      * 받은 쪽지 중요 쪽지함으로 이동
      * 전체 직원 조회
      * 쪽지 전송, 수정, 삭제

## 🥔: ERD 설계

<div>
  <img src="https://github.com/Classy-Potatoes/Server_Classy-Groupware/assets/138549312/8f063643-bab4-4d38-b6ea-3dcaa54a4e30" width="1400" height="430"/>
  <img src="https://github.com/Classy-Potatoes/Server_Classy-Groupware/assets/138549312/0d8851f7-250c-468a-b059-da09016091d7" width="1400" height="430"/>
</div>
