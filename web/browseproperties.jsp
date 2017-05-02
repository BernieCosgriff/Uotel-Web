<%@ page language="java" import="cs5530.*" %>
<html>
<head>
    <title>Browse Properties</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/js/materialize.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/css/materialize.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
    <script>
        function optionBoxChecked(id) {
            var box = $('#' + id);
            if (id == 'price') {
                if (box.is(':checked')) {
                    $('#priceDiv').fadeIn(500)
                } else {
                    $('#priceDiv').fadeOut(200);
                    $('#priceDiv :input').each(function () {
                        $(this).val("");
                    });
                }
            } else if (id == 'address') {
                if (box.is(':checked')) {
                    $('#addressOptionDiv').fadeIn(500)
                } else {
                    $('#addressOptionDiv').fadeOut(200);
                    $('#addressOptionDiv > div > input').each(function () {
                        $(this).val("");
                    });
                }
            } else if (id == 'keyword') {
                if (box.is(':checked')) {
                    $('#keywordDiv').fadeIn(500)
                } else {
                    $('#keywordDiv').fadeOut(200);
                    $('#keywordDiv :input').each(function () {
                        $(this).val("");
                    });
                }
            } else if (id == 'category') {
                if (box.is(':checked')) {
                    $('#categoryDiv').fadeIn(500)
                } else {
                    $('#categoryDiv').fadeOut(200);
                    $('#categoryDiv :input').each(function () {
                        $(this).val("");
                    });
                }
            }
        }

        function getProperties() {
            var lowPrice = $('#lowPriceInput').val();
            var highPrice = $('#highPriceInput').val();
            var city = $('#cityInput').val();
            var state = $('#stateInput').val();
            var keyword = $('#keywordInput').val();
            var category = $('#categoryInput').val();
            var sortBy = $('#sortOption').val();
            var order = $('#sortDirection').val();
            var limit = $('#limitSelect').val();
            $.ajax({
                url: 'displayproperties.jsp?low=' + lowPrice + '&high=' + highPrice +
                '&city=' + city + '&state=' + state + '&keyword=' + keyword + '&category=' + category
                + '&sortBy=' + sortBy + '&order=' + order + '&limit=' + limit,
                type: 'GET',
                data: 'html',
                success: function (data) {
                    $('#mainDiv').hide();
                    $('#mainDiv').html(data);
                    $('#mainDiv').show();
                }
            });
        }

        $(document).ready(function () {
            $('select').material_select();
            $("input:radio[name=addressOption]").click(function () {
                var value = $(this).val();
                if (value == 'state') {
                    $('#cityInputDiv').hide();
                    $('#stateInputDiv').show();
                } else if (value == 'city') {
                    $('#stateInputDiv').hide();
                    $('#cityInputDiv').show();
                }
            });
        });
    </script>
</head>
<body>
<%
    if (session.getAttribute("userId") == null ||
            session.getAttribute("conn") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<div class="container" id="mainDiv">
    <%@include file="header.jsp" %>
    <h2 class="center-align">What Would You Like to Search By?</h2>
    <div class="divider"></div>
    <div class="row section center-align">
        <div class="col s3 input-field">
            <input class="with-gap" type="checkbox" id="price" onclick="optionBoxChecked(id)">
            <label for="price">Price</label>
        </div>
        <div class="col s3 input-field">
            <input type="checkbox" id="address" onclick="optionBoxChecked(id)">
            <label for="address">Address</label>
        </div>
        <div class="col s3 input-field">
            <input type="checkbox" id="keyword" onclick="optionBoxChecked(id)">
            <label for="keyword">Keyword</label>
        </div>
        <div class="col s3 input-field">
            <input type="checkbox" id="category" onclick="optionBoxChecked(id)">
            <label for="category">Category</label>
        </div>
    </div>
    <div hidden class="row section" id="priceDiv">
        <div class="col s6 input-field">
            <input type="number" id="lowPriceInput">
            <label for="lowPriceInput">Low Price Per Night</label>
        </div>
        <div class="col s6 input-field">
            <input type="number" id="highPriceInput">
            <label for="highPriceInput">High Price Per Night</label>
        </div>
    </div>
    <div hidden id="addressOptionDiv">
        <div class="row section">
            <h2 class="center-align input-field">What Would You Like to Search By?</h2>
            <div class="col s6 input-field center-align">
                <input class="with-gap" type="radio" value="city" name="addressOption" id="cityOption">
                <label for="cityOption">City</label>
            </div>
            <div class="col s6 input-field center-align">
                <input class="with-gap" type="radio" value="state" name="addressOption" id="stateOption">
                <label for="stateOption">State</label>
            </div>
        </div>
        <div hidden class="row section input-field" id="cityInputDiv">
            <input type="text" id="cityInput">
            <label for="cityInput">City</label>
        </div>
        <div hidden class="row section input-field" id="stateInputDiv">
            <input type="text" id="stateInput">
            <label for="stateInput">State</label>
        </div>
    </div>
    <div hidden class="row section input-field" id="keywordDiv">
        <input type="text" id="keywordInput" placeholder="Enter Keywords Seperate by Spaces">
        <label for="keywordInput">Keyword</label>
    </div>
    <div hidden class="row section input-field" id="categoryDiv">
        <input type="text" id="categoryInput">
        <label for="categoryInput">Category</label>
    </div>
    <div id="sortByDiv" class="row section">
        <h2 class="center-align">How Would You Like to Sort the Results?</h2>
        <div class="col s4 input-field">
            <select id="sortOption">
                <option value="Price">Price</option>
                <option value="Feedback">Property Feedback</option>
                <option value="TrustedFeedback">Trusted User Feedback</option>
            </select>
            <label for="sortOption">Sort By</label>
        </div>
        <div class="col s4 input-field">
            <select id="sortDirection">
                <option selected value="asc">Ascending</option>
                <option value="desc">Descending</option>
            </select>
            <label for="sortDirection">Order</label>
        </div>
        <div class="col s4 input-field">
            <select id="limitSelect">
                <option selected value="0">No Limit</option>
                <%for (int i = 1; i < 21; i++) {%>
                <option value="<%=i%>"><%=i%>
                </option>
                <%}%>
            </select>
            <label for="limitSelect">Limit</label>
        </div>
    </div>
    <div class="center-align">
        <button class="col s3 btn red darken-4" onclick="getProperties()">Submit</button>
    </div>
</div>
</body>
</html>
