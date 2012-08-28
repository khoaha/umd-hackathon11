<?php
$con = mysql_connect("localhost", "root", "password123");
if (!$con) {
  die('Could not connect: ' . mysql_error());
}
mysql_select_db("HAM", $con);

$result = mysql_query("SELECT * FROM Data WHERE PhoneNumber="."5");
while($row = mysql_fetch_array($result)) {
  echo $row['PhoneNumber'] . " " . $row['Name'] . " " . $row['Contacts'];
  //echo "<br />";
  
}

mysql_close($con);
?>