<?php

include('functions.php');
$cod_tarjeta = $_GET['txtCodigo'];
$array = array();
if ($resultset = getSQLResultSet("SELECT COUNT(likes.likid) as cantidad FROM likes INNER JOIN tarjetas ON likes.likidtarjeta=tarjetas.tarid WHERE tarjetas.tarid='" . $cod_tarjeta . "'")) {

    while ($row = $resultset->fetch_array(MYSQLI_NUM)) {
        $e = array();
        $e['cantidad'] = $row[0];
        array_push($array, $e);
    }
    echo json_encode($array);
}

