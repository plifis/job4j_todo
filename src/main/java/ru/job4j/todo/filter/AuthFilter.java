package ru.job4j.todo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse respHttp = (HttpServletResponse) response;
        HttpServletRequest reqHttp = (HttpServletRequest) request;
        String uri = reqHttp.getRequestURI();
        if ((uri.endsWith("reg.do")) || (uri.endsWith("auth.do"))) {
            chain.doFilter(request, response);
            return;
        }

        if (reqHttp.getSession().getAttribute("user") == null) {
            respHttp.sendRedirect(reqHttp.getContextPath() + "/login.jsp");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
