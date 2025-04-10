<?php
    class Message
	{
		public $ID;
		public $Message;
		public $Extra1, $Extra2, $Extra3;
			
		public function __construct($id, $message, $extra1, $extra2, $extra3)
		{
			$this->ID = $id;
			$this->Message = $message;
			$this->Extra1 = $extra1;
			$this->Extra2 = $extra2;
			$this->Extra3 = $extra3;
		}
    }
    
    class User
	{
		public $UserID;
		public $UserName;
		public $UserPassword;
		public $UserLevel;
		public $UserActive;
			
		public function __construct($id, $name, $password, $level, $active)
		{
			$this->UserID = $id;
			$this->UserName = $name;
			$this->UserPassword = $password;
			$this->UserLevel = $level;
			$this->UserActive = $active;
		}
	}
?>