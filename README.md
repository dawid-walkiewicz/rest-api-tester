Example of a simple test suite.

```
test "Pobranie użytkownika" options { repeat: 5, timeout: 2000 } {
    GET "/api/users/123" {
        headers {
            "Authorization": "Bearer abcdef123456"
        }
    }
    expect status 200
    expect json "$.id" == 123
    expect json "$.username" == "JanKowalski"
}

test "Tworzenie użytkownika" {
    POST "/api/users" {
        headers {
            "Content-Type": "application/json"
        }
        body {
            "username": "NowyUser",
            "email": "nowy@przyklad.com"
        }
    }
    expect status 201
}
```