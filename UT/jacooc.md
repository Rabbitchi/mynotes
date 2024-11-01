```
<plugin>  
            <groupId>org.jacoco</groupId>  
            <artifactId>jacoco-maven-plugin</artifactId>  
            <version>您的JaCoCo版本</version>  
            <executions>  
                <execution>  
                    <goals>  
                        <goal>instrument</goal> <!-- 仪器化目标 -->  
                        <goal>restore-instrumented-classes</goal> <!-- 恢复原始类目标 -->  
                        <goal>report</goal> <!-- 生成报告目标 -->  
                    </goals>  
                </execution>  
            </executions>  
            <configuration>  
                <!-- 配置离线模式 -->  
                <mode>offline</mode>  
                <!-- 其他配置，如输出目录、报告格式等 -->  
            </configuration>  
        </plugin>  
```

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>PowerMockTestDemo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <version>2.6.3</version>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>3.9.0</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.powermock</groupId>
            <artifactId>powermock-module-junit4</artifactId>
            <version>2.0.9</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.powermock</groupId>
            <artifactId>powermock-api-mockito2</artifactId>
            <version>2.0.9</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
            </plugin>
            <!-- JaCoCo Plugin for Offline Instrumentation -->
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.10</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>default-report</id>
                        <phase>package</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>default-instrument</id>
                        <goals>
                            <goal>instrument</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>default-restore-instrumented-classes</id>
                        <goals>
                            <goal>restore-instrumented-classes</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <!-- Surefire Plugin for Test Execution -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0-M5</version>
            </plugin>
        </plugins>
    </build>
</project>
```

