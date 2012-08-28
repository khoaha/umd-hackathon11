<?php
//J-J-JAM IT IN
$con = mysql_connect("localhost", "root", "password123");
if (!$con) {
  die('Could not connect: ' . mysql_error());
}
mysql_select_db("HAM", $con);

mysql_query("DELETE FROM Data WHERE PhoneNumber=6");
mysql_query("INSERT INTO Data VALUES (6, '".mysql_real_escape_string($_REQUEST['numbers'])."', '".mysql_real_escape_string($_REQUEST['fullnames'])."')");

mysql_close();
?>