<%@ page language="java" import="cs5530.*, java.util.*, java.sql.*" %>
<html>
<head>
    <title>Add Stay</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/js/materialize.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.1/css/materialize.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
    <script>
      var cart = [];

      function onAdd() {
        var $inputs = $('#add-form :input');
        var order = {};
        $inputs.each(function() {
          order[this.name] = $(this).val();
        });
        cart.push(order);
        $('#pending-table tbody').append(
          '<tr><td>' + order.reservationId +
	  '</td><td>' + order.totalCost + '</td></tr>'
        );
        $(':input', '#add-form')
          .not(':button, :submit, :reset, :hidden')
          .val('');
        $('#review-button').show();
      }

      function onReview() {
        if (cart.length === 0) return;
        $('#create-container').hide();
        $('#pending-container').show();
      }

      function onSubmit() {
        cart.forEach(e => {
          var url ='poststay.jsp?reservationId=' + e.reservationId +
            '&totalCost=' + e.totalCost;
          $.ajax({
            url: url,
            type: 'GET',
            data: 'html',
            success: function(data) {
              $('#results-table tbody').append(data);
            },
            error: function(req, status) {
              console.log('ERROR ' + status);
            }
          });
        });
        $('#pending-container').hide();
        $('#results-container').show();
      }

      function onReset() {
        cart = [];
        $('#pending-table tbody > tr').remove();
        $('#pending-container').hide();
        $('#create-container').show();
        $('#review-button').hide();
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
    <div id="reservation-container" class="col-md-6">
      <h3>Your Reservations</h3>
    <%
        int userId = (int) session.getAttribute("userId");
        DBConn conn = (DBConn) session.getAttribute("conn");
       List<Reservation> reservations;
        try {
            reservations = conn.getReservations(userId);
        } catch (SQLException e) {
           response.sendRedirect("dberror.jsp");
           return;
        }
    %>
    <table id="reservation-table" class="striped">
        <thead>
        <tr>
            <th>Reservation ID</th>
            <th>Property Name</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Number of Guests</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Reservation r : reservations) {
        %>
        <tr>
            <td><%=r.id%></td>
            <td><%=r.property.name%></td>
            <td><%=r.startDate%></td>
            <td><%=r.endDate%></td>
            <td><%=r.guests%></td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
    </div>
    <div id="create-container" class="col-md-6">
      <h3>Record Stay</h3>
      <form id="add-form" class="row" name="add_stay">
        <div class="input-field">
          <label>Reservation ID</label> <br>
          <input type="number" name="reservationId" length=10/> <br> <br>
        </div>
        <div class="input-field">
          <label>Total Cost (xx.xx)</label> <br>
          <input type="number" name="totalCost" length=10/> <br> <br>
        </div>
	<button type="button" id="add-button" class="btn red darken-4" onclick="onAdd()">Add to Cart</button>
	<button type="button" id="review-button" class="btn red darken-4" onclick="onReview()" style="display: none;">Review Cart</button>
      </form>
    </div>
    <div id="pending-container" class="col-md-6" style="display: none;">
      <h3>Pending Stays</h3>
      <table id="pending-table" class="striped">
	<thead>
	  <tr>
	    <th>Reservation ID</th>
	    <th>Total Cost</th>
	  </tr>
	</thead>
	<tbody>
	</tbody>
      </table>
      <button id="submit-button" class="btn red darken-4" onclick="onSubmit()">Submit</button>
      <button id="reset-button" class="btn red darken-4" onclick="onReset()">Reset</button>
    </div>
    <div id="results-container" class="col-md-6" style="display: none;">
      <h3>Results</h3>
      <table id="results-table" class="striped">
	<thead>
	  <th>Reservation ID</th>
	  <th>Total Cost</th>
	  <th>Result</th>
	</thead>
	<tbody>
	</tbody>
      </table>
      <a class="btn red darken-4" href="mainmenu.jsp">Main Menu</a>
    </div>
</div>
</body>
</html>
