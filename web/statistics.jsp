<%@ page language="java" import="cs5530.*" %>
<html>
<head>
    <title>Statistics</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/js/materialize.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/css/materialize.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
    <script>
        $(document).ready(function () {
            $('select').material_select();
        });

        function getProperties() {
            var option = $('#optionSelect').val();
            var numProps = $('#numInput').val();
            $.ajax({
                url: 'displaystatproperties.jsp?option=' + option + '&numProps=' + numProps,
                type: 'GET',
                data: 'html',
                success: function (data) {
                    console.log('Working');
                    $('#ajaxDiv').html(data).hide();
                    $('#ajaxDiv').fadeIn(500);
                }
            });
        }
    </script>
</head>
<body>
<%@include file="header.jsp"%>
<div class="container">
    <h1 class="center-align">Statistics</h1>
    <div class="row section">
        <div class="col s2 input-field">
            <select id="optionSelect">
                <option value="popular">Most Popular</option>
                <option value="expensive">Most Expensive</option>
                <option value="rating">Highest Rated</option>
            </select>
            <label for="optionSelect">View</label>
        </div>
        <div class="input-field col s8">
            <input type="number" id="numInput">
            <label for="numInput">Number of Properties You Would Like to See per Category</label>
        </div>
        <div style="margin-top: 20px" class="col s2">
            <button class="btn red darken-4" onclick="getProperties()">Submit</button>
        </div>
    </div>
    <div hidden class="row section" id="ajaxDiv">

    </div>
</div>
</body>
</html>
