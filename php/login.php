<?php
	include('config.php');
	if($db->connect_errno > 0){
		die('Unable to connect to database [' . $db->connect_error . ']');
	}
	include('class.php');	

	try {
		$userid = $_REQUEST['UserID'];
		$userpass = hash('sha256', $_REQUEST['UserPassword']);
		$sql = "SELECT * FROM User 
			WHERE UserID=? AND UserPassword=? AND UserActive=1 LIMIT 1";
		$statement = $db->prepare($sql);
		$statement->bind_param('ss', $userid, $userpass);
		$statement->execute();
		$result = $statement->get_result();
	
		if ($row = $result->fetch_assoc()) {
			$statement->free_result();
			$statement->close();
			$user = new User($row["UserID"], $row["UserName"], "",
				$row["UserLevel"], $row['UserActive']);

			//update last login datetime
			$sql = "UPDATE User SET UserLastLogin=NOW() WHERE UserID=?";
			$statement = $db->prepare($sql);
			$statement->bind_param('s', $userid);
			$statement->execute();
		}
	
		if (isset($user)) {
			echo json_encode($user);
		} else {
			$user = new User("", "", "", "", 1);
			echo json_encode($user);
		}
	} catch (Exception $e) {
		$msg = new message(3, $e->getMessage(), "", "", "");
		echo json_encode($msg);
	}	
	$db->close();	
?>