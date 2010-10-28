package br.ueg.openodonto.web.tags;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.FaceletException;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagHandler;

public class Map extends TagHandler{

    private final TagAttribute id;
    private final TagAttribute name;
    
    public Map(TagConfig config) {
        super(config);
        this.id = this.getRequiredAttribute("id");
        this.name = this.getRequiredAttribute("name");
    }

    @Override
    public void apply(FaceletContext ctx, UIComponent parent)  throws IOException, FacesException, FaceletException, ELException {
        String id = (String)this.id.getObject(ctx);
        String name = (String)this.name.getObject(ctx);
    }

}
