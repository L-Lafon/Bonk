function myFunction(){
			var pseudo = document.getElementById('pseudo'),
			radios = document.getElementsByName('radi'),
			text = "";
			
			for (var i = 0, length = radios.length; i < length; i++) {
			    if (radios[i].checked) {
			        // do whatever you want with the checked radio
			        text += radios[i].value;
			        // only one radio can be logically checked, don't check the rest
			        
			    }
			    
			}
			if (text != "" && pseudo.value!=""){
				document.formu.submit();
			}
			else{
				alert("Merci de remplir correctement les champs du formulaire")
			}
		}