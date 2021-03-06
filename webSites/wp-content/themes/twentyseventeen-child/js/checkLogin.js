firebase.auth().onAuthStateChanged(function(user){

  var ref = firebase.database().ref('Accounts');
  ref.once('value', function(snapshot){
        snapshot.forEach(function(child){
          if(child.key == user.uid){
            if(child.child("isPro").val()==true){
              document.getElementById("pending").style.display="block";
              document.getElementById("uploadPhoto").style.display="block";
              document.getElementById("uploadPhotoUser").style.display="none";
              document.getElementById("editSpecies").style.display="block";
            }else{
              document.getElementById("pending").style.display="none";
              document.getElementById("uploadPhoto").style.display="none";
              document.getElementById("uploadPhotoUser").style.display="block";
              document.getElementById("editSpecies").style.display="none";
            
            }
          }
        });
      });

  if(user){
      document.getElementById("login").style.display="none";

  }else{
    document.getElementById("logout").style.display="none";
    document.getElementById("userAccount").style.display="none";
    document.getElementById("uploadPhoto").style.display="none";
    document.getElementById("pending").style.display="none";
    document.getElementById("uploadPhotoUser").style.display="none";
  }

  window.localStorage.setItem("userLogged", user.email);
  window.localStorage.setItem("userUID", user.uid);
  window.localStorage.setItem("displayName", user.displayName);
  });
