package br.ueg.openodonto.util;

import java.util.Map;

import javax.el.MethodExpression;

import org.richfaces.component.html.HtmlModalPanel;

public interface DialogoConfirmacaoIF {

	public abstract void renderConfirmDialog(Map<String, String> params,
			Map<String, String> values);

	public abstract HtmlModalPanel getModal();

	public abstract void setModal(HtmlModalPanel modal);

	public abstract Map<String, MethodExpression> getActionsForDialogModal();

	public abstract void setActionsForDialogModal(
			Map<String, MethodExpression> actionsForDialogModal);

	public abstract String getForm();

	public abstract void setForm(String form);

	public abstract String getTitle();

	public abstract void setTitle(String title);

}