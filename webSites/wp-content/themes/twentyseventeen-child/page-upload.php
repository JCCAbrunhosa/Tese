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

<style type="text/css">
#map {

	width:    100%;
	height:   500px;
	padding-left: 20px;
	padding-right: 20px;

}
</style>

<div class="wrap">
	<div id="primary" class="content-area">
		<main id="main" class="site-main" role="main">
			<h1>Carregar Avistamento</h1>

			<div id="Name">
			  Espécie:
			</div>
			<div class="ui-widget">
				<p id="speciesName" contenteditable="true"><br>
			</div>
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
			<input id="speciesDescription" ><br>


		</main><!-- #main -->
		<div id="map"></div>
		<input type="file" id='file-select' class="file-select" accept="image/*"/>


		<button id='file-submit' class="file-submit">Upload</button>

	</div><!-- #primary -->
</div><!-- .wrap -->
<script type="text/javascript">
	var map = new google.maps.Map(document.getElementById('map'), {
		zoom: 8
	});
</script>



<?php get_footer();
