function digitarNumero(campo, e, dec) {
	var key = window.event ? window.event.keyCode : e.which;
	if (key == null)
		return false;
	var keychar = String.fromCharCode(key);
	if ((key == null) || (key == 0) || (key == 8) || (key == 9) || (key == 27) || (key == 47)) {
		return true;
	} else if ((("0123456789").indexOf(keychar) > -1)) {
		return true;
	} else if (dec && (keychar == ".")) {
		campo.form.elements[dec].focus();
		return false;
	} else {
		return false;
	}
}
function exibirMensagem(msg){
	alert(msg);
}
function limparCPF(cpf){
	cpf = cpf.replace(/-/g,"");
	cpf = cpf.replace(/[.]/g,"");
	cpf = cpf.replace(/_/g,"");
	cpf = cpf.replace(/[ ]/g,"");
	return cpf;
}

function doEnter(e) {  
	var key=e.keyCode || e.which;
	return key==13;
 }  

function dataTabeMouseOut(row,color){
	row.style.backgroundColor=color;
	row.style.cursor='default';	
}

function dataTableMouseClick(row,e){
	selectBox = row.cells.item(0).firstChild;
	source =  e.target || e.srcElement;
	if(source != selectBox){
		associar = selectBox.checked;
		selectBox.checked = !associar;
	}
}

function dataTabeMouseOver(row){
	row.style.backgroundColor='#F1F1F1';
	row.style.cursor='pointer';	
}
function restoreBg(dom,img){
	dom.src = img;
}
function align_div(element,position){
	jQuery(element).css('text-align',position);
}