
var ref = firebase.database().ref('toReview');
var addToSpecies = firebase.database().ref('Species');

var speciesAvailable=[];
var imgArray=[];
var i=0;

var imgLocation={latitude:"",longitude:""};
var imgObject = {author:"",date:"",eco:"",location:"",species:"",uid:"",url:"",vulgar:""};

ref.once('value', function(snapshot){
  snapshot.forEach(function(child){

            var img = new Image(250,250);
            smallImg = new Image(150,150);

            var button = document.createElement("button");

            img.src=child.child('url').val();

            imgArray[i]= new Image();
            imgArray[i].src=child.child('url').val();
            imgArray[i].style.width='100%';
            imgArray[i].style.height='100%';
            imgArray[i].style.flex='50%';
            i++;
            smallImg.src=child.child('url').val();
            img.id = child.child('species').val();

            img.style.height='250px';
            img.style.width='250px';
            img.style.flex='50%';
            img.style.padding='4px';



            document.getElementById('image').appendChild(img).onclick = function(){
              showOptions(img);
              document.getElementById("imageToDocument").src=img.src;
              imgLocation.latitude=child.child('location').child('latitude').val();
              imgLocation.longitude=child.child('location').child('longitude').val();
              imgObject.author=child.child('author').val();
              imgObject.date=child.child('date').val();
              imgObject.uid=child.child('uid').val();
            };


        });

      });
  document.getElementById('overallImage').appendChild(imgArray[0]);


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


function showOptions(img){
  // Get the modal

  var modal = document.getElementById('anotherModal');
  imgObject.url=img.src;
  imgObject.location=imgLocation;

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

  var speciesInput = document.getElementById('speciesName');

  addToSpecies.once('value', function(snapshot){
    snapshot.forEach(function(child){
      speciesAvailable.push(child.key);
      speciesInput.addEventListener('input',function(){
        jQuery( function() {
          jQuery( "#speciesName" ).autocomplete({
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




  document.getElementById('Guardar').onclick = function() {
      //Upload information to Firebase database
      var reviewedPhotos = firebase.database().ref('PhotosReviewed'); //This is going to add the photo to the reviewed group
      var addToUser = firebase.database().ref('Users'); //This is going to add the photo to the user portfolio
      var addToSpecies = firebase.database().ref('Species'); //This is going to add to an existing species or create a new one

      imgObject.eco=document.getElementById('speciesEcology').value;
      imgObject.species=document.getElementById('speciesName').textContent;
      imgObject.vulgar=document.getElementById('speciesVulgar').value;

      //Adds photo to Species tree
      addToSpecies.child(document.getElementById('speciesName').textContent).child(imgObject.date).set({
        author: imgObject.author,
        date: imgObject.date,
        eco: imgObject.eco,
        location: imgLocation,
        species: imgObject.species,
        uid: imgObject.uid,
        url: img.src,
        vulgar: imgObject.vulgar
      });

      //Add description to the species branch
      if(document.getElementById("speciesDescription").value.trim()!="Já existe"){
        addToSpecies.child(document.getElementById('speciesName').textContent).child("description").set(document.getElementById("speciesDescription").value);
        addToSpecies.child(document.getElementById('speciesName').textContent).child("ecology").set(imgObject.eco);
        addToSpecies.child(document.getElementById('speciesName').textContent).child("vulgar").set(imgObject.vulgar);
        addToUser.child(imgObject.uid).child(document.getElementById('speciesName').textContent).child("description").set(document.getElementById("speciesDescription").value);
        addToUser.child(imgObject.uid).child(document.getElementById('speciesName').textContent).child("ecology").set(imgObject.eco);
        addToUser.child(imgObject.uid).child(document.getElementById('speciesName').textContent).child("vulgar").set(imgObject.vulgar);
      }
      //Adds photo to Users tree
      addToUser.child(imgObject.uid).child(document.getElementById('speciesName').textContent).child(imgObject.date).set({
        author: imgObject.author,
        date: imgObject.date,
        eco: imgObject.eco,
        location: imgLocation,
        species: imgObject.species,
        uid: imgObject.uid,
        url: img.src,
        vulgar: imgObject.vulgar
      });

      addToUser.child(document.getElementById('speciesName').textContent).once("value", function(data){
        addToUser.child(imgObject.uid).child(document.getElementById('speciesName').textContent).child(imgObject.date).update({
          eco: data.child("ecology").val(),
          vulgar: data.child("vulgar").val()

      });
    });

      //remove from the ToReview Tree on the User branch
      addToUser.child(imgObject.uid).child("ToReview").child(imgObject.date).remove();

      //Adds photo to PhotosReviewed tree
      reviewedPhotos.child(imgObject.date).set({
        author: imgObject.author,
        date: imgObject.date,
        eco: imgObject.eco,
        location: imgLocation,
        species: imgObject.species,
        uid: imgObject.uid,
        url: img.src,
        vulgar: imgObject.vulgar
      });

      ref.child(imgObject.date).remove();

      //Reloads the page
      location.reload();
  }

  document.getElementById("Cancelar").onclick=function(){
    ref.child(imgObject.date).remove();
    firebase.database().ref('Users').child(imgObject.uid).child("ToReview").child(imgObject.date).remove();
    location.reload();
  }
}
