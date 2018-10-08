<?php
  date_default_timezone_set('America/Los_Angeles');
  //date_default_timezone_set('UTC');
  $Time= new DateTime();
  $Time->setTimezone(new DateTimeZone('Asia/Ho_Chi_Minh'));
  $mytime= $Time->format('Y-m-d H:i:s');//datetime sql type
  echo $mytime;
?>