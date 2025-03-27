# Dokumentacja

## Asercja
`expect <wyrażenie logiczne>`

`expect status == 200`
`expect json[id] == „123”`
`expect json[username] == "JanKowalski"`

## Deklarowanie zmiennych
### Zmienne
- Typy prymitywne/lokalne
  - number

  `var repeatNumber = 5`
  - str

  `var userName = ”admin”`

- boolean
  `var isEmployee = false`

- Listy

`var items = [item1, item2, item3]`

- Zmienne środowiskowe / globalne / stałe

`ENV API_URL = “myApi.com/api/v1”`
`ENV PASSWORD = “1qaz@WSX”`


Po zadeklarowaniu zmiennej środowiskowej można odwołać się do niej w dowolnym miejscu w skrypcie

`
GET '@{API_URL}/login' {
    headers: {
        "Authorization": "Bearer @{PASSWORD}"
    }
    ...
}
`

## Odpowiedź

Zgodnie z https://developer.mozilla.org/en-US/docs/Web/API/Response

- Response - json

- Response.body - json

- Response.headers -json

- Response.status - number

- Response.type


## Tworzenie testów

```test <nazwa> {
METODA_HTTP ”/api/resource” {
  }
}`

`test "Tworzenie użytkownika" {
POST "/api/users" {
  headers {
    "Content-Type": "application/json"
  }
  body {
    "username": "NowyUser",
    "email": "nowy@przyklad.com"
  }
}
expect status == 201
}
```
