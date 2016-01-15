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

/**
 * if ((key < 48 || key > 57) && key!=46 && key!=44 && key!=45)
58-57 numeros
46 es el código del punto
44 de la coma y
45 del guión
 */

//permite solo numeros y puto decinmal
function isNumberAndDecimalKey(evt)  {
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	// 48 - 57  numeros
	// 44 - coma
	// 46 - punto
	// 8 - borrar
	if ((key < 48 || key > 57) && key!=46 && key!=45 && key!=8)
		return false;

	return true;
}
