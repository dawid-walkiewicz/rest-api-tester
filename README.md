# REST Api Tester
___
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

___
## Asercja
Składnia asercji jest następująca:

`expect <wyrażenie logiczne>`

Dostępne są następujące operatory porównania:
- `==` - równe
- `!=` - różne
- `>` - większe
- `<` - mniejsze
- `>=` - większe lub równe
- `<=` - mniejsze lub równe

Dostępne są następujące operatory logiczne:
- `&&` - i
- `||` - lub
- `!` - nie

Przykłady asercji:
```
expect status == 200
expect body[user][id] == „admin”
expect headers[Content-Type] != "text/html"
expect body[success] == true && body[message] == "OK"
```
___
## Deklarowanie zmiennych
### Typy
- number

`var repeatNumber = 5`

- str

`var userName = ”admin”`

- boolean

`var isEmployee = false`

- list

`var items = [item1, item2, item3]`

- object

`var user = {name: "Jan", surname: "Kowalski"}`

### Zmienne
- Zmienne lokalne
    - Deklaracja i Inicjalizacja

  `var repeatNumber = 5`

    - Przypisanie wartości

  `userName = ”admin”`

    - Interpolacja

  `"username": "${userName}"`

    - Zmienne lokalne definiować można w dowolnym miejscu w teście i są dostępne tylko w obrębie danego testu

- Zmienne środowiskowe / globalne

    - Deklaracja i Inicjalizacja

  `ENV PASSWORD = “pass”`

    - Interpolacja

  `"Authorization": "Bearer @{PASSWORD}"`

    - Do zadeklarowanej zmiennej środowiskowej można odwołać się w dowolnym miejscu w skrypcie

### Pobieranie wartości listy lub obiektu
Pobieranie wartości z listy lub obiektu odbywa się za pomocą operatora "[]".

```
var items = [item1, item2, item3]
var user = {name: "Jan", surname: "Kowalski"}

items[0]
user[name]
```
___
## Odpowiedź

Do poszczególnych elementów odpowiedzi można odwołać się za pomocą:

- response (typ json) - cała odpowiedź

- body (typ json) - ciało odpowiedzi

- headers (typ json) - nagłówki odpowiedzi

- status (typ number) - status odpowiedzi

- type (typ str) - typ odpowiedzi

Są one traktowane jako zmienne lokalne i można się do nich odwoływać w obrębie testu.
Z tego powodu nie można ich deklarować jako zmienne lokalne.
___
## Tworzenie testów

```test <nazwa> {
METODA_HTTP ”/api/resource” {
  }
}
```

```
// Globalna zmienna środowiskowa
ENV PASSWORD = "securePass123"

test "User Login" options { repeat: 3, timeout: 5000 } {
    // Deklaracja zmiennych lokalnych
    var username = "admin"
    var password = "securePass123"
    
    // Wysłanie żądania POST z ciałem JSON
    POST "/api/login" {
        "headers": {
            "Content-Type": "application/json"
        },
        "body": {
            "username": "${username}",
            "password": "${password}"
        }
    }
    
    // Asercje
    expect status == 200
    expect body[success] == true
    expect body[token] != ""
}

test "Get User Details" {
    // Wysłanie żądania GET z nagłówkiem autoryzacyjnym
    GET "/api/users/42" {
        "headers": {
            "Authorization": "Bearer @{PASSWORD}"
        }
    }
    
    // Asercje
    expect status == 200
    expect body[user][id] == 42
    expect body[user][role] == "admin"
    
    // Przypisanie wartości z odpowiedzi do zmiennej lokalnej
    var userName = body[user][name]
    expect userName == "John"
}

test "Update User Profile" options { timeout: 3000 } {
    // Wysłanie żądania PUT z ciałem JSON
    PUT "/api/users/42" {
        "headers": {
            "Content-Type": "application/json",
            "Authorization": "Bearer @{PASSWORD}"
        },
        "body": {
            "name": "John",
            "surname": "Doe"
        }
    }
    
    // Asercje
    expect status == 200
    expect body[user][surname] == "Doe"
}
```
