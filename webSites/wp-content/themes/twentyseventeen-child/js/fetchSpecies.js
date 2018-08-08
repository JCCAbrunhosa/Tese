
var ref = firebase.database().ref('Species');
var t;

ref.once('value', function(snapshot){
  snapshot.forEach(function(child){
        var btn = document.createElement("button");
        var t = document.createTextNode(child.key);
        btn.id=child.key;
        btn.className="button";

        btn.appendChild(t);

        document.getElementById('buttonList').appendChild(btn);
        document.getElementById(child.key).onclick = function () {
              window.localStorage.setItem("species", child.key);
               window.location = "http://localhost:8888/galeria/";

           };

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
