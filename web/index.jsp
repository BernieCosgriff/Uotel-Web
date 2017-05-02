<%@ page language="java" import="cs5530.*" %>
<html>
<head>
    <title>Login</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/js/materialize.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/css/materialize.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
    <div class="row" style="text-align: center; vertical-align: middle;">
        <h1>Welcome to Uotel!</h1>
        <form class="col s6 push-s3" name="user_login" method=post action="postlogin.jsp">
            <label for="login">Login</label> <br>
            <input type=text id="login" name="login" length=10/> <br> <br>
            <label for="password">Password</label> <br>
            <input type=text id="password" name="password" length=10/> <br> <br>
            <button type="submit" class="btn red darken-4">Log In</button>
        </form>
    </div>
    <div class="row">
        <form class="center-align" action="newuser.jsp">
            <button type="submit" class="btn red darken-4">Create Account</button>
        </form>
    </div>
</div>
</body>
</html>
