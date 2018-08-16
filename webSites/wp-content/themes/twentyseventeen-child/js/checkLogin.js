firebase.auth().onAuthStateChanged(function(user){

  var ref = firebase.database().ref('Accounts');
  ref.once('value', function(snapshot){
        snapshot.forEach(function(child){

          if(child.key == user.uid && child.child('isPro').val() != true){
            document.getElementById("pending").style.display="none";
          }
        });
      });

  if(user){
      document.getElementById("login").style.display="none";


  }else{
    document.getElementById("logout").style.display="none";
    document.getElementById("userAccount").style.display="none";
    document.getElementById("uploadPhoto").style.display="none";
  }

  window.localStorage.setItem("userLogged", user.email);
  window.localStorage.setItem("userUID", user.uid);
  });
