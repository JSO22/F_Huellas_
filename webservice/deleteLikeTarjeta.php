<?php

include('functions.php');
$codigo_tarjeta = $_POST['txtCodigo'];
$correo = $_POST['txtCorreo'];
ejecutarSQLCommand("DELETE FROM likes WHERE likes.likidtarjeta='" . $codigo_tarjeta . "' AND likes.likcorreo='" . $correo . "'");
echo 'Success';
