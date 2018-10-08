<?php

  $host='localhost';
  $uname='tin_qrcode';
  $db="tin_qrcode";
  $pwd='Grsc@123';

  //@ khi dung hosttinger bi bao loi thoi
  $con = mysql_connect($host,$uname,$pwd,'charset=utf8') or die("connection failed");
  mysql_query ("set character_set_client='utf8'");
  mysql_query ("set character_set_results='utf8'");

  mysql_query ("set collation_connection='utf8_general_ci'");
  mysql_select_db($db,$con) or die("db selection failed");

  // header('Location: index.php'); // Redirecting To Home Page


?>
