<html>
<body>
<?
include('../connect.php');
$homefilter = $_REQUEST['homefilter'];
date_default_timezone_set('America/Los_Angeles');
$Time= new DateTime();
$Time->setTimezone(new DateTimeZone('Asia/Ho_Chi_Minh'));
$mytime= $Time->format('Y-m-d H:i:s');
$today= $Time->format('Y-m-d');
$mydate= $Time->format('d/m/Y');
// $mymonth= $Time->format('Y-m');
// $fmonth = date("m",strtotime($today));
// $fyear = date("Y",strtotime($today));
// $myhour = $Time->format('H');

switch ($homefilter) {
    case 'qr_on':
        $tb_qrdata = mysql_query("SELECT *
        FROM tb_data LEFT JOIN tb_qrcode ON tb_data.qr_code = tb_qrcode.code
        WHERE DATE(tb_data.session_app) = '$today' AND time_out IS NULL
        ORDER BY tb_data.id DESC");
        break;

    case 'qr_off':
        $tb_qrdata = mysql_query("SELECT *
        FROM tb_data LEFT JOIN tb_qrcode ON tb_data.qr_code = tb_qrcode.code
        WHERE DATE(tb_data.session_app) = '$today' AND time_out IS NOT NULL
        ORDER BY tb_data.id DESC");
        break;
    
    default:
        $tb_qrdata = mysql_query("SELECT *
        FROM tb_data LEFT JOIN tb_qrcode ON tb_data.qr_code = tb_qrcode.code
        WHERE DATE(tb_data.session_app) = '$today'
        ORDER BY tb_data.id DESC");
        break;
}
?>
<div>
    <table class="table  table-bordered">
        <thead>
            <tr>
                <th class="text-center">Thẻ</th>
                <th class="text-center">Hình ảnh</th>
                <th class="text-center" style="width:10%;">Vào</th>
                <th class="text-center" style="width:10%;">Ra</th>
                            
            </tr>
        </thead>
        <tbody>
        <?
            if(mysql_num_rows($tb_qrdata) != 0)
            {
                while($r_tb_qrdata = mysql_fetch_array($tb_qrdata)){
        ?>
        <tr>
            <td class="text-center" style="font-size:xx-large; font-weight: 500;">
                <?
                echo $r_tb_qrdata[name];
                ?>
            </td>
            <td align="center"> 
                <?
                    $image_folder = explode(" ", $r_tb_qrdata[url_img]);
                    $img_save = $image_folder[1];
                ?>
                <img class="img_inout" width="100px" height="50px"  data-toggle="modal" data-target="#imgview" src="/qrcode/api/upload/<? echo $img_save."/".$r_tb_qrdata[url_img];?>">  
            </td>
            <td>
                <?
                $time_in = $r_tb_qrdata[session_app];
                if($time_in !=""){
                    echo (date('H:i', strtotime($time_in)));
                }
                ?>
            </td>
            <td>
                <?
                
                $time_out = $r_tb_qrdata[time_out];
                if($time_out !=""){
                    echo (date('H:i', strtotime($time_out)));
                }
                
                ?>
            </td>
            
        </tr>
        <?
                }
            }
        ?>
        </tbody>
    </table>
</div>
</body>
</html>