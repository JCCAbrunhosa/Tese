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
#getLocation {

	width:    100%;
	height:   500px;
	padding-left: 20px;
	padding-right: 20px;

}
</style>

<div class="wrap">
	<div id="primary" class="content-area">
		<main id="main" class="site-main" role="main">
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


		</main><!-- #main -->
		<input type="file" id="myFile" multiple size="50" onchange="uploadPhoto()">
		<div id="getLocation"></div>
		<script>
		var getLocation = new google.maps.Map(document.getElementById('getLocation'), {
				zoom: 8
			});
			google.maps.event.addDomListener(window, "load", captureLocation);

		</script>

	</div><!-- #primary -->
</div><!-- .wrap -->




<?php get_footer();
