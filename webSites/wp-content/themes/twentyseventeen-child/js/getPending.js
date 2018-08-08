
var ref = firebase.database().ref('toReview');

ref.once('value', function(snapshot){
  snapshot.forEach(function(child){

            var img = new Image(250,250);
            var overallImage =
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
            img.onclick=showOptions;


            document.getElementById('image').appendChild(img);

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


function showOptions(){
  // Get the modal
  var modal = document.getElementById('anotherModal');

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
  //alert(smallImg);
}
