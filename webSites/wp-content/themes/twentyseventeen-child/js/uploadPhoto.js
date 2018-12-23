
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
var marker;

//The page loads scripts only after being fully loaded
window.onload = function (){

  var fileButton = document.getElementById('file-select');
  var uploadButton = document.getElementById('file-submit');
  var speciesInput = document.getElementById('speciesName');


  //Autocomplete that checks if species already exists and description, ecology and vulgar name are automatically filled

    addToSpecies.once('value', function(snapshot){
      snapshot.forEach(function(child){
        speciesAvailable.push(child.key);
        speciesInput.addEventListener('input',function(){
          jQuery( function() {
            jQuery( `#speciesName`).autocomplete({
              source: speciesAvailable,
              select: function( event , ui ) {
                if((ui.item.label != null)==true){
                  document.getElementById("speciesVulgar").disabled=true;
                  document.getElementById("speciesVulgar").value="Já existe";
                  document.getElementById("speciesEcology").disabled=true;
                  document.getElementById("speciesEcology").value="Já existe";
                  document.getElementById("speciesDescription").disabled=true;
                  document.getElementById("speciesDescription").value="Já existe";
                }
              }
            });
          });
          document.getElementById("speciesVulgar").disabled=false;
          document.getElementById("speciesVulgar").value="";
          document.getElementById("speciesEcology").disabled=false;
          document.getElementById("speciesEcology").value="";
          document.getElementById("speciesDescription").disabled=false;
          document.getElementById("speciesDescription").value="";
      });
    } );
  });


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
  map.addListener('click', function(event) {

    if (marker) {
      marker.setMap(null);
    }

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


  var reviewedPhotos = firebase.database().ref('PhotosReviewed'); //This is going to add the photo to the reviewed group
  var addToUser = firebase.database().ref('Users'); //This is going to add the photo to the user portfolio
  var addToSpecies = firebase.database().ref('Species'); //This is going to add to an existing species or create a new one
  var imgLocation={latitude:"",longitude:""};

  if(document.getElementById("speciesEcology").value=="Já existe"){
    firebase.database().ref("Species").once('value', function(snapshot){
      snapshot.forEach(function(name){
        if(document.getElementById("speciesName").textContent==name.key.trim()){
          imgObject.eco=name.child("ecology").val();
          imgObject.vulgar=name.child("vulgar").val();
        }
      });
    });
  }else{
    imgObject.eco = document.getElementById('speciesEcology').value;
    imgObject.vulgar = document.getElementById('speciesVulgar').value;
  }

  imgObject.species= document.getElementById('speciesName').textContent;
  imgObject.author=window.localStorage.getItem('userLogged');
  imgObject.uid=window.localStorage.getItem('userUID');


  storageRef.getDownloadURL().then(function(url){
    imgObject.url=url;

    imgLocation.latitude=latitude;
    imgLocation.longitude=longitude;

    //Adds photo to Species tree
    addToSpecies.child(document.getElementById(`speciesName`).textContent).child(imgObject.date).set({
      author: displayName,
      date: imgObject.date,
      eco: imgObject.eco,
      location: imgLocation,
      species: imgObject.species,
      uid: imgObject.uid,
      url: imgObject.url,
      vulgar: imgObject.vulgar
    });

    //Adds photo to Users tree
    addToUser.child(imgObject.uid).child(document.getElementById("speciesName").textContent).child(imgObject.date).set({
      author: displayName,
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
      author: displayName,
      date: imgObject.date,
      eco: imgObject.eco,
      location: imgLocation,
      species: imgObject.species,
      uid: imgObject.uid,
      url: imgObject.url,
      vulgar: imgObject.vulgar
    });


    if(document.getElementById("speciesDescription").value.trim()!="Já existe"){
      addToSpecies.child(document.getElementById("speciesName").textContent).child("description").set(document.getElementById("speciesDescription").value);
      addToSpecies.child(document.getElementById("speciesName").textContent).child("ecology").set(imgObject.eco);
      addToSpecies.child(document.getElementById("speciesName").textContent).child("vulgar").set(imgObject.vulgar);
      addToUser.child(imgObject.uid).child(document.getElementById("speciesName").textContent).child("description").set(document.getElementById("speciesDescription").value);
      addToUser.child(imgObject.uid).child(document.getElementById("speciesName").textContent).child("ecology").set(imgObject.eco);
      addToUser.child(imgObject.uid).child(document.getElementById("speciesName").textContent).child("vulgar").set(imgObject.vulgar);
    }

  });

}
