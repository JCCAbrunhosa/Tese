
  var ref = firebase.database().ref('Species');
  var positions=[];

  var str = "Ver página das Espécie";

  var markers = [];
  var speciesAvailable=[];
  var searchSpecies={name:"", marker:""};

  var selectedLat = window.localStorage.getItem("photoLat");
  var selectedLng = window.localStorage.getItem("photoLng");


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

            searchSpecies.name=photos.child('species').val();
            searchSpecies.markers=marker;

            markers.push(searchSpecies);
            google.maps.event.trigger(map, "resize");
            map.panTo(myLatLng);

            var contents = '<div>' + marker.title + '</div><br><div> ' + str.link("file:///Users/JCCA/teseWeb/sampleWebsite/speciesGallery.html#" + marker.title)+ '</div>' ;

            var infoWindow = new google.maps.InfoWindow({
              content:contents
            });

            marker.addListener('click', function(){
                infoWindow.open(map, marker);
               //window.location = "file:///Users/JCCA/teseWeb/sampleWebsite/speciesGallery.html#" + marker.title;
            });

            infoWindow.addListener('click', function(){
              window.location = "file:///Users/JCCA/teseWeb/sampleWebsite/speciesGallery.html#" + marker.title;
            });
        }
      });
    });
  });

  //Cleans the map when the search function is focused
function searchSpecies(){
    var specie = document.getElementById("myInput").value;
    alert("hey");

}

function showMarkers(){
  setMapOnAll(map,null);
}

function setMapOnAll(map,markerSearched) {
    for (var i = 0; i < markers.length; i++) {
      markers[i].marker.setMap(null);
    }
}
