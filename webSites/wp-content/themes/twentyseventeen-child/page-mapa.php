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

	width:    80%;
	height:   500px;
	margin: auto;
    border: 3px solid green;
    padding: 10px;


}
</style>

<div class="wrap">
	<div id="primary" class="content-area">
		<main id="main" class="site-main" role="main">
				<h1>Mapa</h1>
				<input type="text" placeholder="Pesquise a sua espécie aqui." id="myInput">
				<br>
		</main><!-- #main -->
	</div><!-- #primary -->
</div><!-- .wrap -->
<div id="map"></div>

<div id="infoMap" style = "display:none" class="modal">

  <!-- Modal content -->
	<div class='container'>
  	<div class="modal-content">
    	<span class="close">&times;</span>
			<div class="row">
				<div class="col">
					<div id="smallImgMap"></div>
				</div>
				<div class="col">
					<div id="speciesName">
					 Espécie:
					</div>
					<div id="nameHMap">
						<p id="nameMap"></p>
					</div>
					<div class="row">
						<div class="col">
							<div id="speciesVulg">
							 Nome Vulgar:
							</div>
							<div id="vulgarHMap">
								<p id="vulgarMap"></p>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col">
							<button id ="seeMore" onclick="seeMore()">Ver mais</button>
						</div>
					</div>

				</div>
			</div>
			</div>
		</div>
  </div>

<script type="text/javascript">

var map = new google.maps.Map(document.getElementById('map'), {
		zoom: 8
	});
</script>

<?php get_footer();
