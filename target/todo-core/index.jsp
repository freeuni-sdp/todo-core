<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>todo</title>
  <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
  <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
 
  <style>
  #feedback { font-size: 1.4em; }
  #selectable .ui-selecting { background: #FECA40; }
  #selectable .ui-selected { background: #F39814; color: white; }
  #selectable { list-style-type: none; margin: 0; padding: 0; width: 60%; }
  #selectable li { margin: 3px; padding: 0.4em; font-size: 1.4em; height: 18px; }
  </style>
  
  <script>
  $(function() {
	  $( "#selectable" ).selectable();
	  
	  var todocoreapi = "webapi/todos/";
	  $.getJSON(todocoreapi)
	    .done(function( data ) {
	      $.each( data.items, function( i, item ) {
	        $( "<li class='ui-widget-content'>")
	        	.attr("id", item.id)
	        	.text(item.text)
	        	.appendTo( "#selectable" );
	      });
	    });
	})();
  
  </script>
</head>
<body>

<input type="text"> 
<ol id="selectable">
</ol>
 
 
</body>
</html>