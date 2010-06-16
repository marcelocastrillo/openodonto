function digitarNumero(campo, e, dec)
{

       var key;

       var keychar;    

               

       if (window.event)
           key = window.event.keyCode;
       else if (e)
           key = e.which;
       else
    	   return false;
     

       keychar = String.fromCharCode(key);    

   //alert(("0123456789").indexOf(keychar));

       // control keys

       if ((key==null) || (key==0) || (key==8) || (key==9) || (key==27) || (key==47))

               return true;



       // numbers

       else if ((("0123456789").indexOf(keychar) > -1))

               return true;



       // decimal point jump

       else if (dec && (keychar == "."))

       {

               campo.form.elements[dec].focus();

               return false;

       }

       else

               return false;

}
