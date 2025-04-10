<?php
	include('config.php');
	//error_reporting(E_ERROR);
	if($db->connect_errno > 0){
		die('Unable to connect to database [' . $db->connect_error . ']');
	}
	include('class.php');
	
	try {
		$userid = $_REQUEST['UserID'];
		$username = $_REQUEST['UserName'];
		$userpass = hash('sha256', $_REQUEST['UserPassword']);
		$userlevel = $_REQUEST['UserLevel'];

		$sql = "SELECT COUNT(*) FROM User WHERE UserID LIKE ?";
		$statement = $db->prepare($sql);
		$statement->bind_param('s', $userid);
		$statement->execute();
		$statement->bind_result($count);
		$statement->fetch();
		$statement->free_result();
		$statement->close();
	
		if ($count < 1) {			
			$sql = "INSERT INTO User (UserID, UserName, UserPassword, UserLevel) 
			VALUES (?, ?, ?, ?)";
			$statement = $db->prepare($sql);
			$statement->bind_param('ssss', $userid, $username, $userpass, $userlevel);
			$statement->execute();
			if ($statement->affected_rows>0) {
				$msg = new message(1, "User has been added.", "", "", "");
				echo json_encode($msg);
			} else {
				$msg = new message(2, "Failed to add User.", $statement->error, "", "");
				echo json_encode($msg);
			}
			$statement->close();			
		} else {
			$msg = new message(4, $userid . " already existed in database.", "", "", "");
			echo json_encode($msg);
		}
	} catch(Exception $e) {
		$msg = new message(3, $e->getMessage(), "", "", "");
		echo json_encode($msg);
	}

	$db->close();	
?>