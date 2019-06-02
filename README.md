# J2EProject

## How to use the API

### Authorization

#### Sign up a user

Create a user to log in the application.

Call the route `localhost:5000/user/sign-up` with data of the user you want to create
```json
{
  "username": "word",
  "password": "pass"
}
```

#### Login as a user

Log in the application to be able to call any route

Call the route `localhost:5000/login` with data of the user you want to log in as.
```json
{
  "username": "word",
  "password": "pass"
}
```

The route returns a token in the headers.
`Authorization → Bearer eyJ0eXAiOiJKV1QiLCJhbGaaOiJIUzUxMiJ9.eyJzdWIiOiJ2aW5jZW50IiwiZXhwIjoxNTU4NTk4MjQ4fQ.L2WSsw9ohkEifZriylPRvD6SAGd7iaEbdBkYc46YJFilSqmgx7FELtq8tRFZlldBUnOZEMbEYj1DS1nSTfGfsA`

You need to keep it to use it in the headers of your next requests. 

On postman, you can put it in the `Authorization` section with the `Bearer token` type.
