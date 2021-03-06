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

<div class="wrap">
	<div id="primary" class="content-area">
		<main id="main" class="site-main" role="main">
			<h1>Avaliar</h1>
			
			<div id="Name">
			  Espécie:
			</div>
			<p id="speciesName" contenteditable="true"><br>
				<div id="Vulgar">
				  Nome Vulgar:
				</div><br>
			<p id="speciesVulgar" contenteditable="true"><br>
				<div id="Ecology">
				  Ecologia:
				</div>
			<p id="speciesEcology" contenteditable="true"><br>
				<div id="Description">
				  Descrição:
				</div>
			<p id="speciesDescription" contenteditable="true"><br>

				<div class="buttonDiv">
	        <button class="button" style="float:right" id="Guardar">Guardar</button>
	        <br>
	        <button class="button" style="float:left"id="Cancelar">Cancelar</button>
	      </div>

		</main><!-- #main -->

	</div><!-- #primary -->
</div><!-- .wrap -->




<?php get_footer();
