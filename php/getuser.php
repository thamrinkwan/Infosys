<?php
	include('config.php');
	if($db->connect_errno > 0){
		die('Unable to connect to database [' . $db->connect_error . ']');
	}
	include('class.php');
	
	try {
		$sql = "SELECT * FROM User";
		$statement = $db->prepare($sql);
		$statement->execute();
		$result = $statement->get_result();
	
		while ($row = $result->fetch_assoc()) {
			$users[] = new User($row["UserID"], $row["UserName"], "", $row["UserLevel"], $row['UserActive']);
		}
		$statement->free_result();
		$statement->close();
		if (isset($users)) {
			foreach ($users as $value) {
				$array = (array)$value;
				$str[] = $array;
			}
			echo json_encode($str);
		} else {
			echo json_encode (json_decode ("[]"));
		}
	} catch (Exception $e) {
		$msg = new message(3, $e->getMessage(), "", "", "");
		echo json_encode($msg);		
	}
	
	$db->close();  		
?>