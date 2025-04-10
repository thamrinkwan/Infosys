<?php
	include('config.php');
	if($db->connect_errno > 0){
		die('Unable to connect to database [' . $db->connect_error . ']');
	}
	include('class.php');
	
	try {
		$userid = $_REQUEST['UserID'];
		$sql = "DELETE FROM User WHERE UserID=?";
		$statement = $db->prepare($sql);
		$statement->bind_param('s', $userid);
		$statement->execute();
		if ($statement->affected_rows>0) {
			$msg = new message(1, "User has been deleted.", "", "", "");
			echo json_encode($msg);
		} else {
			$msg = new message(2, "No changes made to User.", $statement->error, "", "");
			echo json_encode($msg);
		}
		$statement->close();
	} catch(Exception $e) {
		$msg = new message(3, $e->getMessage(), "", "", "");
		echo json_encode($msg);
	}
			  				
	$db->close();	
?>