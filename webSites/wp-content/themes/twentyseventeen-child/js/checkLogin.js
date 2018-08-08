firebase.auth().onAuthStateChanged(function(user){

  if(user){
      document.getElementById("login").style.display="none";
  }else{
    document.getElementById("logout").style.display="none";
    document.getElementById("userAccount").style.display="none";
  }
  });
