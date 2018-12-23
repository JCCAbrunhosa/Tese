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


<h1>Avistamentos por Avaliar</h1>
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
    <div class="row">

        <div class="wrap">
      	<div id="primary" class="content-area">
      		<main id="main" class="site-main" role="main">
            <img id="imageToDocument" class="rounded"/>
        			<div id="Name">
        			  Espécie:
        			</div>
        			<p id="speciesName" contenteditable="true"><br>
        				<div id="Vulgar">
        				  Nome Vulgar:
        				</div><br>
        			<input id="speciesVulgar"><br>
        				<div id="Ecology">
        				  Ecologia:
        				</div>
        			<input id="speciesEcology"><br>
        				<div id="Description">
        				  Descrição:
        				</div>
        			<input id="speciesDescription"><br>

      		</main><!-- #main -->

      	</div><!-- #primary -->
      </div><!-- .wrap -->
  </div>
    <br>
    <div class="buttonDiv">
      <button class="button" style="float:right" id="Guardar">Aceitar</button>
      <br>
      <button class="button" style="float:left"id="Cancelar">Rejeitar</button>
    </div>
  </div>

</div>



<?php get_footer();
