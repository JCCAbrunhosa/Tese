<?php
/**
 * The template for displaying all pages
 *
 * This is the template that displays all pages by default.
 * Please note that this is the WordPress construct of pages
 * and that other 'pages' on your WordPress site may use a
 * different template.
 *
 * @link https://codex.wordpress.org/Template_Hierarchy
 *
 * @package WordPress
 * @subpackage Twenty_Seventeen
 * @since 1.0
 * @version 1.0
 */

get_header(); ?>
<h1> Detalhes da Conta</h1>
<br>
<div id="userName">
    User Name:<br>
</div>
<div id="nameH">
    <p id="name"></p>
</div>
<div id="header">
    Email de Contacto:
</div>
<div id="emailH">
    <div id="email"></div>
</div>
<br>
<div id="photos">Seus Avistamentos:</div>
<br>
<div class="row" id="myGrid" style="margin-bottom:128px">
  <div class="column" id="gridCol">
      <div class ="container" id="ownPics">
  </div>
</div>

<?php get_footer();
