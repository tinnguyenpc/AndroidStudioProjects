<?php

require('../connect.php');

$tbqrcode= @mysql_query("select * from tb_qrcode");

if(@mysql_num_rows($tbqrcode) == 0)
{
echo "no-data";
}
else

$rows = array();
while($r = mysql_fetch_assoc($tbqrcode)) {
    $rows[] = $r;
}

$data = array('GRSC_QR' => $rows);
print json_encode($data);

$data2 = json_decode(json_encode($data));

 ?>
