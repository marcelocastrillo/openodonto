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

function doEnter(event) {  
    var keynum;           
    if(window.event) {  
        keynum = event.keyCode;  
    }else if(event.which){  
        keynum = event.which;  
    }  
    if( keynum==13 ){  
   	 return true;
    }
    return false;  
 }  
