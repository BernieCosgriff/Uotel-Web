<%@ page language="java" import="cs5530.*, java.util.*" %>
<html>
<head>
    <title>Add Reservation</title>
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
          '<tr><td>' + order.propertyId +
	  '</td><td>' + order.startDate +
	  '</td><td>' + order.endDate +
	  '</td><td>' + order.guests + '</td></tr>'
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
          var url = 'postreservation.jsp?propertyId=' + e.propertyId +
            '&startDate=' + e.startDate +
            '&endDate=' + e.endDate +
            '&guests=' + e.guests;
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
    <div id="create-container" class="col-md-6">
      <h1>Add Reservation</h1>
      <form id="add-form" class="row" name="add_reservation">
        <div class="input-field">
          <label>Property ID</label> <br>
          <input type="number" name="propertyId" length=10/> <br> <br>
        </div>
        <div class="input-field">
          <label>Start Date (MM-dd-yyyy)</label> <br>
          <input type="text" name="startDate" length=10/> <br> <br>
        </div>
        <div class="input-field">
          <label>End Date (MM-dd-yyyy)</label> <br>
          <input type="text" name="endDate" length=10/> <br> <br>
        </div>
        <div class="input-field">
          <label>Guests</label> <br>
          <input type="number" name="guests" length=10/> <br> <br>
        </div>
	<button type="button" id="add-button" class="btn red darken-4" onclick="onAdd()">Add to Cart</button>
	<button type="button" id="review-button" class="btn red darken-4" onclick="onReview()" style="display: none;">Review Cart</button>
      </form>
    </div>
    <div id="pending-container" class="col-md-6" style="display: none;">
      <h1>Pending Reservations</h1>
      <table id="pending-table" class="striped">
	<thead>
	  <tr>
	    <th>Property ID</th>
	    <th>Start Date</th>
	    <th>End Date</th>
	    <th>Guests</th>
	  </tr>
	</thead>
	<tbody>
	</tbody>
      </table>
      <button id="submit-button" class="btn red darken-4" onclick="onSubmit()">Submit</button>
      <button id="reset-button" class="btn red darken-4" onclick="onReset()">Reset</button>
    </div>
    <div id="results-container" class="col-md-6" style="display: none;">
      <h1>Results</h1>
      <table id="results-table" class="striped">
	<thead>
	  <th>Property ID</th>
	  <th>Start Date</th>
	  <th>End Date</th>
	  <th>Guests</th>
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
