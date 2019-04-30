<?php
include('functions.php');
$array = array();
if ($resultset = getSQLResultSet("SELECT * FROM view_tarjetas")) {

    while ($row = $resultset->fetch_array(MYSQLI_NUM)) {
        $e = array();
        $e['tarid'] = $row[0];
        $e['tarnombre'] = $row[1];
        $e['tardescripcion'] = $row[2];
        $e['tarimagen'] = $row[3];
        $e['tartipo'] = $row[4];
        $e['tarestado'] = $row[5];
        $e['tarlikes'] = $row[6];
        array_push($array, $e);
    }
    echo json_encode($array);
}
