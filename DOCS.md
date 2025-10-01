# REST Api Tester - Documentation

[Wersja Polska](#asercje)

## Table of Contents

1. [Assertions](#assertions)
2. [Variables](#variables)
3. [Responses](#responses)
4. [Creating Tests and Examples](#creating-tests)

## Assertions

### Syntax

The syntax for assertions is as follows:

`expect <logical expression>`

### Comparison Operators

- `==` - equal
- `!=` - not equal
- `>` - greater than
- `<` - less than
- `>=` - greater than or equal
- `<=` - less than or equal

### Logical Operators

The following logical operators are available:

- `&&` - and
- `||` - or
- `!` - not

### Examples

```
expect status == 200 
expect body[user][id] == "admin" 
expect headers[Content-Type] != "text/html" 
expect body[success] == true && body[message] == "OK"
```

## Variables

### Variable Types

- number

`var repeatNumber = 5`

- str

`var userName = "admin"`

- boolean

`var isEmployee = false`

- list

`var items = [item1, item2, item3]`

- object

`var user = {name: "John", surname: "Smith"}`

### Local Variables

Declaration and Initialization:

`var repeatNumber = 5`

Assignment:

`userName = "admin"`

Interpolation:

`"username": "${userName}"`

Local variables can be defined anywhere in the test and are only available within that test.

### Environment / Global Variables

Declaration and Initialization:

`ENV PASSWORD = "pass"`

Interpolation:

`"Authorization": "Bearer @{PASSWORD}"`

An environment variable can be referenced anywhere in the script.

### Accessing Structures (lists, objects)

Values from a list or object are accessed using the `[]` operator.

```
var items = [item1, item2, item3] 
var user = {name: "John", surname: "Smith"}

items[0] 
user[name]
```

## Responses

You can reference individual elements of the response using:

| Variable | Type   | Description      |
|----------|--------|------------------|
| response | JSON   | Entire response  |
| body     | JSON   | Response body    |
| headers  | JSON   | Response headers |
| status   | number | Response status  |
| type     | str    | Response type    |

These are treated as local variables and can be referenced within a test.
For this reason, they cannot be declared as local variables.

## Creating Tests
```
test <name> {
    HTTP_METHOD "/api/resource" {
    }
}
```

```
// Global environment variable
ENV PASSWORD = "securePass123"

test "User Login" options { repeat: 3, timeout: 5000 } {
    // Local variable declaration
    var username = "admin"
    var password = "securePass123"
    
    // Sending a POST request with JSON body
    POST "/api/login" {
        "headers": {
            "Content-Type": "application/json"
        },
        "body": {
            "username": "${username}",
            "password": "${password}"
        }
    }
    
    // Assertions
    expect status == 200
    expect body[success] == true
    expect body[token] != ""
}

test "Get User Details" {
    // Sending a GET request with authorization header
    GET "/api/users/42" {
        "headers": {
            "Authorization": "Bearer @{PASSWORD}"
        }
    }
    
    // Assertions
    expect status == 200
    expect body[user][id] == 42
    expect body[user][role] == "admin"
    
    // Assigning value from response to local variable
    var userName = body[user][name]
    expect userName == "John"
}

test "Update User Profile" options { timeout: 3000 } {
    // Sending a PUT request with JSON body
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
    
    // Assertions
    expect status == 200
    expect body[user][surname] == "Doe"
}
```


---

## Spis treści

1. [Asercje](#asercje)
2. [Zmienne](#zmienne)
3. [Odpowiedzi](#odpowiedzi)
4. [Tworzenie testów i przykłady](#tworzenie-testów)

## Asercje

### Składnia

Składnia asercji jest następująca:

`expect <wyrażenie logiczne>`

### Operatory porównania

- `==` - równe
- `!=` - różne
- `>` - większe
- `<` - mniejsze
- `>=` - większe lub równe
- `<=` - mniejsze lub równe

### Operatory logiczne

Dostępne są następujące operatory logiczne:

- `&&` - i
- `||` - lub
- `!` - nie

### Przykłady

```
expect status == 200
expect body[user][id] == "admin"
expect headers[Content-Type] != "text/html"
expect body[success] == true && body[message] == "OK"
```

## Zmienne

### Typy zmiennych

- number

`var repeatNumber = 5`

- str

`var userName = "admin"`

- boolean

`var isEmployee = false`

- list

`var items = [item1, item2, item3]`

- object

`var user = {name: "Jan", surname: "Kowalski"}`

### Zmienne lokalne

Deklaracja i Inicjalizacja:

`var repeatNumber = 5`

Przypisanie wartości:

`userName = "admin"`

Interpolacja:

`"username": "${userName}"`

Zmienne lokalne definiować można w dowolnym miejscu w teście i są dostępne tylko w obrębie danego testu

### Zmienne środowiskowe / globalne

Deklaracja i Inicjalizacja:

`ENV PASSWORD = "pass"`

Interpolacja:

`"Authorization": "Bearer @{PASSWORD}"`

Do zadeklarowanej zmiennej środowiskowej można odwołać się w dowolnym miejscu w skrypcie

### Dostęp do struktur (listy, obiekty)

Pobieranie wartości z listy lub obiektu odbywa się za pomocą operatora `[]`.

```
var items = [item1, item2, item3]
var user = {name: "Jan", surname: "Kowalski"}

items[0]
user[name]
```

## Odpowiedzi

Do poszczególnych elementów odpowiedzi można odwołać się za pomocą:

| Zmienna  | Typ    | Opis                |
|----------|--------|---------------------|
| response | JSON   | Cała odpowiedź      |
| body     | JSON   | Ciało odpowiedzi    |
| headers  | JSON   | Nagłówki odpowiedzi |
| status   | number | Status odpowiedzi   |
| type     | str    | Typ odpowiedzi      |

Są one traktowane jako zmienne lokalne i można się do nich odwoływać w obrębie testu.
Z tego powodu nie można ich deklarować jako zmienne lokalne.

## Tworzenie testów

```
test <nazwa> {
    METODA_HTTP "/api/resource" {
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