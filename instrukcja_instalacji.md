Aby poprawnie przygotować projekt do uruchomienia w środowisku deweloperskim należy mieć zainstalowane:
- Java 21 JDK
- Maven 3.x
# Instalacja ANTLR4 (sugerowana wersja 4.13.0)
## Maven
Do pliku konfiguracyjnego <i>pom.xml</i> należy dodać zależność
```
    <dependencies>
        <dependency>
            <groupId>org.antlr</groupId>
            <artifactId>antlr4-runtime</artifactId>
            <version>4.13.0</version>
        </dependency>
    </dependencies>
```
Oraz plugin
```
            <plugin>
                <groupId>org.antlr</groupId>
                <artifactId>antlr4-maven-plugin</artifactId>
                <version>4.13.0</version>
                <executions>
                    <execution>
                        <id>antlr</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>antlr4</goal>
                        </goals>
                        <configuration>
                            <visitor>true</visitor>
                            <listener>false</listener>
                            <sourceDirectory>src/main/java/grammar</sourceDirectory>
                            <outputDirectory>gen/grammar</outputDirectory>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
```
## JAR
ANTLR4 można pobrać i dodać do zależności w formie .jar [zgodnie z oficjalną instrukcją](https://www.antlr.org/download.html)