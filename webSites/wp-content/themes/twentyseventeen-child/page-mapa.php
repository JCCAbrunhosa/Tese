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


}
</style>

<div class="wrap">
	<div id="primary" class="content-area">
		<main id="main" class="site-main" role="main">
				<h1>Mapa</h1>
				<input type="text" id="myInput" oninput="searchSpecies()" placeholder="Espécie">
				<br>
		</main><!-- #main -->
	</div><!-- #primary -->
</div><!-- .wrap -->
<div id="map"></div>
<script type="text/javascript">

var map = new google.maps.Map(document.getElementById('map'), {
		zoom: 8
	});
</script>

<?php get_footer();
