<?php
$con = mysql_connect("localhost", "root", "password123");
if (!$con) {
  die('Could not connect: ' . mysql_error());
}
print($_REQUEST['fullname']);
print($_REQUEST['number']);

mysql_select_db("HAM", $con);
$result = mysql_query("SELECT * FROM Data WHERE PhoneNumber="."5");
while($row = mysql_fetch_array($result)) {
  echo $row['PhoneNumber'] . " " . $row['Numbers'] . " " . $row['Names'];
  //echo "<br />";
  $contacts = explode(",",$row['Names']); // List of all the users friends
  $numbers = explode(" ",$row['Numbers']);
  
  for ($i=0; $i<=count($contacts,1); $i++) { // I think we need count($array,1) because our arrays might be multidimensional
    if ($_REQUEST['searchname'] == $contacts[$i]) {
      $temp = explode(",",$numbers[$i]);
      foreach ($temp as $x) {
	print($x);
      }
    }
  }
}
mysql_close($con);

?>