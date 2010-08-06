package br.ueg.openodonto.filtro;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FiltroLogin implements Filter {

    public FiltroLogin() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    	HttpServletRequest httpRequest;
	if ((httpRequest = (HttpServletRequest) request).getSession().getAttribute(
		"usuarioSessao") == null) {
	    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
	    httpServletResponse.sendRedirect(httpRequest.getContextPath()+"/faces/login/login.xhtml");
	} else {
	    chain.doFilter(request, response);
	}
    }

    public void init(FilterConfig fConfig) throws ServletException {
    }

}
