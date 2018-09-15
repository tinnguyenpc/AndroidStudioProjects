<?php
include("../connect.php");
date_default_timezone_set('America/Los_Angeles');
$Time= new DateTime();
$Time->setTimezone(new DateTimeZone('Asia/Ho_Chi_Minh'));
$mytime=$Time->format('Y-m-d H:i:s');
$mydate=$Time->format('Y-m-d');


$filelog = '../mylogs/update_data'.$mydate.'.log';
$ip_client = $_SERVER['REMOTE_ADDR'];
$fcontent1 = $mytime." ".$ip_client."\n";
$fcontent2 = "";
foreach ($_REQUEST as $param_name => $param_val) {
        $fcontent2= $fcontent2."$param_name = $param_val\n";    
}
$fcontent3 = "---------------------------------\n\n";
// $fcontent = $fcontent1.$fcontent2.$fcontent3;
// file_put_contents($file, $fcontent , FILE_APPEND | LOCK_EX);

$content_err = "";


if((isset($_REQUEST["session_app"]))&&(isset($_REQUEST["qrcode"]))){
  $session_app=$_REQUEST["session_app"];
  $qr_code=$_REQUEST["qrcode"];

  $tb_data = mysql_query("select * from tb_data WHERE session_app='$session_app' and qr_code='$qr_code' ");
  if(mysql_num_rows($tb_data) == 0)
  {
  echo $content_err="update_data-false";
  }
  else {
    if(($session_app!="")&&($qr_code!="")&&($r=mysql_query("UPDATE tb_data SET time_out='$mytime' where session_app='$session_app' AND qr_code='$qr_code' ",$con))&&($r2=mysql_query("UPDATE tb_qrcode SET session_qr=NULL where code='$qr_code'",$con)))
    {
      echo $content_err="update_data-true";
    }
    else
    {
      echo $content_err="update_data-false";
    }
  }
  mysql_close($con);
}

echo $fcontent = $fcontent1.$fcontent2.$content_err."\n".$fcontent3;

file_put_contents($filelog, $fcontent , FILE_APPEND | LOCK_EX);

?>