# 게시판 애플리케이션

created on 2021. 7. 18

---

## Todo

### Domain

- **Entity**
    - [x] Member
    - [x] Board
    - [x] Comment


- **Repository**
    - [x] Member
    - [x] Board
    - [x] Comment

### Service

- **Member**
    - [x] join
    - [x] withdrawal
    - [x] login
    - [x] getMember
    - [ ] searchMember (세모)


- **Board**
    - [x] createBoard
    - [x] deleteBoard
    - [ ] updateBoard
    - [ ] getBoard
    - [ ] searchBoard
    - [ ] getBoardList


- **Comment**
    - [ ] createComment
    - [ ] deleteComment
    - [ ] updateComment

### Controller

- **Member**
    - [x] 회원가입
    - [x] 회원탈퇴
    - [x] 로그인
    - [x] 회원 상세 조회
    - [ ] 회원 검색 (게시물과 함께) (세모)


- **Board**
    - [x] 글 쓰기
    - [ ] 글 삭제
    - [ ] 글 수정
    - [ ] 글 조회
    - [ ] 글 검색
    - [ ] 글 목록


- **Comment**
    - [ ] 댓글 작성
    - [ ] 댓글 수정
    - [ ] 댓글 삭제

### DTO