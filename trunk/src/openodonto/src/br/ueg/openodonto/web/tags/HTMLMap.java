package br.ueg.openodonto.web.tags;

import java.io.IOException;

import javax.faces.component.UIComponentBase
;
import javax.faces.context.FacesContext;

public class HTMLMap extends UIComponentBase{

    @Override
    public String getFamily() {
        return null;
    }
    
    @Override
    public void encodeBegin(FacesContext ctx) throws IOException {
        super.encodeBegin(ctx);
    }
    
    @Override
    public void encodeEnd(FacesContext ctx) throws IOException {
        super.encodeEnd(ctx);
    }    

}
