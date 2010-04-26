package br.ueg.openodonto.controle;

import java.util.Map;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlOutputText;

import org.ajax4jsf.component.html.HtmlAjaxCommandButton;
import org.richfaces.component.html.HtmlModalPanel;

import br.ueg.openodonto.util.DialogoConfirmacaoIF;

public class DialogoConfirmacaoJSF implements DialogoConfirmacaoIF{
	
	private String form;
	private String title;
	private HtmlModalPanel modal;
	private Map<String,MethodExpression> actionsForDialogModal;
	
	public DialogoConfirmacaoJSF(){
		this("formDialog" , "titleDialog");
	}
	
	public DialogoConfirmacaoJSF(String form,String title){
		setForm(form);
		setTitle(title);
	}
	
	/* (non-Javadoc)
	 * @see br.ueg.openodonto.visao.DialogoConfirmacaoIF#renderConfirmDialog(java.util.Map, java.util.Map)
	 */
	public void renderConfirmDialog(Map<String,String> params,Map<String,String> values){						
			
		/* Manipulando o titulo do dialogo */
		HtmlOutputText htlmTitle = (HtmlOutputText)modal.findComponent(getTitle());
		htlmTitle.setValue(values.get("title"));
						
		/*Manipulando a mensagem do dialogo*/
		HtmlOutputText htlmMessage = (HtmlOutputText)modal.findComponent(getForm() + ":messageDialog");
		htlmMessage.setValue(values.get("message"));
			
		/*Manipulando o botao 'sim' do dialogo*/
		HtmlAjaxCommandButton btYes  = (HtmlAjaxCommandButton)modal.findComponent(getForm() + ":yesButton");
		btYes.setValue(values.get("yes"));
		if(values.get("acaoYes") != null)
			btYes.setActionExpression(actionsForDialogModal.get(values.get("acaoYes")));
		if(values.get("reRenderWhenOk") != null)
			btYes.setReRender(btYes.getReRender() + "," + values.get("reRenderWhenOk"));	
				
		/*Manipulando o botao 'nao' do dialogo*/
		HtmlAjaxCommandButton btNo = (HtmlAjaxCommandButton)modal.findComponent(getForm() + ":noButton");
		btNo.setValue(values.get("no"));
		if(values.get("acaoNo") != null)
			btNo.setActionExpression(actionsForDialogModal.get(values.get("acaoNo")));
						
		/*Manipulando parametros*/
		if(params != null)	
			for(String key : params.keySet()){
				UIComponent component = modal.findComponent(getForm() + ":"+key);
				if((component != null) &&  (component instanceof UIParameter)){
					UIParameter param = (UIParameter)component;
					if( param != null){
						param.setName(key);
						param.setValue(params.get(key));
					}
				}
			}
	}
	
	/* (non-Javadoc)
	 * @see br.ueg.openodonto.visao.DialogoConfirmacaoIF#getModal()
	 */
	public HtmlModalPanel getModal() {
		return modal;
	}

	/* (non-Javadoc)
	 * @see br.ueg.openodonto.visao.DialogoConfirmacaoIF#setModal(org.richfaces.component.html.HtmlModalPanel)
	 */
	public void setModal(HtmlModalPanel modal) {
		this.modal = modal;
	}

	
	/* (non-Javadoc)
	 * @see br.ueg.openodonto.visao.DialogoConfirmacaoIF#getActionsForDialogModal()
	 */
	public Map<String, MethodExpression> getActionsForDialogModal() {
	
		return this.actionsForDialogModal;
	}

	
	/* (non-Javadoc)
	 * @see br.ueg.openodonto.visao.DialogoConfirmacaoIF#setActionsForDialogModal(java.util.Map)
	 */
	public void setActionsForDialogModal(
			Map<String, MethodExpression> actionsForDialogModal) {
	
		this.actionsForDialogModal = actionsForDialogModal;
	}

	
	/* (non-Javadoc)
	 * @see br.ueg.openodonto.visao.DialogoConfirmacaoIF#getForm()
	 */
	public String getForm() {
	
		return this.form;
	}

	
	/* (non-Javadoc)
	 * @see br.ueg.openodonto.visao.DialogoConfirmacaoIF#setForm(java.lang.String)
	 */
	public void setForm(String form) {
	
		this.form = form;
	}

	
	/* (non-Javadoc)
	 * @see br.ueg.openodonto.visao.DialogoConfirmacaoIF#getTitle()
	 */
	public String getTitle() {
	
		return this.title;
	}

	
	/* (non-Javadoc)
	 * @see br.ueg.openodonto.visao.DialogoConfirmacaoIF#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
	
		this.title = title;
	}
	
	
	
	
}
