function load_data_home() {
    var homefilter = "";
    switch($("input[name=qr_filter]:checked").val() ) {
        case "qr_on":
            homefilter = "qr_on"
            break;
        case "qr_off":
            homefilter = "qr_off"
            break;
        default:
            homefilter = "qr_all"
    }
    // alert(date_tb_order);
    $.get("/qrcode/modules/div-tb_data_home.php", {
        homefilter: homefilter
    }, function (data) {
        // alert(data);
        $("#tb_dataqr").html(data);
    
    });
}