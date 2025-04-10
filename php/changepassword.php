<?php	
	include('config.php');
	if($db->connect_errno > 0){
		die('Unable to connect to database [' . $db->connect_error . ']');
	}
	include('class.php');
	
	try {
		if (isset($_REQUEST['UserID']) && isset($_REQUEST['UserPassword']) && isset($_REQUEST['UserNewPassword'])) {
			$userid = $_REQUEST['UserID'];		
			$userpass = hash('sha256', $_REQUEST['UserPassword']);
			$usernewpass = hash('sha256', $_REQUEST['UserNewPassword']);		
		
			$sql = "SELECT COUNT(*) FROM User WHERE UserID=? AND UserPassword=?";
			$statement = $db->prepare($sql);
			$statement->bind_param('ss', $userid, $userpass);
			$statement->execute();
			$statement->bind_result($count);
			$statement->fetch();
			$statement->free_result();
			$statement->close();

			if ($count > 0) {
				$sql = "UPDATE User SET UserPassword=? WHERE UserID=?";
				$statement = $db->prepare($sql);
				$statement->bind_param('ss', $usernewpass, $userid);			
				$statement->execute();
				if ($statement->affected_rows>=0) {
					$msg = new message(1, "Password has been updated.", "", "", "");
					echo json_encode($msg);					
				} else {
					$msg = new message(2, "Failed to update Password.", "", "", "");
					echo json_encode($msg);
				}
				$statement->close();
			} else {
				$msg = new message(4, "Wrong Password.", "", "", "");
				echo json_encode($msg);
			}
		}					
	} catch(Exception $e) {
		$msg = new message(3, $e->getMessage(), "", "", "");
		echo json_encode($msg);
	}
				
	$db->close();	
?>