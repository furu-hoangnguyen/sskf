FROM tomcat:latest
ADD build/libs/app.war /usr/local/tomcat/webapps/
VOLUME ./usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]