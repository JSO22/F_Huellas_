<?php
include('functions.php');
$idTarjeta = $_GET['txtIdTarjeta'];
$array = array();	

if($resultset=getSQLResultSet("SELECT count(*) as LIKES FROM `likes` WHERE `likidtarjeta`='".$idTarjeta."'")){ 
	
     $row = $resultset->fetch_array(MYSQLI_NUM);
     echo json_encode($array);
}
?>