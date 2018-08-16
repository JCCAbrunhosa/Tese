
function captureLocation(){
  var marker = new google.maps.Marker({
    position: myLatLng,
    getLocation: getLocation ,
    title: photos.child('species').val()
  });
  google.maps.event.trigger(getLocation, "resize");
  map.panTo(myLatLng);


  var ref = firebase.database().ref('Species');
  var positions=[];

  ref.once('value', function(snapshot){
    snapshot.forEach(function(child){
      child.forEach(function(photos){
        if(photos.hasChild('location')){

        }
      });
    });
  });
}
