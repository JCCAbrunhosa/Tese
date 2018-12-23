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
	height:   250px;


}
</style>



<br>
<br>
<div class="container">
<div class="row">
	<div class="col-md">
					<h1>Galeria</h1>

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
</div>
		<div class="col">
			<div class="row">
			    <img id="imageCont" class="rounded float-right"/>
			</div>
			<br>
	</div>
	</div>
	<br>
	<div class="row">
		<div class="col-sm-3">
			<div class="col-sm-6">
			<button class="button" id="proceedToBlog" onclick="goToBlog()">Blog</button>
		</div>
	</div>
		<div class="col-sm-3">
			<div class="col-sm-6">
			<button class="button" style="display:none" id="editSpecies" onclick="editNewInfo()">Editar Espécie</button>
		</div>
	</div>
		<div class="col-sm-3">
			<div class="col-sm-6">
			<button class="button" id="displayPrevImage" onclick="displayPreviousImage()">Anterior</button>
		</div>
	</div>
		<div class="col-sm-3">
			<div class="col-sm-6">
			<button class="button" id="displayNextImage" onclick="displayNextImage()">Próximo</button>
		</div>
	</div>
		</div>
</div>
<!--Modal for data edition-->
<div id="editData" style = "display:none" class="modal">
	<div class="modal-content">
		<span class="close" style="float:right">&times;</span>
		<h1>Editar Informações</h1>
		<br>
		<div id="editSpecies">
			Espécie:<br>
		</div>
		<div id="newSpecies">
      <input type="text" id="speciesN">
    </div>
		<div id="editVulgar">
			Nome Vulgar:<br>
		</div>
		<div id="newVulgar">
      <input type="text" id="vulgarN">
    </div>
		<div id="editEcology">
			Ecologia:<br>
		</div>
		<div id="newEcology">
      <input type="text" id="ecologyN">
    </div>
		<div id="editDescription">
			Descrição:<br>
		</div>
		<div id="newDescription">
			<input type="text" id="descriptionN">
		</div>
		<button class="button" id="uploadNewInfo" onclick="uploadNewInfo()">Confirmar edições</button>
</div>
</div>
  <!-- The Modal regarding images -->
<div id="myModal" style = "display:none" class="modal">
  <!-- Modal content -->
  <div class="modal-content">
    <span class="close1">&times;</span>
    <br>
		<div class="row">
		<div class="col-sm">
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
	      </div>
			</div>
			<div class="col-sm">
				<img id="smallImg" class="rounded-circle"/>
			</div>
		</div>
		<br>
		<div id="locationLabel">
			Localização<br>
		</div>
    <div id="map"></div>
  </div>
</div>
  <script type="text/javascript">

  var map = new google.maps.Map(document.getElementById('map'), {
  		zoom: 8,
			 disableDefaultUI: true
  	});
  </script>

</div>


<?php get_footer();
