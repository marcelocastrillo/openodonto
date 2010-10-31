package br.ueg.openodonto.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import br.ueg.openodonto.dominio.Usuario;

public class FiltroLogin implements Filter {

	public FiltroLogin() {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		if (httpRequest.getSession().getAttribute("usuarioSessao") == null) {
			Usuario usuario = new Usuario();
			usuario.setNome("Vinicius G G R");
			httpRequest.getSession().setAttribute("usuarioSessao", usuario);
			/*
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			httpServletResponse.sendRedirect(httpRequest.getContextPath()+ "/faces/login/login.xhtml");
			*/
		} else {
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
