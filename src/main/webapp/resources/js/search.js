function filter (term, _id, cellNr){
	var suche = term.value.toLowerCase();
	//var table =  $("#form\\:docs");// document.getElementById('#form\\jsftags:docs');
	var element1 = $("#form\\:docs");
	// or
	var element2 = $("[id='form:docs:j_idt33']");//form:docs:j_idt33
	// or
	var element3 = $(document.getElementById("form:docs"));
	var element4 = $("#form\\:docs:j_idt39_clone");
	var table = $('#form\\:docs_data');//form:docs_data   ///*[@id="form:docs"]/div[2]/table
	var ele;
	alert('alert: '+element4.toString());//element1 +' - 2: '+element2+' - 3: '+element3);
	for (var r = 1; r < table.rows.length; r++){
		ele = table.rows[r].cells[cellNr].innerHTML.replace(/<[^>]+>/g,"");
		if (ele.toLowerCase().indexOf(suche)>=0 )
			table.rows[r].style.display = '';
		else table.rows[r].style.display = 'none';
	}
}