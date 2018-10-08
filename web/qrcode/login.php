<?php
session_start(); // Starting Session
include('connect.php'); // Includes Mysql connect
$error=$_GET['login_err']; // Variable To Store Error Message
if (isset($_POST['submit'])) {
if (empty($_POST['username']) || empty($_POST['password'])) {
$error = "Username or Password is invalid";
}
else
{

$username=$_POST['username'];
$password=$_POST['password'];
$tb_user = mysql_query("SELECT * FROM tb_user WHERE  uname='$username' AND  upass='$password' AND active='1' ", $con);
$row_user = mysql_num_rows($tb_user);
echo $row_user;
// while($row_user = mysql_fetch_array($tb_user)){
// 	$fullname = $row_user[fullname];
// 	$userlevel = $row_user[role];
// }
// $error=$username.$password;
if ($row_user == 1) {
$_SESSION['login_user']=$username;
$_SESSION['login_passwword']=$password;
$error=$error.$level; // Initializing Session
header("location: index.php"); // Redirecting To Other Page
} else {
$error = "Username or Password is invalid";
// header("Location: dangnhap.php?login_err="."Username or Password is invalid".$row_user);
}
mysql_close($con); // Closing Connection
}
}
?>
