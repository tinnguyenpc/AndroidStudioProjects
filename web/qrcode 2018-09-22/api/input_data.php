<?php

include('../connect.php');
date_default_timezone_set('America/Los_Angeles');
$Time= new DateTime();
$Time->setTimezone(new DateTimeZone('Asia/Ho_Chi_Minh'));
$mytime=$Time->format('Y-m-d H:i:s');
$mydate=$Time->format('Y-m-d');

$qr_code= $_REQUEST['qr_code'];
$session_app= $_REQUEST['session_app'];
$imgname = $_REQUEST['img_name'];
$target_Path = "upload/";

if(isset($_REQUEST['img_name'])){
    $image_folder = explode(" ", $_REQUEST['session_app']);
  $img_save = $image_folder[0];
  if (!file_exists($target_Path).$img_save) {
    mkdir($target_Path.$img_save, 0777, true);
    }
  $target_Path = $target_Path. $img_save."/".$imgname;
  $imsrc = base64_decode($_POST['img_base64']);
  $fp = fopen($target_Path, 'w');
  fwrite($fp, $imsrc);
  if(fclose($fp)){
  echo "image-true";
  }else{
  echo "image-false";
  }
}

$filelog = '../mylogs/input_data'.$mydate.'.log';
$ip_client = $_SERVER['REMOTE_ADDR'];
$fcontent1 = $mytime." ".$ip_client."\n";
$fcontent2 = "";
foreach ($_REQUEST as $param_name => $param_val) {
    if($param_name!="img_base64"){
        $fcontent2= $fcontent2."$param_name = $param_val\n";
    }
    
}
$fcontent3 = "---------------------------------\n\n";
// $fcontent = $fcontent1.$fcontent2.$fcontent3;
// file_put_contents($file, $fcontent , FILE_APPEND | LOCK_EX);

$content_err = "";

$tb_qrcode= @mysql_query("select * from tb_qrcode where code='$qr_code'");
if(@mysql_num_rows($tb_qrcode) == 0)
{
echo $content_err="check-qr_code-false";
}
else{
    if(($qr_code!="")&&($imgname!="")&&($imsrc!="")&&($session_app!="")&&($r=@mysql_query("INSERT INTO tb_data SET qr_code='$qr_code', session_app='$session_app', url_img='$imgname'",$con))&&($r2=mysql_query("UPDATE tb_qrcode SET session_qr='$session_app' where code='$qr_code'",$con))){
        echo $content_err="input_data-true";
      }
      else {
          echo $content_err="input_data-false";
        //   echo $content_err="qr-code=".$qr_code." imgname=".$imgname." imsrc=".$imsrc." session_app=".$session_app;
    }
}
echo $fcontent = $fcontent1.$fcontent2.$content_err."\n".$fcontent3;

file_put_contents($filelog, $fcontent , FILE_APPEND | LOCK_EX);


mysql_close($con);
 ?>
