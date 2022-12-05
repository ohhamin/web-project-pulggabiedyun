## 목차

- [기술 스택 & 버전 정보](#기술-스택-&-버전-정보)
- [빌드 방법](#빌드-방법)
- [docker & jenkins](#docker-&-jenkins)
- [모니터링](#모니터링)



## 기술 스택 & 버전 정보

1. 이슈 관리 : ![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white)

2. 형상 관리 : ![GitLab](https://img.shields.io/badge/gitlab-%23181717.svg?style=for-the-badge&logo=gitlab&logoColor=white)
3. 커뮤니케이션 : ![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white) ![Discord](https://img.shields.io/badge/discord-5865F2.svg?style=for-the-badge&logo=discord&logoColor=white)
4. 개발 환경

- IDE : ![IntelliJ](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)2022.1.2 ![Visual_Studio_Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white)1.71.2
- DB : ![MySQL](https://img.shields.io/badge/MySQL-003545?style=for-the-badge&logo=mysql&logoColor=white) 8.0.30
- UI & UX : ![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)
- Server : ![AWS](https://img.shields.io/badge/AWS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) ![NGINX](https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white) 1.18.0

5. 상세

- BE : ![JAVA](https://img.shields.io/badge/JAVA-FF0000?style=for-the-badge&logoColor=white) 8 ![Spring](https://img.shields.io/badge/Spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)2.7.3 ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)7.5 ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)0.11.2 ![Node.js](https://img.shields.io/badge/Node.js-339933?style=for-the-badge&logo=node.js&logoColor=white)16.16.0
- FE : ![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)![CSS3](https://img.shields.io/badge/css3-1572B6?style=for-the-badge&logo=css3&logoColor=white)![JAVASCRIPT](https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white) ![Vue.js](https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white) 3.2.39 ![Three.js](https://img.shields.io/badge/Three.js-000000?style=for-the-badge&logo=Three.js&logoColor=white)0.145.0 ![Blender](https://img.shields.io/badge/Blender-F5792A?style=for-the-badge&logo=Blender&logoColor=white) 
- CI/CD : ![docker](https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)20.10.18 ![Jenkins](https://img.shields.io/badge/jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white) 2.222.4
- Monitoring : ![Grafana](https://img.shields.io/badge/grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white) ![Prometheus](https://img.shields.io/badge/prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white)



## 빌드 방법

### BE

1. BackEnd -> Dokcho2 프로젝트 열기
2. JDK 1.8 버전 확인
3. src -> main -> Dokcho2Application class 실행

### FE

1. npm install --force
2. npm run serve

### NODE.JS(Socket 서버)

1. npm install
2. node quiz_server.js





## docker & jenkins

1. 서버 접속
2. 기본 설치

```bash
$ sudo apt install upgrade
$ sudo apt install update

# JAVA 설치
$ sudo apt install openjdk-8-jdk

# JAVA_HOME 설정
# ~/.bashrc에 export 추가
$ sudo vi ~/.bashrc
export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")

# 적용
$ source ~/.bashrc

# 방화벽 설정
$ sudo ufw allow 22
# 22 port를 열어놓지 않으면 ssh로 접근 불가능하므로 꼭 열어야 한다.
$ sudo ufw enable
# 상태 확인
$ sudo ufw status
```

3. docker 설치 & 이미지 받기

```bash
$ sudo apt update

# http 패키지 설치
$ sudo apt-get install -y ca-certificates curl software-properties-common apt-transport-https gnupg lsb-release

# 레포지토리 설정
# docker의 Official GPG Key 등록
$ $ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

# stable repository 등록
$ echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# docker 엔진 설치
$ sudo apt install docker-ce docker-ce-cli containerd.io

# docker 그룹에 사용자 추가
$ sudo usermod -aG docker ubuntu

# MySQL 이미지 다운로드
$ docker pull mysql

# MySQL docker 컨테이너 생성 및 실행
$ docker run --name mysql-container -e MYSQL_ROOT_PASSWORD={password} -d -p 3306:3306 mysql:latest

# docker 컨테이너 리스트 출력
$ docker ps -a

# MySQL 컨테이너 접속
$ docker exec -it mysql-container bash
```

4. jenkins 설치 및 실행

```bash
# docker 이미지 다운로드
$ docker pull jenkins/jenkins:lts-jdk11

# 이미지 실행
$ docker run -d -p 20001:8080 -p 50000:50000 -v /var/jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock --name jenkins -u root jenkins/jenkins:lts-jdk11

# 컨테이너 내부 접근
$ docker exec -it <container_id> bash
```

5. jenkins 내부에 docker 설치

```bash
$ curl https://get.docker.com/ > dockerinstall && chmod 777 dockerinstall && ./dockerinstall
```

6. ssl 설정

```bash
$ cd
$ sudo mkdir certbot
$ cd certbot
$ sudo mkdir conf www logs
$ sudo docker pull certbot/certbot
$ sudo docker run -it --rm --name certbot -p 80:80 -v "/home/ubuntu/certbot/conf:/etc/letsencrypt" -v "/home/ubuntu/certbot/log:/var/log/letsencrypt" -v "/home/ubuntu/certbot/www:/var/www/certbot" certbot/certbot certonly

# 실행 시 나타나는 선택 창에서 standalone, agree, no, <domain_name> 순서로 입력
```

7. nginx 설정(/home/ubuntu/nginx/conf/default.conf)

```bash
server{
        listen 80 default_server;
        listen [::]:80 default_server;
        server_name k7e203.p.ssafy.io;
        return 301 https://$host$request-uri;
}
server{
        listen 443 ssl;
        listen [::]:443;
        server_name k7e203.p.ssafy.io;
        access_log /var/log/nginx/access.log;
        error_log /var/log/nginx/error.log;
        ssl_certificate /etc/letsencrypt/live/k7e203.p.ssafy.io/fullchain.pem;
        ssl_certificate_key /etc/letsencrypt/live/k7e203.p.ssafy.io/privkey.pem;
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2 SSLv3;
        ssl_ciphers ALL;
		location / {
                root /usr/share/nginx/html;
                index index.html;
                try_files $uri $uri/ /index.html;
                proxy_redirect off;
                charset utf-8;
        }
        location /socket.io {
                proxy_pass http://k7e203.p.ssafy.io:3001;
                charset utf-8;
                proxy_redirect off;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header X-NginX-Proxy true;
                proxy_set_header X-Forwarded-Proto $scheme;
        }
}

```

8. jenkins 설정

```
1. jenkins 웹 접속(http://k7e203.p.ssafy.io:20001)
2. jenkins login(docker logs로 확인)
3. jenkins 관리 -> 플러그인 관리 -> gitlab, docker 관련된 것들 설치
4. 새로운 item -> freestyle project
```

9. jenkins 구성 및 gitlab 연동 설정

![image](https://user-images.githubusercontent.com/51230515/200231086-8b31e647-0b7c-472a-9b64-04a13d8399f0.png)

![image](https://user-images.githubusercontent.com/51230515/200231174-370a6c6c-cf6c-46f6-8d15-0056cded0e21.png)

![image](https://user-images.githubusercontent.com/51230515/200231209-8d7ab41c-759c-441a-8d20-8d5a111870f5.png)

![image](https://user-images.githubusercontent.com/51230515/200231267-f8c7545c-80a9-4720-8ded-07085b32c6f1.png)

![image](https://user-images.githubusercontent.com/51230515/200231339-65ae3f2f-3166-4d23-9823-264e573858f1.png)

여기서 생성한 토큰을 복사

![image](https://user-images.githubusercontent.com/51230515/200231502-11fad7a5-c9c4-4040-a700-3df5364a324f.png)

gitlab 프로젝트 - setting - webhook에서 자동 빌드 설정을 해준다.

10. jenkins execute shell(backend)

```bash
cd BackEnd
cd Dokcho2
chmod +x ./gradlew
./gradlew build
docker ps -f name=backend -q | xargs --no-run-if-empty docker container stop
docker container ls -a -f name=backend -q | xargs -r docker container rm
docker build -t backend .
docker run -p 8080:8081 -d -e TZ=Asia/Seoul --name=backend backend -h bserver
docker rmi -f $(docker images -f "dangling=true" -q) || true
```

11. jenkins execute shell(frontend)

```bash
cd FrontEnd
cd dokcho2

docker ps -f name=front -q | xargs --no-run-if-empty docker container stop
docker container ls -a -f name=front -q | xargs -r docker container rm
docker build -t front .
docker run -d -p 80:80 -p 443:443 -v /home/ubuntu/certbot/conf:/etc/letsencrypt/ -v /home/ubuntu/certbot/www:/var/www/certbot -v /home/ubuntu/nginx/conf/default.conf:/etc/nginx/conf.d/default.conf --name front front
docker rmi -f $(docker images -f "dangling=true" -q) || true
```

12. jenkins execute shell(nodejs)

```bash
cd Node

docker ps -f name=node -q | xargs --no-run-if-empty docker container stop
docker container ls -a -f name=node -q | xargs -r docker container rm
docker build -t node .
docker run -d -p 3001:3001 -v /home/ubuntu/certbot/conf:/etc/letsencrypt/ -v /home/ubuntu/certbot/www:/var/www/certbot --name node node
docker rmi -f $(docker images -f "dangling=true" -q) || true
```

13. dockerfile(backend)

```bash
FROM openjdk:8-jdk-alpine

ADD build/libs/Dokcho2-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar","-Dspring.profiles.active=gcp", "/app.jar"]
EXPOSE 8081
```

14. dockerfile(frontend)

```bash
FROM node:lts-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install --force
COPY . .
RUN npm run build
# production stage
FROM nginx:stable-alpine as production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

15. dockerfile(nodejs)

```bash
FROM node:16-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install --silent
COPY . .

EXPOSE 3001
CMD [ "node", "quiz_server.js"]
```



## 모니터링

### Node exporter

1. node_exporter 설치

```bash
$ sudo wget https://github.com/prometheus/node_exporter/releases/download/v*/node_exporter-1.4-amd64.tar.gz
$ sudo tar xvfz node_exporter-1.4-amd64.tar.gz
```

2. 실행

```bash
$ cd node_exporter-1.4-amd64
$ sudo nohup ./node_exporter &
```



### MySQL exporter

1. mysqld_exporter 설치

```bash
$ sudo wget https://github.com/prometheus/mysqld_exporter/releases/download/v0.14.0/mysqld_exporter-0.14.0.linux-amd64.tar.gz
$ sudo tar xvfz mysqld_exporter-0.14.0.linux-amd64.tar.gz
```

2. mysql 계정 생성

```mysql
CREATE USER 'test'@'%' IDENTIFIED BY 'test123' WITH MAX_USER_CONNECTIONS 3;
GRANT PROCESS, REPLICATION CLIENT, SELECT ON *.* TO 'test'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

3. my.cnf 작성(/root/.my.cnf)

```bash
[client]
user=test
password=test123
```

4. 실행

```bash
$ cd mysqld_exporter-0.14.0.linux-amd64
$ sudo nohup ./mysqld_exporter &
```

### Grafana & Prometheus

1. docker-compose.yml 작성(/home/monitor/docker-compose.yml)

```bash
version: '3.8'
networks:
        monitor:
                driver: bridge

services:
   prometheus:
     ports:
       - "9090:9090"
     image: prom/prometheus:latest
     container_name: prometheus
     user: root
     volumes:
       - /home/monitor/prometheus/:/etc/prometheus/
       - /home/monitor/prometheus/data:/prometheus
     networks:
       - monitor

   grafana:
     ports:
       - "3000:3000"
     container_name: grafana
     image: grafana/grafana:latest
     user: root
     environment:
       - GF_SECURITY_ADMIN_USER={아이디}
       - GF_SECURITY_ADMIN_PASSWORD={비밀번호}
       - GF_USERS_ALLOW_SIGN_UP=false
     volumes:
       - /home/monitor/grafana:/var/lib/grafana
       - /home/monitor/grafana/provisioning:/etc/grafana/provisioning
     depends_on:
       - prometheus
     networks:
       - monitor

```

2. promethues.yml 작성(/home/monitor/prometheus/prometheus.yml)

```bash
global:
        scrape_interval: 15s
        evaluation_interval: 15s

scrape_configs:
        - job_name: node
          static_configs:
          - targets: ['3.38.97.194:9100']
        - job_name: mysql
          static_configs:
          - targets: ['3.38.97.194:9104']
```

3. 방화벽 열기

```bash
$ sudo ufw allow 9100
$ sudo ufw allow 9104
```

4. 실행

```bash
$ docker-compose up -d
```
