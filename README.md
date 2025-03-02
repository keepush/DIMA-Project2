# 화물 선박 중심 해양 정보 제공 서비스:바다이음 – 바다와 사용자를 연결하다
## 프로젝트 기획 동기
-	선박의 위치, 속력 등의 정보를 무선 통신을 통해 자동 송수신하는 AIS 데이터를 기반으로 선박의 대기 구역을 추출한 기업 연계 프로젝트의 연장선으로, 웹서비스를 개발하게 되었습니다.
-	이전 데이터 분석을 진행하며 해운 산업에 조사한 결과, 선박 정보, 입출항 비용, 항해 관련 통계 자료와 같은 해운업 정보들이 여러 플랫폼으로 분산되어 제공되고 있었습니다.
-	원하는 정보를 얻기 위해서는 다수의 페이지를 통해서 접근할 수 있었으며, 선주부담액 산정법이나 정박지 위치 정보들은 가독성이 낮았습니다.
-	이를 개선하여 사용자의 접근성과 편의성을 높이고자 한 곳에서 종합적으로 모든 정보를 제공하는 웹 서비스를 기획하게 되었습니다.

<br>

## 프로젝트 핵심 기능
1. 메인 화면에서 선박 고유 정보인 Call Sign, MMSI, IMO를 검색하여 실시간 선박 정보, 선박의 목적항 정보, 예상 항만 사용료를 확인할 수 있습니다.
2. 각각의 대시보드를 클릭해 상세 화면으로 들어갈 경우, 더욱 자세한 정보를 구글 지도, 토글, 팝업, 통계, 계산기 기능 등을 활용해 제공합니다.
3. 검색하고자 하는 여러 선박을 등록을 통해 한 번에 관리할 수 있습니다.
4. 마이페이지에서 등록된 여러 선박의 간결한 정보를 확인할 수 있으며, 삭제와 1개의 즐겨찾기 선박을 설정할 수 있습니다. 이 하나의 선박은 로그인 시 메인 화면에 자동으로 표출됩니다.
5. 세션에 선박 고유 정보를 저장하여 다른 페이지로 전달합니다. 다른 페이지에서도 같은 선박의 정보를 자동으로 확인할 수 있습니다.

<br>

## 담당 역할
1.	MySQL을 사용하여 사용자 DB, 선박 DB 중 일부를 담당해 생성하였습니다.
2.	메인 기능인 실시간 선박 정보, 선박 목적항 정보, 항만 사용료 계산기 중에서 항만 사용료 계산기와 마이페이지 기능을 구현하였습니다.
    - 각 페이지를 JAVA를 이용하여 개발
    - 항만 사용료 계산 로직 구현
    - chart.js 이용하여 각 항목의 통계 시각화
    - session을 사용하여 페이지별 선박 고유 정보 전달
    - Fetch API 활용하여 즐겨찾기 선박 설정 및 변경 관리
3. Figma를 사용하여 모든 페이지의 HTML, CSS 디자인을 하였습니다.

<br>

## 프로젝트 구조
![스크린샷 2024-11-04 212306](https://github.com/user-attachments/assets/ca61b2a7-42ce-490a-95d0-b0299e1f33f4)
![스크린샷 2024-11-04 212541](https://github.com/user-attachments/assets/4cf590e0-9fac-4776-a449-477e8190d0eb)

<br>

## ERD
![DB_ERD_241010](https://github.com/user-attachments/assets/1c5c2950-ee3c-4c25-9991-5567f574c89c)

<br>

## 기술 스택
[![My Skills](https://skillicons.dev/icons?i=java,js,spring,mysql,html,css)](https://skillicons.dev)
<br>

## 핵심 담당 코드
### 1. Service.java
[즐겨찾기 서비스 구현](src/main/java/com/kdigital/SecondProject/service/FavoriteVoyageService.java) <br>
[사용자 서비스 구현](src/main/java/com/kdigital/SecondProject/service/UsersService.java) <br>
### 2. HTML
[항만 사용료 계산기](src/main/resources/templates/pages/calculator.html) <br>
[마이페이지](src/main/resources/templates/pages/mypage.html) <br>
### 3. js
[항만 사용료 계산 로직과 저장, 초기화, chart.js 활용](src/main/resources/static/script/calculator.js) <br>
[마이페이지 즐겨찾기 선박 설정 및 변경](src/main/resources/static/script/mypage.js) <br>

<br>

## View
### 1. 비회원 메인 화면
![스크린샷 2024-11-04 213412](https://github.com/user-attachments/assets/bfc8ac16-6302-42b9-adab-5da298a20331)
### 2. 회원 메인 화면
![스크린샷 2024-11-04 213435](https://github.com/user-attachments/assets/ae755272-04fe-4201-b000-fa291536e364)
### 3. 마이페이지 화면
![스크린샷 2024-11-04 213459](https://github.com/user-attachments/assets/4fc41d15-e059-449a-9848-a1f850ad281f)
### 4. 항만 사용료 계산기 화면
![스크린샷 2024-11-04 213447](https://github.com/user-attachments/assets/e65490e5-1045-4742-aec5-aede0e842c76)
### 5. 선박 정보 화면
![스크린샷 2024-11-04 213821](https://github.com/user-attachments/assets/7bdf2310-9bdb-4f40-bc02-965e9ed3079d)
### 6. 목적지 정보 화면
![스크린샷 2024-11-04 213832](https://github.com/user-attachments/assets/6ac0d477-b163-4b73-b287-55a8c4b8987c)

