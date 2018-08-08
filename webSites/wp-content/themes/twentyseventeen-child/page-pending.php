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


<div id="photos">Avistamentos da Espécie:</div>
<br>
<div class="row" id="myGrid" style="margin-bottom:128px">
  <div class="column" id="gridCol">
      <div class ="container" id="image">
  </div>
</div>

<div id="anotherModal" style = "display:none" class="modal">

  <!-- Modal content -->
  <div class="modal-content">
    <span class="close">&times;</span>

      <div class="buttonDiv">
        <button class="button" id="Avaliar">Avaliar</button>
        <br>
        <button class="button" id="Apagar">Apagar</button>
      </div>
    <br>
  </div>

</div>



<?php get_footer();
