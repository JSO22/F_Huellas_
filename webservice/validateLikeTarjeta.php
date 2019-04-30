<?php

include('functions.php');
$codigo_tarjeta = $_POST['txtCodigo'];
$correo = $_POST['txtCorreo'];
$con = 0;
if ($resultset = getSQLResultSet("SELECT likes.likid FROM likes WHERE likes.likidtarjeta='" . $codigo_tarjeta . "' AND likes.likcorreo='" . $correo . "'")) {
    while ($row = $resultset->fetch_array(MYSQLI_NUM)) {
        $con++;
    }
}
if ($con > 0) {
    echo 'Si';
} else {
    echo 'No';
}

