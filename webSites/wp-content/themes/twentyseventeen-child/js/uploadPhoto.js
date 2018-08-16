
var storageService = firebase.storage().ref();
var ref = firebase.database().ref('toReview');
var imgLocation={latitude:"",longitude:""};
var imgObject = {author:"",date:"",eco:"",location:"",species:"",uid:"",url:"",vulgar:""};

var reviewedPhotos = firebase.database().ref('PhotosReviewed'); //This is going to add the photo to the reviewed group
var addToUser = firebase.database().ref('Users'); //This is going to add the photo to the user portfolio
var addToSpecies = firebase.database().ref('Species'); //This is going to add to an existing species or create a new one




var userLogged = window.localStorage.getItem('userLogged');
var userUID = window.localStorage.getItem('userUID');
var url;
window.onload = function (){

  var fileButton = document.getElementById('file-select');
  var uploadButton = document.getElementById('file-submit');


  fileButton.addEventListener('change', function(e){

      var file = e.target.files[0];
      var storageRef = firebase.storage().ref('photos/' + userLogged+'/'+file.name);
      var task;

      uploadButton.onclick=function(){
        task = storageRef.put(file);
        uploadInfo(storageRef);
      };
    });

    //Create the map for photo location
    var start = new google.maps.LatLng(40.64092, -8.64803);

    google.maps.event.trigger(map, "resize");
    map.panTo(start);


  google.maps.event.addListener(map, 'click', function(event) {
   placeMarker(event.latLng);
  });

}

function placeMarker(location) {

    var marker = new google.maps.Marker({
        position: location,
        map: map
    });

    google.maps.event.addListener(marker, "click", function (event) {
      getCoords(event);
  });

  //This is used to put the photo name in the same format as the others
  var currentdate = new Date();
  imgObject.date = currentdate.getFullYear().toString() +
                ('0' + (currentdate.getMonth()+1).toString()).slice(-2) + ('0' + currentdate.getDate().toString()).slice(-2)
                + "_"
                + currentdate.getHours() +
                + currentdate.getMinutes() +
                + currentdate.getSeconds();

}


function uploadInfo(storageRef){

  var reviewedPhotos = firebase.database().ref('PhotosReviewed'); //This is going to add the photo to the reviewed group
  var addToUser = firebase.database().ref('Users'); //This is going to add the photo to the user portfolio
  var addToSpecies = firebase.database().ref('Species'); //This is going to add to an existing species or create a new one

  imgObject.eco = document.getElementById('speciesEcology').textContent;
  imgObject.species= document.getElementById('speciesName').textContent;
  imgObject.vulgar = document.getElementById('speciesVulgar').textContent;
  imgObject.author=window.localStorage.getItem('userLogged');
  imgObject.uid=window.localStorage.getItem('userUID');
  storageRef.getDownloadURL().then(function(url){
    imgObject.url=url;

    //Adds photo to Species tree
    addToSpecies.child(document.getElementById('speciesName').textContent).child(imgObject.date).set({
      author: window.localStorage.getItem('userLogged'),
      date: imgObject.date,
      eco: imgObject.eco,
      location: imgLocation,
      species: imgObject.species,
      uid: imgObject.uid,
      url: imgObject.url,
      vulgar: imgObject.vulgar
    });

    //Adds photo to Users tree
    addToUser.child(imgObject.uid).child(imgObject.date).set({
      author: imgObject.author,
      date: imgObject.date,
      eco: imgObject.eco,
      location: imgLocation,
      species: imgObject.species,
      uid: imgObject.uid,
      url: imgObject.url,
      vulgar: imgObject.vulgar
    });

    //Adds photo to PhotosReviewed tree
    reviewedPhotos.child(imgObject.date).set({
      author: imgObject.author,
      date: imgObject.date,
      eco: imgObject.eco,
      location: imgLocation,
      species: imgObject.species,
      uid: imgObject.uid,
      url: imgObject.url,
      vulgar: imgObject.vulgar
    });

    ref.child(imgObject.date).remove();
  });
}

function getCoords(event){
  var latitude = event.latLng.lat();
  var longitude = event.latLng.lng();
  imgLocation.latitude=latitude;
  imgLocation.longitude=longitude;
}
