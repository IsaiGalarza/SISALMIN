function validateOnlyNumber(e){
	tecla = (document.all) ? e.keyCode : e.which;

	//Tecla de retroceso para borrar, siempre la permite
	if (tecla==8){
		return true;
	}

	// Patron de entrada, en este caso solo acepta numeros
	patron =/[0-9]/;
	tecla_final = String.fromCharCode(tecla);
	return patron.test(tecla_final);
}

function validateOnlyPrecio(e){
	tecla = (document.all) ? e.keyCode : e.which;

	//Tecla de retroceso para borrar, siempre la permite
	if (tecla==8){
		return true;
	}

	// Patron de entrada, en este caso solo acepta numeros
	patron =/[0-9]/;
	tecla_final = String.fromCharCode(tecla);
	return patron.test(tecla_final);
}

function permiteKey(e,t){
	tecla = (document.all) ? e.keyCode : e.which;
	//Tecla de retroceso para borrar
	if (tecla==8){
		return false;
	}
	return false;
}


//permite solo numeros y puto decinmal
/**
       function isNumberAndDecimalKey(evt)
       {
          var charCode = (evt.which) ? evt.which : evt.keyCode;
          if (charCode != 46 && charCode > 31 
            && (charCode < 48 || charCode > 57))
             return false;

          return true;
       }
 **/