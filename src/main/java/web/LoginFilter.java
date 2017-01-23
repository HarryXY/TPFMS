package web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	private String[] paths;
	
	public void destroy() {		

	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		//Tomcat调用此方法时传入的是Http开头的对象，是当前参数的子类型
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		HttpSession session = request.getSession();
		
		//排除掉不需要过滤的请求
//		String[] paths = new String[]{
//			"/toLogin.do",
//			"/login.do",
//			"/createImg.do"
//		};
		for(String path : paths){
			if(path.equals(request.getServletPath())){
				//若当前路径等于某排除路径，则本次请求不需要过考虑，请求继续执行
				chain.doFilter(request, response);
				return;
			}
		} 
		//从session中获取账号
		String adminCode = (String)session.getAttribute("adminCode");
		//判断该账号是否为空值
		if(adminCode==null){
			//重定向到登录页面
			response.sendRedirect(request.getContextPath()+"/toLogin.do");
		}else{
			//非空，已登录，请求继续执行
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig cfg) throws ServletException {
		
		String ipath = cfg.getInitParameter("ignorePath");
		paths = ipath.split(",");
	}

}
