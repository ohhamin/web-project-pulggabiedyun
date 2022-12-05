# [풀깨비뎐](https://k7e203.p.ssafy.io/ "풀깨비뎐 바로가기!") 🌿

![풀깨비뎐](./README.assets/풀깨비뎐.png)

## 목차

- [풀깨비뎐](#풀깨비뎐)
  - [목차](#목차)
  - [서비스 소개](#서비스-소개)
    - [📋 기술 스택 및 버전](#기술-스택-및-버전)
  - [프로젝트 파일 구조](#프로젝트-파일-구조)
  - [산출물](#산출물)
  - [결과물](#결과물)
  
- [📺 프로젝트 UCC]()

## 서비스 소개

1. 개발 기간 : 2022.10.11 ~ 2022.11.21 (총 6주)
2. 인원 (총 6인)

| [서상균](https://github.com/seosanggyun) | [김성빈](https://github.com/ksb0903) |  [김효근]()  | [박지현](https://github.com/petteloiv) | [오하민](https://github.com/ohhamin) |  [이근희]()   |
| :--------------------------------------: | :----------------------------------: | :----------: | :------------------------------------: | :----------------------------------: | :-----------: |
|      **팀장, Front-end, Back-end**       |         **Back-end, DevOps**         | **Back-end** |             **Front-end**              |      **Front-end, 3D Modeling**      | **Front-end** |



## 기술 스택 및 버전

1. 이슈 관리 : ![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white)
2. 형상 관리: ![GitLab](https://img.shields.io/badge/gitlab-%23181717.svg?style=for-the-badge&logo=gitlab&logoColor=white)
3. 커뮤니케이션: ![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white) ![Discord](https://img.shields.io/badge/discord-5865F2.svg?style=for-the-badge&logo=discord&logoColor=white) 
4. 개발 환경 
   - IDE : ![IntelliJ](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)2022.1.2 ![Visual_Studio_Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white)1.71.2
   - DB : ![MySQL](https://img.shields.io/badge/MySQL-003545?style=for-the-badge&logo=mysql&logoColor=white) 8.0.30 !
   - UI & UX : ![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white) 
   - Server : ![AWS](https://img.shields.io/badge/AWS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) ![NGINX](https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white) 1.18.0
5. 상세
   - Backend : ![JAVA](https://img.shields.io/badge/JAVA-FF0000?style=for-the-badge&logoColor=white) 8 ![Spring](https://img.shields.io/badge/Spring-boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)2.7.3 ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)7.5 ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)0.11.2 ![Node.js](https://img.shields.io/badge/Node.js-339933?style=for-the-badge&logo=node.js&logoColor=white)16.16.0 ![python](https://img.shields.io/badge/python-3776AB?style=for-the-badge&logo=python&logoColor=white)3.7
   - Frontend : ![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)![CSS3](https://img.shields.io/badge/css3-1572B6?style=for-the-badge&logo=css3&logoColor=white)![JAVASCRIPT](https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white)![Vue.js](https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white) 3.2.39 ![Three.js](https://img.shields.io/badge/Three.js-000000?style=for-the-badge&logo=Three.js&logoColor=white)0.145.0 ![Blender](https://img.shields.io/badge/Blender-F5792A?style=for-the-badge&logo=Blender&logoColor=white) 
   - CI/CD : ![docker](https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)20.10.18 ![Vue.js](https://img.shields.io/badge/jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white) 2.222.4

## 프로젝트 파일 구조

- **Backend**

  ```
  main
  ├─generated
  ├─java
  │  └─com
  │      └─ssafy
  │          └─dockcho2
  │             ├─config
  │             ├─controller
  │             ├─domain
  │             │  ├─basetime
  │             │  ├─enums
  │             │  ├─item
  │             │  ├─minigame
  │             │  ├─mission
  │             │  ├─monster
  │             │  ├─user
  │             │  ├─userItem
  │             │  └─userMonster
  │             ├─dto
  │             │  ├─exception
  │             │  │  ├─common
  │             │  │  ├─item
  │             │  │  ├─mission
  │             │  │  ├─monster
  │             │  │  └─user
  │             │  ├─item
  │             │  ├─jwt
  │             │  ├─minigame
  │             │  ├─mission
  │             │  ├─monster
  │             │  ├─oauth2
  │             │  └─user
  │             ├─jwt
  │             ├─service
  │             │  ├─item
  │             │  ├─jwt
  │             │  ├─minigame
  │             │  ├─mission
  │             │  ├─monster
  │             │  ├─oauth2
  │             │  └─user
  │             └─util
  └─resources
  ```

- **Frontend** 

  ```
  rc
  ├─assets
  │  ├─battle
  │  ├─item
  │  ├─loading
  │  ├─minigame
  │  ├─mission
  │  ├─monsters
  │  ├─mypage
  │  ├─navbar
  │  ├─quiz
  │  └─starting
  ├─components
  │  ├─accounts
  │  ├─battle
  │  ├─canvas
  │  ├─ending
  │  │  ├─arena
  │  │  ├─friend
  │  │  ├─gg
  │  │  └─shop
  │  ├─main
  │  ├─minigame
  │  ├─modules
  │  ├─monster
  │  ├─script
  │  └─start
  ├─constant
  ├─fonts
  ├─router
  ├─store
  │  └─modules
  └─views
     ├─canvas
     ├─error
     └─start
  
  ```

  <br><br>

  ## 산출물

- [프로젝트 메모 및 공유 : Notion](https://www.notion.so/faef428821b04f3ea6a4324aa6c8aed9)

- [프로젝트 회의록](https://www.notion.so/ea1a637672b244fd8a1240ab0d94e74c?v=9199099e636044a9b20ae53103f6cdd7)

  <br>

- [프로젝트 컨벤션 목록](https://www.notion.so/2ef6f198c0c74ae998c9646c8ef13654)

- [기획서](https://www.notion.so/54eab313652a4caea007b8397d0297e8)

- [API Docs](https://www.notion.so/API-Docs-38bc08bf6c9e43f99d0844ff5f3c91a4)

- [서비스 아키텍처](./docs/아키텍처.md) 

- [기능 명세서](https://www.notion.so/3a54b3a7161244d78facf5d56f31191c?v=9301e374c0fd4906925ce21200b995de)

- [데이터베이스:ERD](https://www.erdcloud.com/d/AhhMDN9Ez33s5uWC3) 

  <br><br>

  ## 결과물 

- [포팅메뉴얼](./exec/porting_manual.md)

- [시연시나리오](./exec/[시연시나리오]자율_PJT_부울경_2반_E203.pdf)

- [중간발표자료](./pt/[중간발표]자율_PJT_부울경_2반_E203.pdf)

- [최종발표자료](./pt/[최종발표]자율_PJT_부울경_2반_E203.pdf)
