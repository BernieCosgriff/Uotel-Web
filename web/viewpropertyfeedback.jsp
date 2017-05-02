<%@ page language="java" import="cs5530.*" %>
<%@ page import="uotel.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<html>
<head>
    <title>Property Feedback</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/js/materialize.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/css/materialize.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
    <script>
        function allFeedbacks() {
            var pid = $('#pid').val();
            $.ajax({
                url: 'displaypropertyfeedback.jsp?pid=' + pid + '&numProps=0',
                type: 'GET',
                data: 'html',
                success: function (data) {
                    console.log('Working');
                    $('#ajaxDiv').html(data).hide();
                    $('#ajaxDiv').fadeIn(500);
                    $('#feedbackDiv').fadeIn(500);
                }
            });
        }

        function topFeedbacks() {
            var pid = $('#pid').val();
            var numProps = $('#numProps').val();
            $.ajax({
                url: 'displaypropertyfeedback.jsp?pid=' + pid + '&numProps=' + numProps,
                type: 'GET',
                data: 'html',
                success: function (data) {
                    console.log('Working');
                    $('#ajaxDiv').html(data).hide();
                    $('#ajaxDiv').fadeIn(500);
                    $('#feedbackDiv').fadeIn(500);
                }
            });
        }

        function showPropertyIdInput() {
            $('#feedbackDiv').hide();
            $('#numPropsDiv').hide(200);
            $('#pid').val("");
            $('#ajaxDiv').hide();
            $('#topBtn').hide();
            $('#propIdDiv').fadeIn(500);
            $('#allBtn').fadeIn(500);
        }

        function showBothInputs() {
            $('#feedbackDiv').hide();
            $('#allBtn').hide();
            $('#pid').val("");
            $('#numProps').val("");
            $('#ajaxDiv').hide();
            $('#propIdDiv').fadeIn(200);
            $('#numPropsDiv').fadeIn(500);
            $('#topBtn').fadeIn(500);
        }
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
<div class="container">
    <%@include file="header.jsp" %>
    <div class="section">
    <div class="row">
        <div class="col s6">
            <button style="width: 100%;" onclick="showPropertyIdInput()" class="btn">View all Feedbacks for a Property
            </button>
        </div>
        <div class="col s6">
            <button style="width: 100%;" onclick="showBothInputs()" class="btn">View the Most Useful Feedbacks for a
                property
            </button>
        </div>
    </div>

    <div id="propIdDiv" hidden class="input-field">
        <input id="pid" type="number">
        <label for="pid">Property ID</label>
    </div>
    <div id="numPropsDiv" hidden class="input-field">
        <input id="numProps" type="number">
        <label for="numProps">Maximum Number of Properties to Display</label>
    </div>
    <div hidden id="allBtn">
        <button class="btn red darken-4" onclick="allFeedbacks()">Submit</button>
    </div>
    <div hidden id="topBtn">
        <button class="btn red darken-4" onclick="topFeedbacks()">Submit</button>
    </div>
    </div>
    <div hidden id="feedbackDiv" class="row section">
        <h2>Rate a Feedback</h2>
        <form class="col s12" action="postfeedbackrating.jsp">
            <div class="row">
                <div id="feedbackIdDiv" class="input-field col s6">
                    <input id="feedId" name="fid" type="number" required>
                    <label for="feedId">Feedback ID</label>
                </div>
                <div id="ratingDiv" class="input-field col s6">
                    <input placeholder="0, 1, or 2" id="rating" name="fRating" type="number" min="0" max="2" required>
                    <label for="rating">Usefulness Rating</label>
                </div>
            </div>
            <div id="feedbackBtnDiv">
                <button class="btn red darken-4" type="submit">Submit Feedback</button>
            </div>
        </form>
    </div>
    <div id="ajaxDiv">

    </div>
</div>
</body>
</html>
