<?php
session_start();
if(session_destroy()) // Destroying All Sessions
{
header("Location: dangnhap.php"); // Redirecting To Home Page
}
?>
