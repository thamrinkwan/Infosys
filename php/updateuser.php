<?php
	include('config.php');
	if($db->connect_errno > 0){
		die('Unable to connect to database [' . $db->connect_error . ']');
	}
	include('class.php');

	try {
		$userid = $_REQUEST['UserID'];
		$username = $_REQUEST['UserName'];
		if ($_REQUEST['UserPassword'] != "")
			$userpass = hash('sha256', $_REQUEST['UserPassword']);
		else
			$userpass = "";
		$userlevel = $_REQUEST['UserLevel'];
		if ($user != $userid) 
			$update = "UserToken='', ";
		else
			$update = "";
		$useractive = $_REQUEST['UserActive'];

		if ($userpass != "") {
			$sql = "UPDATE User SET UserName=?, UserPassword=?, UserLevel=?, UserActive=? WHERE UserID=?";
			$statement = $db->prepare($sql);
			$statement->bind_param('sssss', $username, $userpass, $userlevel, $useractive, $userid);
		} else {
			$sql = "UPDATE User SET UserName=?, UserLevel=?, UserActive=? WHERE UserID=?";
			$statement = $db->prepare($sql);
			$statement->bind_param('ssss', $username, $userlevel, $useractive, $userid);
		}
		
		$statement->execute();
		if ($statement->affected_rows>0) {
			$msg = new message(1, "User has been updated.", "", "", "");
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