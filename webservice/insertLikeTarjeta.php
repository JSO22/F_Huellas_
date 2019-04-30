<?php

include('functions.php');
$codigo_tarjeta = $_POST['txtCodigo'];
$correo = $_POST['txtCorreo'];
ejecutarSQLCommand("INSERT INTO likes(likidtarjeta, likcorreo) VALUES ('" . $codigo_tarjeta . "','" . $correo . "')");
echo 'Success';

