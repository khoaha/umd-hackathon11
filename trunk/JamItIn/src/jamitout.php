<?php
$con = mysql_connect("localhost", "root", "password123");
if (!$con) {
  die('Could not connect: ' . mysql_error());
}

mysql_select_db("HAM", $con);
$result = mysql_query("SELECT * FROM Data WHERE PhoneNumber=".$_REQUEST['idnumber']);
while($row = mysql_fetch_array($result)) {
  $numbers = explode(" ",$row['Numbers']);
  $contacts = explode(",",$row['Names']); // List of all the users friends

  for ($x=0; $x < count($numbers); $x++)
    $numbers[$x] = explode(",",$numbers[$x]);
  //print_r($numbers);
  //print_r($contacts);


  $retnumbers = array();
  
  for ($i=0; $i < count($contacts); $i++) { // I think we need count($array,1) because our arrays might be multidimensional
    for ($j=0; $j < count($numbers[$i],1); $j++) {

      $foafs = mysql_query("SELECT * FROM Data WHERE PhoneNumber=".$numbers[$i][$j]);
      while ($row = mysql_fetch_array($foafs)) {
	$foafcont = explode(",", $row['Names']);
	$foafnumb = explode(" ",$row['Numbers']);

	//for ($x=0; $x < count($numbers); $x++)
	//  $foafnumb[$l] = explode(",",$foafnumb[$l]);

	for ($k = 0; $k < count($foafcont); $k++) {
	  if (strcmp(strtolower($_REQUEST['fullname']), strtolower($foafcont[$k])) == 0) {
            $foafnumb[$k] = explode(",",$foafnumb[$k]);
            for ($l=0; $l < count($foafnumb[$k]); $l++) {
	      if (!in_array($foafnumb[$k][$l],$retnumbers))
	        array_push($retnumbers, $foafnumb[$k][$l]);
	    }
	  }
	}
      }
    }
  }
  $retnumbers = implode(",",$retnumbers);
  print($retnumbers);
}
mysql_close($con);

?>