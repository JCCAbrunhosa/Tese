var newlyCreated = false;
var isPro=false;


firebase.auth().onAuthStateChanged(function(user){
  if(user){
    if(newlyCreated){
        registerUser(user.uid);
      if(isPro){
        registerPro(user.uid);
      }
      newlyCreated=false;
      isPro=false;
    }
      document.getElementById("main-div").style.display="none";
      goToHome();
  }else{
  }
});

function login(){

  var email= document.getElementById("email").value;
  var password = document.getElementById("pwd").value;
  firebase.auth().setPersistence(firebase.auth.Auth.Persistence.LOCAL)
  .then(function() {
    // Existing and future Auth states are now persisted in the current
    // session only. Closing the window would clear any existing state even
    // if a user forgets to sign out.
    // ...
    // New sign-in will be persisted with session persistence.
    return firebase.auth().signInWithEmailAndPassword(email, password);
  })
  .catch(function(error) {
    // Handle Errors here.
    var errorCode = error.code;
    var errorMessage = error.message;
  });

  window.setTimeout(goToHome, 4000);
}

function logOut(){
  firebase.auth().signOut().then(function() {
    // Sign-out successful.
  }).catch(function(error) {
    // An error happened.
  });
  goToHome();
}

function goToHome(){
  window.location.href="/";
}

function register(){
  var email= document.getElementById("email").value;
  var password = document.getElementById("pwd").value;

  firebase.auth().createUserWithEmailAndPassword(email, password).then(function(user) {
    return user.updateProfile({displayName: document.getElementById("userName").value});
  }).catch(function(error){
    // Handle Errors here.
    var errorCode = error.code;
    var errorMessage = error.message;
    // ...
  });
  if(document.getElementById("checkPro").checked){
    isPro=true;
  }

  newlyCreated=true;
  window.setTimeout(goToHome, 2000);
}

function cancelRegister(){
  document.getElementById("login").style.display="block";
  document.getElementById("registar").style.display="block";
  document.getElementById("pro").style.display="none";
  document.getElementById("register").style.display="none";
  document.getElementById("userName").style.display="none";
  document.getElementById("cancelRegister").style.display="none";
  document.getElementById("email").value="";
  document.getElementById("pwd").value="";

}

function hideLogin(){
      document.getElementById("login").style.display="none";
      document.getElementById("registar").style.display="none";
      document.getElementById("pro").style.display="inline";
      document.getElementById("register").style.display="block";
      document.getElementById("userName").style.display="block";
      document.getElementById("cancelRegister").style.display="block";
}

function registerPro(uuid){
  firebase.database().ref('Accounts').child(uuid).child("isPro").set("true");
}

function registerUser(uuid){
  firebase.database().ref('Accounts').child(uuid).child("userName").set(document.getElementById("userName").value);
}
