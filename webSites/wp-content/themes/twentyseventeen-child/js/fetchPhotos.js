
var ref = firebase.database().ref('Species');
var addToUser = firebase.database().ref('Users');

var speciesName;
var speciesDesc;
var speciesEco;
var speciesVulg;
var photoAuthor;
var nameChild;
var descChild;
var ecoChild;
var vulgChild;
var span;

var spName;
var spVulgar;

var smallImg;
var overallImage;

var markers=[];

var x=-1;

var imgArray = [];
var i=0;
var imgLocation={latitude:"",longitude:""};
var imgObject = {author:"",date:"",eco:"",location:"",species:"",uid:"",url:"",vulgar:""};

var species= window.localStorage.getItem("species");
var url = window.location.href;
var authorNode=document.createTextNode("");


//Goes through the Firebase Database and fetches the species photos
ref.once('value', function(snapshot){
  snapshot.forEach(function(child){
    speciesDesc=document.createTextNode(child.child('description').val());
    speciesEco=document.createTextNode(child.child('ecology').val());
    speciesVulg=document.createTextNode(child.child('vulgar').val());

    if(child.key.trim() == species){
      child.forEach(function(photos){
          if(photos.hasChild('species')){
              imgObject = {author:"",date:"",eco:"",location:"",species:"",uid:"",url:"",vulgar:""};
              speciesName = document.createTextNode(child.key.trim());
              photoAuthor = document.createTextNode(photos.child('author').val());

              spName=document.createTextNode(photos.child('species').val());
              spVulgar = document.createTextNode(photos.child('vulgar').val());

              nameChild = document.getElementById('name');
              descChild=document.getElementById('desc');
              ecoChild=document.getElementById('ecology');
              vulgChild=document.getElementById('vulgar');
              var img = new Image(250,250);

              var button = document.createElement("button");

              img.src=photos.child('url').val();

              img.id = photos.child('species').val();

              img.style.height='250px';
              img.style.width='250px';
              img.style.flex='50%';
              img.style.padding='4px';

              imgObject.url=img;
              imgLocation.latitude=photos.child('location').child('latitude').val();
              imgLocation.longitude=photos.child('location').child('longitude').val();
              imgObject.location=imgLocation;
              imgObject.author=photos.child('author').val();
              imgObject.vulgar=photos.child('vulgar').val();

              imgArray[i]=imgObject;
              i++;

        }

      });
      nameChild.parentNode.insertBefore(speciesName, nameChild);
      descChild.parentNode.insertBefore(speciesDesc, descChild);
      ecoChild.parentNode.insertBefore(speciesEco, ecoChild);
      vulgChild.parentNode.insertBefore(speciesVulg,vulgChild);

      document.getElementById("displayPrevImage").onclick= function(){
        displayPreviousImage(imgArray);
      };
      document.getElementById("displayNextImage").onclick=function(){
        displayNextImage(imgArray);
      };

      //displayNextImage(imgArray);
      window.setInterval(function(){
        displayNextImage(imgArray);
      }, 3000);
    }

  });

});

firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.
    var displayName = user.displayName;
    var email = user.email;
    var emailVerified = user.emailVerified;
    var photoURL = user.photoURL;
    var isAnonymous = user.isAnonymous;
    var uid = user.uid;
    var providerData = user.providerData;

    // ...
  } else {
    // User is signed out.
    // ...
  }

});

//Method that shows the image details
function showDetails(imgPosLat,imgPosLong,authorNode, vulgarName){
  // Get the modal
  var modal1 = document.getElementById('myModal');

  // When the user clicks the button, open the modal
  modal1.style.display = "block";

  // Get the <span> element that closes the modal
  var span1 = document.getElementsByClassName("close1")[0];

  // When the user clicks on <span> (x), close the modal
  span1.onclick = function() {
      modal1.style.display = "none";
      setMapOnAll(null);
  }

  // When the user clicks anywhere outside of the modal, close it
  window.onclick = function(event) {
      if (event.target == modal1) {
          modal1.style.display = "none";
          setMapOnAll(null);
      }
  }

  var myLatLng ={};
  myLatLng.lng=imgPosLong;
  myLatLng.lat=imgPosLat;


  var marker = new google.maps.Marker({
    position: myLatLng,
    map: map
  });
  markers.push(marker);

  google.maps.event.trigger(map, "resize");
  map.panTo(myLatLng);

  spVulgar = document.createTextNode(vulgarName);


  var nameM = document.getElementById("nameM");
  var authorM = document.getElementById("authorM");
  document.getElementById("vulgarM").textContent=vulgarName;

  authorM.parentNode.insertBefore(authorNode,authorM);
  nameM.parentNode.insertBefore(spName, nameM);
  //vulgarM.parentNode.insertBefore(spVulgar,vulgarM);

}

