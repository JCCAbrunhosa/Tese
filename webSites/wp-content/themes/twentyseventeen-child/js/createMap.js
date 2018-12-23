
  var ref = firebase.database().ref('Species');
  var positions=[];

  var str = "Ver página das Espécie";

  var markers = [];
  var speciesAvailable=[];
  var searchSpecies=[]

  var specName;
  var specVulg ;

  var nameMap;
  var vulgarMap;
  var smallMap;
  var img;


//  var selectedLat = window.localStorage.getItem("photoLat");
  //var selectedLng = window.localStorage.getItem("photoLng");



  ref.once('value', function(snapshot){

    snapshot.forEach(function(child){
      child.forEach(function(photos){
        if(photos.hasChild('location')){

            var myLatLng = {};

            myLatLng.lng=parseFloat(photos.child('location').child('longitude').val());
            myLatLng.lat=parseFloat(photos.child('location').child('latitude').val());

            var marker = new google.maps.Marker({
              position: myLatLng,
              map: map,
              title: photos.child('species').val()
            });

            searchSpecies.push(marker);

            markers.push(marker);
            google.maps.event.trigger(map, "resize");
            map.panTo(myLatLng);

            marker.addListener('click', function(){
                showDetails(marker.title);
            });


        }
      });
    });
  });

  ref.once('value', function(snapshot){
    snapshot.forEach(function(child){
      speciesAvailable.push(child.key);
      jQuery( function() {
        jQuery( "#myInput" ).autocomplete({
          source: speciesAvailable
        });
      } );
    });
  });


    //Method that shows the image details
    function showDetails(title){

      // Get the modal
      var modal = document.getElementById('infoMap');

      // When the user clicks the button, open the modal
      modal.style.display = "block";

      // Get the <span> element that closes the modal
      var span = document.getElementsByClassName("close")[0];

      // When the user clicks on <span> (x), close the modal
      span.onclick = function() {
          modal.style.display = "none";
      }

      // When the user clicks anywhere outside of the modal, close it
      window.onclick = function(event) {
          if (event.target == modal) {
              modal.style.display = "none";
          }
      }

      document.getElementById("seeMore").onclick = function(){
        window.localStorage.setItem("species", title);
        window.location = "http://localhost:8888/galeria/";
      }

      fetchInfo(title);
  }

  function fetchInfo(speciesName){

    ref = firebase.database().ref('Species');
    nameMap = document.getElementById("nameMap");
    vulgarMap = document.getElementById("vulgarMap");
    img= new Image();

    ref.once("value", function(snapshot){
      snapshot.forEach(function(specie){
        if(specie.key.trim()==speciesName){
          specName = document.createTextNode(specie.key);
          specVulg = document.createTextNode(specie.child("vulgar").val());
          specie.forEach(function(capture){
            if(capture.hasChild("url")){
              img.src=capture.child("url").val();
              img.style.height='250px';
              img.style.width='250px';
              img.style.flex='50%';
              img.style.padding='4px';
            }

          });
        }
      });
      nameMap.parentNode.removeChild(nameMap.previousSibling);
      vulgarMap.parentNode.removeChild(vulgarMap.previousSibling);
      document.getElementById("smallImgMap").innerHTML="";
      nameMap.parentNode.insertBefore(specName, nameMap);
      vulgarMap.parentNode.insertBefore(specVulg,vulgarMap);
       document.getElementById("smallImgMap").appendChild(img);
    });

  }


window.onload = function (){
var i;
    document.getElementById("myInput").oninput=function(){
      setMapOnAll(map, document.getElementById("myInput").value);

    }
    //Cleans the map when the search function is focused

  function showMarkers(){
    setMapOnAll(map,null);
  }

  function setMapOnAll(map,markerSearched) {

      for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
      }
      for (var k = 0; k < markers.length; k++) {
        if(markers[k].title.includes(markerSearched)){
          markers[k].setMap(map);
        }
      }
  }
}
