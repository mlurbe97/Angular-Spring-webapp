package com.mlurbe.storage.web.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class CorsFilter extends GenericFilterBean {

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8081");// "*");//
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");// "*");//
        response.setHeader("Access-Control-Allow-Headers", "X-AUTH-TOKEN, Content-Type, Authorization");
        response.setHeader("Access-Control-Expose-Headers", "X-AUTH-TOKEN");// "*");//
        response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Max-Age", "3600");
        // response.setHeader("Access-Control-Allow-Headers", "*");// "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type,
                                                                // Access-Control-Request-Method, Access-Control-Request-Headers");
		if (request.getMethod().toUpperCase().trim().equals("OPTIONS")) {

			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().flush();
			response.getWriter().close();
			return;
		}

		chain.doFilter(request, response);
	}
}
