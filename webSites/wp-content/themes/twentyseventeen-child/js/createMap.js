
  var ref = firebase.database().ref('Species');
  var positions=[];

  var str = "Ver página das Espécie";

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
