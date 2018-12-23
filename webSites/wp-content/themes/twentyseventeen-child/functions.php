<?php

  function twentyseventeen_child_scripts(){

      wp_enqueue_script('google-maps', 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBEgYZJ7ikdHhHz-TofIZjUwMoC5e7_EFE');
      wp_enqueue_script('firebase-all', 'https://www.gstatic.com/firebasejs/4.12.1/firebase.js');
      wp_enqueue_script('firebase-auth', 'https://www.gstatic.com/firebasejs/4.10.1/firebase-auth.js');
      wp_enqueue_script('firebase-database','https://www.gstatic.com/firebasejs/4.9.0/firebase-database.js');
      wp_enqueue_script('jquery', "https://code.jquery.com/jquery-1.12.4.js");
      wp_enqueue_script('jqueryUI', "https://code.jquery.com/ui/1.12.1/jquery-ui.js");
      wp_enqueue_script('connectToFirebase', get_stylesheet_directory_uri() . '/js/connectToFirebase.js');
      wp_enqueue_script('checkLogin', get_stylesheet_directory_uri() . '/js/checkLogin.js');
      if(is_page('mapa')){
        wp_enqueue_script('createMap', get_stylesheet_directory_uri() . '/js/createMap.js');
      }
      if(is_page('upload')){
        wp_enqueue_script('uploadPhoto', get_stylesheet_directory_uri() . '/js/uploadPhoto.js');
      }
      wp_enqueue_script('fetchSpecies', get_stylesheet_directory_uri() . '/js/fetchSpecies.js');
      wp_enqueue_script('login', get_stylesheet_directory_uri() . '/js/login.js');
      if(is_page('galeria')){
          wp_enqueue_script('fetchPhotos', get_stylesheet_directory_uri() . '/js/fetchPhotos.js');
      }
      if(is_page('conta')){
        wp_enqueue_script('getUserInfo', get_stylesheet_directory_uri() . '/js/getUserInfo.js');
      }
      wp_enqueue_script('captureLocation', get_stylesheet_directory_uri() . '/js/captureLocation.js');
      wp_enqueue_script('getPending', get_stylesheet_directory_uri() . '/js/getPending.js');
      wp_enqueue_script('uploadCapture', get_stylesheet_directory_uri() . '/js/uploadUserPhoto.js');

  }
  add_action('wp_enqueue_scripts' , 'twentyseventeen_child_scripts');
 ?>
