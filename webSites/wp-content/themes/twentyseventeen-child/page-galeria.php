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

<br>
<div id="speciesName">
  Espécie:<br>
</div>
<div id="nameH">
  <p id="name"></p>
</div>
<div id="speciesVulg">
  Nome Vulgar:<br>
</div>
<div id="vulgarH">
  <p id="vulgar"></p>
</div>
<div id="speciesDesc">
  Descrição:<br>
</div>
<div id="descH">
  <p id="desc"></p>
</div>

<div id="speciesEcology">
  Ecologia:<br></div>
<div id="ecoH">
  <p id="ecology"></p>
</div>
<br>

<div id="photos">Avistamentos da Espécie:</div>
<br>
<div class="row" id="myGrid" style="margin-bottom:128px">
  <div class="column" id="gridCol">
      <div class ="container" id="imageCont">
  </div>
</div>
  <!-- The Modal -->
<div id="myModal" style = "display:none" class="modal">

  <!-- Modal content -->
  <div class="modal-content">
    <span class="close">&times;</span>
    <div id="smallImg"></div>
    <br>
    <div id="speciesName">
      Espécie:<br>
    </div>
    <div id="nameMH">
      <p id="nameM"></p>
    </div>
    <div id="speciesVulg">
      Nome Vulgar:<br>
    </div>
    <div id="vulgarMH">
      <p id="vulgarM"></p>
    </div>
      <div id="takenByMH">
        Tirada por:<br>
      </div>
      <div id="authorMH">
        <p id="authorM"></p>
      <div class="buttonDiv">
        <button class="button" id="seeMap">Ver no Mapa</button>
        <br>
        <button class="button" id="moreAuthor">Mais avistamentos deste utilizador</button>
      </div>
    <br>
  </div>

</div>


<?php get_footer();
