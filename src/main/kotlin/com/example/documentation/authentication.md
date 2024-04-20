# Authentication Routes
<hr>

# User Routes




<hr>

## SignUp
**End Point = "/v1/auth/register"**
<br>
<br>
**Request** : Data Class
``` 
val username : String,
val name : String,
val email : String , 
val password : String 
```

Example Request = 
```
{
    "username" : "aarya003",
    "name" : "Aarya",
    "email" : "aarya2003",
    "password" : "aarya1223"
}
// this user is created already do not try with same credentials
```

Response = ``` "User Inserted Into Database"```

<hr>

## SignIn
**End Point = "v1/auth/login"**
<br>
<br>
**Request** : Data Class
``` 
val username : String,
val name : String,
val email : String , 
val password : String 


// this will be changed to 
val email : String,
val password : String
```


Example Request =
```
{
    "username" : "aarya003",
    "name" : "Aarya",
    "email" : "aarya2003",
    "password" : "aarya1223"
    
    // above code is for only testing purpose and will be changed to LoginRequest Data Class
    
}
```

Response = ```Data Class(token)```

<br>

Example Response = 
```
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJqd3QtYXVkaWVuY2UiLCJpc3MiOiJodHRwczovL2p3dC1wcm92aWRlci1kb21haW4vIiwiZXhwIjoxNzQyODQ4NDk0LCJ1c2VySWQiOiI2NjAwOGQ2NDlhNjNjNzU5YWZkMGI0ZmIifQ.ObUOa5yF11MudJVa7O1vEWSltH1q777_aQq3lbqEbjA"
}
```

