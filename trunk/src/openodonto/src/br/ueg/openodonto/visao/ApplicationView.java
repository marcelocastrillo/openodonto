package br.ueg.openodonto.visao;

public interface ApplicationView {

	void      showOut();
	void      showAction();
	void      showPopUp(String msg);
	String    getPopUpMsg();
	boolean   displayMessages();
	boolean   displayPopUp();
	void      addResourceMessage(String key, String target);
	void      addResourceDynamicMenssage(String msg, String target);
	void      addLocalMessage(String key,String target,boolean targetParam);
	void      addLocalDynamicMenssage(String msg,String target, boolean targetParam);
	void      refresh();
	
}
