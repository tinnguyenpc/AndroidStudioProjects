<?php

function rand_string( $length ) {

       $chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";  

       $size = strlen( $chars );

    //    echo "Random string =";

       for( $i = 0; $i < $length; $i++ ) {

              $str= $chars[ rand( 0, $size - 1 ) ];

              echo $str;

       }

}

 rand_string( 10 );

?>