//Method that shows the edit Info modal
function editNewInfo(){
  // Get the modal
  var modal = document.getElementById('editData');

  // When the user clicks the button, open the modal
  modal.style.display = "block";

  // Get the <span> element that closes the modal
  var span = document.getElementsByClassName("close")[0];

  // When the user clicks on <span> (x), close the modal
  span.onclick = function() {
      modal.style.display = "none";
  }

  window.onclick = function(event) {
      if (event.target == modal) {
          modal.style.display = "none";
      }
    }
  }
//Method to tinker with the maps markers
function setMapOnAll(map) {
        for (var i = 0; i < markers.length; i++) {
          markers[i].setMap(map);
        }
      }

//This method updates the species info
function uploadNewInfo(){
  var editSpecies = firebase.database().ref('Species');

  var speciesNameN=document.getElementById("speciesN").value;
  var descriptionN=document.getElementById("descriptionN").value;
  var ecologyN=document.getElementById("ecologyN").value;
  var vulgarN=document.getElementById("vulgarN").value;

  //Copies all captures to the correct species
  editSpecies.child(species).on('value', function(snapshot){
    snapshot.forEach(function(child){
      if(child.key!="description" && child.key!="ecology" && child.key!="vulgar"){
        editSpecies.child(speciesNameN).child(child.key).set(child.val());
        editSpecies.child(speciesNameN).child(child.key).update({
          species:speciesNameN,
          eco:ecologyN,
          vulgar:vulgarN
        });
      }
    });
  });

  addToUser.once('value', function(user){
    user.forEach(function(userID){
      if(userID.child(species).exists()){
        userID.child(species).forEach(function (captures){
          if(captures.key!="description" && captures.key!="ecology" && captures.key!="vulgar"){
            addToUser.child(userID.key).child(speciesNameN).child(captures.key).set(captures.val());
            addToUser.child(userID.key).child(speciesNameN).child(captures.key).update({
              species:speciesNameN,
              eco:ecologyN,
              vulgar:vulgarN
            });
            addToUser.child(userID.key).child(speciesNameN).child("description").set(descriptionN);
            addToUser.child(userID.key).child(speciesNameN).child("vulgar").set(vulgarN);
            addToUser.child(userID.key).child(speciesNameN).child("ecology").set(ecologyN);
          }
        });
      }
      userID.child(species).ref.remove();
    });
  });



  //Gives the new species description, ecology and vulgar name
  editSpecies.child(speciesNameN).update({
    description:descriptionN,
    ecology:ecologyN,
    vulgar:vulgarN
  });

  //Removes the wrong species form the branch
  editSpecies.child(species).once('value', function(removeNode){
    removeNode.ref.remove();
  });

}

function goToBlog(){
    window.location = "http://localhost:8888/?s=" + species;
}

function displayNextImage(imgArray) {
    x = (x === imgArray.length - 1) ? 0 : x + 1;
    document.getElementById("imageCont").src = imgArray[x].url.src;

    document.getElementById('imageCont').onclick = function(){
      authorNode.textContent=imgArray[x].author;
      document.getElementById("smallImg").src=imgArray[x].url.src;
      showDetails(imgArray[x].location.latitude, imgArray[x].location.longitude, authorNode, authorNode,imgArray[x].vulgar);
    };

}

function displayPreviousImage(imgArray) {
    x = (x <= 0) ? imgArray.length - 1 : x - 1;
    document.getElementById("imageCont").src = imgArray[x].url.src;
    //document.getElementById("imageCont").src = imgArray[x].url.src;

    document.getElementById('imageCont').onclick = function(){
      authorNode.textContent=imgArray[x].author;
      document.getElementById("smallImg").src=imgArray[x].url.src;
      showDetails(imgArray[x].location.latitude, imgArray[x].location.longitude, authorNode,imgArray[x].vulgar);
    };
}
