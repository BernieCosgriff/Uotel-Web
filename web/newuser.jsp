<%@ page language="java" import="cs5530.*" %>
<html>
<head>
    <title>New User</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/js/materialize.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/css/materialize.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
    <div class="row">
        <h1 class="center-align">New User</h1>
        <form name="new_user" method=post action="postnewuser.jsp">
            <div class="col-xs-6">
                <div class="input-field">
                    <label for="firstName">First Name</label>
                    <input id="firstName" type=text name="firstName" length=10/>
                </div>
                <div class="input-field">
                    <label for="lastName">Last Name</label>
                    <input id="lastName" type=text name="lastName" length=10/>
                </div>
                <div class="input-field">
                    <label for="login">Login</label>
                    <input  id="login"type=text name="login" length=10/>
                </div>
                <div class="input-field">
                    <label for="password">Password</label>
                    <input id="password" type=text name="password" length=10/>
                </div>
                <div class="input-field">
                    <label for="phone">Phone Number</label>
                    <input id="phone" type=text name="phoneNumber" length=10/>
                </div>
            </div>
            <div class="col-xs-6">
                <div class="input-field">
                    <label for="unit">House/Unit Number</label>
                    <input id="unit" type=text name="unitNumber" length=10/>
                </div>
                <div class="input-field">
                    <label for="city">City</label>
                    <input id="city" type=text name="city" length=10/>
                </div>
                <div class="input-field">
                    <label for="state">State</label>
                    <input id="state" type=text name="state" length=10/>
                </div>
                <div class="input-field">
                    <label for="country">Country</label>
                    <input id="country" type=text name="country" length=10/>
                </div>
                <div class="input-field">
                    <label for="zip">Zip Code</label>
                    <input id="zip" type=text name="zipcode" length=10/>
                </div>
            </div>
            <div class="input-field col s12">
                <label for="street">Street Name</label>
                <input id="street" type=text name="street" length=10/>
            </div>
            <div class="col s12">
                <div class="center-align">
                    <input type="submit" class="btn red darken-4 center-align" value="Register"/>
                    <a class="btn red darken-4 center-align" href="index.jsp">Cancel</a>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
