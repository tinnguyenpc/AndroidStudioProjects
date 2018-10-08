<?php
include('../connect.php'); // Includes Login Script
?>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>QR Code GRSC</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <script src="../plugins/jquery/3.1.1/jquery.min.js"></script>
    <!-- <script src="plugins/live-query/1.3.6/jquery.livequery.min.js"></script> -->
    <link rel="stylesheet" href="../plugins/bootstrap/3.3.7/bootstrap.min.css">
    <script src="../plugins/bootstrap/3.3.7/bootstrap.min.js"></script>
    <link rel="stylesheet" href="../plugins/datatables/dataTables.bootstrap.css">
    <script src="../plugins/datatables/jquery.dataTables.min.js"></script>
    <script src="../plugins/datatables/dataTables.bootstrap.min.js"></script>
    <link rel="stylesheet" href="../plugins/font-awesome/font-awesome.min.css">

    <script src="../js/myfunction.js"></script>
    <script src="../js/home.js"></script>
    <link rel="stylesheet" href="../css/qrcode.css">




  </head>
<?
date_default_timezone_set('America/Los_Angeles');
$Time= new DateTime();
$Time->setTimezone(new DateTimeZone('Asia/Ho_Chi_Minh'));
$mytime= $Time->format('Y-m-d H:i:s');
$today= $Time->format('Y-m-d');
$mydate= $Time->format('d/m');
// $mymonth= $Time->format('Y-m');
// $fmonth = date("m",strtotime($today));
// $fyear = date("Y",strtotime($today));
// $myhour = $Time->format('H');
?>
  <body>
    <div class="content_main col-xs-12">
      <!-- <div class="col-md-3">
      <img src="images/grsc-logo.jpg" width="200" alt="">
      </div> -->
      <div class="box col-md-6 col-md-offset-3">
        <div class="box-header">
          <div class="col-md-12" style="text-align:center;" ><span style="font-weight:bold;">Ngày: <? echo $mydate;?> | Đang Check in: </span> <span style="background-color: #FFFF00; font-weight:bold; color: red; font-size: large;">
          <?
            $cout_checkin= mysql_query(
              "SELECT *
              FROM tb_data LEFT JOIN tb_qrcode ON tb_data.qr_code = tb_qrcode.code
              WHERE DATE(tb_data.session_app) = '$today' AND time_out IS NULL"
            );
            echo mysql_num_rows($cout_checkin);
          ?>
          </span>
          </div>
        </div>
        <div class="box-body" style="text-align:center;">
          <!-- Filter -->
          <!-- <div class="col-md-4" style=
              "border: solid;
              /* border-collapse: separate; */
              border-style: double;
              border-width: 3px;
              border-color: #b7b3b3;
              font-size: 17px;
              text-align:center;"> -->
            <div class="col-md-12">
            <label class="radio-inline rd_qrall"><input type="radio" name="qr_filter"  value="qr_all">Tất cả</label>
            <label class="radio-inline rd_qron"><input type="radio" name="qr_filter" checked value="qr_on">Đang Check in</label>
            <label class="radio-inline rd_qroff"><input type="radio" name="qr_filter"  value="qr_off">Checked out</label>
          </div>
        <!-- /.box-body -->
        </div>
      <!-- /.box -->
      </div>
    <!-- /.col-XS-12 -->
    </div>
    
    <div class="clear-fix"></div>
    
    <div id="tb_dataqr" class="col-md-6 col-md-offset-3">
    
    </div>
    <!-- The Modal Image -->
    <div class="modal" id="imgview" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" style="width: 80%; height: auto; margin: auto;">
          <div class="modal-content">
            <div class="modal-body">
              <!-- <button type="button" class="close" data-dismiss="modal" aria-label="Close" id="closeVideo"><span aria-hidden="true">&times;</span></button> -->
              <img width="100%" height="500" src="" frameborder="0" allowfullscreen id="iframeImage">
            </div>
          </div>
        </div>
    </div>

  </body>
</html>
