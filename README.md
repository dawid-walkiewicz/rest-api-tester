Example of a simple test.

```
test "User Login" options { repeat: 3, timeout: 5000 } {
    var username = "admin"
    var password = "securePass123"
    
    POST "/api/login" {
        "headers": {
            "Content-Type": "application/json"
        },
        "body": {
            "username": "${username}",
            "password": "${password}"
        }
    }
    
    expect status == 200
    expect body[success] == true
    expect body[token] != ""
}
```