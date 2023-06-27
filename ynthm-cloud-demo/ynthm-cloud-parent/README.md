# Read Me First
```shell
# 独立项目 需要单独先 install 或 deploy
./mvnw clean install -f saas-dependencies/pom.xml
```

```xml
<!-- https://mvnrepository.com/artifact/org.codehaus.mojo/versions-maven-plugin -->
<dependency>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>versions-maven-plugin</artifactId>
    <version>2.16.0</version>
</dependency>
```

```shell
mvn versions:set -DprocessAllModules=true -DnewVersion=1.0.0
```
