$( document ).ready(function() {
    load_data_home();
    $('input[type=radio][name=qr_filter]').change(function() {
        load_data_home();
    });


});


$(document).on("click", ".img_inout", function () {
    var url_img = $(this).attr("src")
    console.log(url_img);
    $("#iframeImage").attr("src",url_img);
});