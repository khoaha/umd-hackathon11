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


  $retnumbers = array();
  
  for ($i=0; $i < count($contacts); $i++) { // I think we need count($array,1) because our arrays might be multidimensional
    for ($j=0; $j < count($numbers[$i],1); $j++) {
      $numbers[$i] = explode(",",$numbers[$i]);
      $foafs = mysql_query("SELECT * FROM Data WHERE PhoneNumber=".$numbers[$i][$j]);
      while ($row = mysql_fetch_array($foafs)) {
	$foafcont = explode(",", $row['Names']);
	$foafnumb = explode(" ",$row['Numbers']);
	for ($k = 0; $k < count($foafcont); $k++) {
	  for ($l=0; $l < count($numbers[$k],1); $l++) {
	    $foafnumb[$k] = explode(",",$foafnumb[$k]);
	    if (strtolower($_REQUEST['fullname']) == strtolower($foafcont[$k]))
	      if (!in_array($foafnumb[$k][$l],$retnumbers))
		$array_push($retnumbers, $foafnumb[$k][$l]);
	  }
	}
      }
    }
  }
  $retnumbers = array("numbers" => '"'.implode(",",$retnumbers).'"');
  print(json_encode($retnumbers));
}
mysql_close($con);

?>