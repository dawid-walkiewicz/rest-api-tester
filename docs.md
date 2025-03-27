# Dokumentacja

## Asercja
`expect <oczekiwany wynik> <otrzymany wynik>`

//przy testowaniu response body

`exist <zmienna> <nazwa oczekiwanego pola w zmiennej>`

## Tworzenie testów
`test <nazwa> `

## Deklarowanie zmiennych
### Zmienne
- Typy prymitywne
  - int
  
  `var repeatNumber = 5`
  - str
  
  `var userName = 'admin'`

- Listy

`var items = [item1, item2, item3]` 

- Zmienne środowiskowe / globalne

`ENV password = '1234'`


Po zadeklarowaniu zmiennej środowiskowej można odwołać się do niej w dowolnym miejscu w skrypcie

`
GET '/api/login' {
    headers: {
        "Authorization": "Bearer @{password}"
    }
    ...
}
`