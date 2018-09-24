firebase.auth().onAuthStateChanged(function(user){

    if(user){
        getDetails(user);
    }else{
    }
  });

  function getDetails(user){

    var ref = firebase.database().ref('Users');

    document.getElementById("email").textContent=user.email;

    ref.once('value', function(snapshot){
      snapshot.forEach(function(child){
        if(child.key == user.uid){
          child.forEach(function(species){
            species.forEach(function(images){
              var img = new Image(250,250);
              img.src=images.child('url').val();
              img.id = images.child('species').val();
              img.style.height='250px';
              img.style.width='250px';
              img.style.flex='50%';
              img.style.padding='4px';

              document.getElementById('ownPics').appendChild(img);
            });

          });
        }
      });


    });
  }
