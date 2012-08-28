<?php
$con = mysql_connect("localhost", "root", "password123");
if (!$con) {
  die('Could not connect: ' . mysql_error());
}

mysql_select_db("HAM", $con);
// gets person requesting
$result = mysql_query("SELECT * FROM Data WHERE PhoneNumber=".$_REQUEST['idnumber']);
while($row = mysql_fetch_array($result)) {
	// gets contacts' numbers
	$numbers = explode(" ",$row['Numbers']);
	
	$output = array();

	$m = explode(",", $numbers); //separate numbers
	for ($m as $number)
		$q = mysql_query("SELECT * FROM Data WHERE PhoneNumber=".$m); // gets friend row with number
		while($r = mysql_fetch_array($q)) {
			$names = explode(",",$r['Names']); //get friend's names list
			$targets = explode(" ",$r['Numbers']); //get friend's names' corresponding number lists
			for ($i=0; $i<count($names); $i++) {
				if strcmp(tolower($names[$i]), tolower($_REQUEST['fullname'])) { //checks if current name matches input
					array_push($output, array('numbers' => $targets[$i])); //TODO: add all numbers
				}
			}
		}
}
  
}
print(json_encode($output));

mysql_close($con);

?>