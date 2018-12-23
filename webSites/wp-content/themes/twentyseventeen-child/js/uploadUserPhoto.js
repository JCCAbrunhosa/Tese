
var storageService = firebase.storage().ref();
var ref = firebase.database().ref('toReview');
var imgObject = {author:"",date:"",eco:"",location:"",species:"",uid:"",url:"",vulgar:""};
var imgLocation={latitude:"",longitude:""};

var reviewedPhotos = firebase.database().ref('PhotosReviewed'); //This is going to add the photo to the reviewed group
var addToUser = firebase.database().ref('Users'); //This is going to add the photo to the user portfolio
var addToSpecies = firebase.database().ref('Species'); //This is going to add to an existing species or create a new one


var userLogged = window.localStorage.getItem('userLogged');
var userUID = window.localStorage.getItem('userUID');
var displayName=window.localStorage.getItem("displayName");
var url;
var speciesAvailable=[];

var alreadyExists;

//The page loads scripts only after being fully loaded
window.onload = function (){

  var fileButton = document.getElementById('file-select');
  var uploadButton = document.getElementById('file-submit');
  var speciesInput = document.getElementById('speciesName');


  //
  fileButton.addEventListener('change', function(e){

      var file = e.target.files[0];
      var storageRef = firebase.storage().ref('photos/' + userLogged+'/'+file.name);
      var task;

      uploadButton.onclick=function(){
        task = storageRef.put(file);
        uploadInfo(storageRef, imgLocation.latitude, imgLocation.longitude);
      };
    });

    //Create the map for photo location
    var start = new google.maps.LatLng(40.64092, -8.64803);

    google.maps.event.trigger(map, "resize");
    map.panTo(start);

  //Adds a marker to the map and captures its location (gets the most recent click)
  google.maps.event.addListener(map, 'click', function(event) {

    var marker = new google.maps.Marker({
        position: event.latLng,
        map: map
    });

    imgLocation.latitude=event.latLng.lat();
    imgLocation.longitude=event.latLng.lng();
  });

}

//Uploads all info
function uploadInfo(storageRef, latitude, longitude){

  //This is used to put the photo name in the same format as the others
  var currentdate = new Date();
  imgObject.date = currentdate.getFullYear().toString() +
                ('0' + (currentdate.getMonth()+1).toString()).slice(-2) + ('0' + currentdate.getDate().toString()).slice(-2)
                + "_"
                + currentdate.getHours() +
                + currentdate.getMinutes() +
                + currentdate.getSeconds();

  var imgLocation={latitude:"",longitude:""};

  imgObject.author=window.localStorage.getItem('userLogged');
  imgObject.uid=window.localStorage.getItem('userUID');


  storageRef.getDownloadURL().then(function(url){
    imgObject.url=url;

    var photosToReview = firebase.database().ref('toReview');
    var photosToReviewUser = firebase.database().ref("Users");

    imgLocation.latitude=latitude;
    imgLocation.longitude=longitude;

    //Adds photo to PhotosReviewed tree
    photosToReview.child(imgObject.date).set({
      author: displayName,
      date: imgObject.date,
      eco: "",
      location: imgLocation,
      species: "",
      uid: imgObject.uid,
      url: imgObject.url,
      vulgar: ""
    });

    photosToReviewUser.child(imgObject.uid).child("ToReview").child(imgObject.date).set({
      author: displayName,
      date: imgObject.date,
      eco: "",
      location: imgLocation,
      species: "",
      uid: imgObject.uid,
      url: imgObject.url,
      vulgar: ""
    });


  });

}
