# REST Api Tester
___
[Wersja Polska](#instalacja)

---

## Description
REST Api Tester is a simple domain-specific language for defining and running tests of REST endpoints. 
Test definitions can include various types of HTTP requests, assertions, as well as local and global variables. Additionally, it is possible to specify parameters such as maximum timeout or the number of test repetitions.

## Installation
To compile and run the application, Java 21 and Maven 3.x are required.
Creating the executable .jar file:
```
mvn clean install
```
Running the application: `java -jar <path to .jar file> <test.txt>` 
For example:
```
java -jar .\target\rest-api-tester-1.0-0.jar .\test_http.txt
```

## Documentation
The language documentation can be found [here](DOCS.md).

---

## Opis
REST Api Tester to prosty język dziedzinowydefiniowanie i uruchamianie testów endpointów REST.
Definicje testów mogą zawierać różne typy żądań HTTP, asercje oraz zmienne lokalne i globalne. Dodatkowo możliwe jest określenie takich parametrów jak maksymalny czas oczekiwania czy liczba powtórzeń testu.

## Instalacja

Do kompilacji i uruchomienia aplikacji wymagana jest Java 21 oraz Maven 3.x

Tworzenie pliku wykonawczego .jar:
```
mvn clean install
```
Uruchomienie aplikacji `java -jar <ścieżka pliku .jar> <test.txt>`
Na przykład:
```
java -jar .\target\rest-api-tester-1.0-0.jar .\test_http.txt
```

## Dokumentacja
Dokumentacja języka znajduje się [tutaj](DOCS.md).
