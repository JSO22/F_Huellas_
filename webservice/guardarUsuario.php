<?php

include('functions.php');
$codigo = $_POST['txtCodigo'];
$nombre = $_POST['txtNombre'];
$correo = $_POST['txtCorreo'];
$imagen = $_POST['txtImagen'];
$telefono = $_POST['txtTelefono'];
$direccion = $_POST['txtDireccion'];
$token = $_POST['txtToken'];
$dispositivo = $_POST['txtDispositivo'];
$con = 0;
if ($resultset = getSQLResultSet("SELECT usuarios.usuid FROM usuarios WHERE usuarios.usucorreo='" . $correo . "'")) {

    while ($row = $resultset->fetch_array(MYSQLI_NUM)) {
        $con++;
    }
}
if ($con > 0) {
    if ($dispositivo == 'Android') {
        ejecutarSQLCommand("UPDATE usuarios SET usuarios.usunombre='" . $nombre . "', usuarios.usutelefono='" . $telefono . "', usuarios.usudireccion='" . $direccion . "', usuarios.usuidphoneandroid='" . $token . "' WHERE usuarios.usucorreo='" . $correo . "'");
    } else {
        ejecutarSQLCommand("UPDATE usuarios SET usuarios.usunombre='" . $nombre . "', usuarios.usutelefono='" . $telefono . "', usuarios.usudireccion='" . $direccion . "', usuarios.usuidphoneios='" . $token . "' WHERE usuarios.usucorreo='" . $correo . "'");
    }
} else {
    if ($dispositivo == 'iOS') {
        ejecutarSQLCommand("INSERT INTO usuarios (usuarios.usuid, usuarios.usunombre, usuarios.usucorreo,  usuarios.usuimagen, usuarios.usutelefono, usuarios.usudireccion, usuarios.usuidphoneios) VALUES ('" . $codigo . "','" . $nombre . "','" . $correo . "','" . $imagen . "','" . $telefono . "','" . $direccion . "','" . $token . "')");
    } else {
        ejecutarSQLCommand("INSERT INTO usuarios (usuarios.usuid, usuarios.usunombre, usuarios.usucorreo, usuarios.usuimagen, usuarios.usutelefono, usuarios.usudireccion, usuarios.usuidphoneandroid) VALUES ('" . $codigo . "','" . $nombre . "','" . $correo . "','" . $imagen . "','" . $telefono . "','" . $direccion . "','" . $token . "')");
        echo "registro";
    }
}
?>