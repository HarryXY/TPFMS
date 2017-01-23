package web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AdminDao;
import dao.CostDao;
import entity.Admin;
import entity.Cost;
import util.ImageUtil;

public class MainServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		String path = req.getServletPath();
		if("/findCost.do".equals(path)){
			//查询资费
			findCost(req,res);				
		}else if("/toAddCost.do".equals(path)){
			toAddCost(req,res);
		}else if("/addCost.do".equals(path)){
			addCost(req,res);
		}else if("/toUpdateCost.do".equals(path)){
			updateCost(req,res);
		}else if("/toModifyCost.do".equals(path)){
			modifyCost(req,res);
		}else if("/toLogin.do".equals(path)){
			toLogin(req,res);
		}else if("/toIndex.do".equals(path)){
			toIndex(req,res);
		}else if("/login.do".equals(path)){
			//检查登录
			login(req,res);
		}else if("/logout.do".equals(path)){
			logout(req,res);
		}else if("/createImg.do".equals(path)){
			createImg(req,res);
		}else if("/toDeleteCost.do".equals(path)){
			deleteCost(req,res);
		}
		else{		
			throw new RuntimeException("没有这个页面");
		}		
	}
	
	private void deleteCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		req.setCharacterEncoding("utf-8");
		String costId = req.getParameter("id");
		CostDao dao = new CostDao();
		dao.delete(costId);
		res.sendRedirect("findCost.do");
		
	}
	
	private void createImg(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		//生成验证码及图片
		Object[] objs = ImageUtil.createImage();
		//将验证码存入session
		String imgCode = (String)objs[0];
		req.getSession().setAttribute("imgCode", imgCode);
		//将图片输出给浏览器
		BufferedImage img = (BufferedImage)objs[1];
		//告诉浏览器输出的内容类型
		res.setContentType("image/png");
		//通过response获取的输出流，其输出的目标就是当前请求的浏览器
		OutputStream os = res.getOutputStream();
		ImageIO.write(img, "png", os);
		os.close();
	}
	
	private void logout(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		//销毁session
		req.getSession().invalidate();;
		//重定向到登录
		//getContextPath：获取项目名		
		res.sendRedirect(req.getContextPath()+"/toLogin.do");
		System.out.println(req.getContextPath());
	}
	
	private void login(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		req.setCharacterEncoding("utf-8");
		//接收表单数据
		String adminCode = req.getParameter("adminCode");
		//System.out.println(adminCode);
		String password = req.getParameter("password");
		String code = req.getParameter("code");
		//从session中获取生成的验证码
		HttpSession session = req.getSession();
		String imgCode = (String)session.getAttribute("imgCode");
		//比较验证码
		if(code == null || !code.equalsIgnoreCase(imgCode)){
			req.setAttribute("error", "验证码错误");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
			return;
		}
		//检查该数据
		AdminDao dao = new AdminDao();
		Admin a = dao.findByCode(adminCode);
		System.out.println(a);
		if(a==null){
			//账号不存在
			req.setAttribute("error", "账号不存在");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		}else if(!a.getPassword().equals(password)){
			//密码错误
			req.setAttribute("error", "密码错误");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		}else{
			//将账号存入cookie
			Cookie c = new Cookie("adminCode",adminCode);
			res.addCookie(c);
			//将账号存入session
			session.setAttribute("adminCode", adminCode);
			//登录成功
			res.sendRedirect("toIndex.do");
		}
	}
	
	private void toLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		//转发到:WEB-INF/main/login.jsp
		req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
	}
	
	private void toIndex(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		//转发到:WEB-INF/main/login.jsp
		req.getRequestDispatcher("WEB-INF/main/index.jsp").forward(req, res);
	}
	
	private void modifyCost(HttpServletRequest req, HttpServletResponse res) throws IOException{
		req.setCharacterEncoding("utf-8");
		String costId = req.getParameter("costId");
		String name = req.getParameter("name");
		String costType = req.getParameter("costType");
		String baseDuration = req.getParameter("baseDuration");
		String baseCost = req.getParameter("baseCost");
		String unitCost = req.getParameter("unitCost");
		String descr = req.getParameter("descr");
		
		Cost cost = new Cost();
		cost.setCostId(Integer.parseInt(costId));
		cost.setName(name);
		cost.setCostType(costType);
		cost.setDescr(descr);
		if(baseDuration!=null && !baseDuration.equals("")){
			cost.setBaseDuration(new Integer(baseDuration));
		}
		if(baseCost!=null && !baseCost.equals("")){
			cost.setBaseCost(new Double(baseCost));
		}
		if(unitCost!=null && !unitCost.equals("")){
			cost.setUnitCost(new Double(unitCost));
		}
		
		CostDao dao = new CostDao();
		dao.modify(cost);
		System.out.println(cost.getCostId()+","+cost.getName()+","+cost.getCostType()+","+cost.getDescr()+","+cost.getBaseCost());
		res.sendRedirect("findCost.do");
		
	}
	
	private void updateCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		CostDao dao = new CostDao();
		int id = Integer.parseInt(req.getParameter("id"));
		Cost cost = dao.findById(id);	
		//System.out.println("查询成功");
		req.setAttribute("cost", cost);
		req.getRequestDispatcher("WEB-INF/cost/update.jsp").forward(req, res);
	}
	
	private void findCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		CostDao dao = new CostDao();
		List<Cost> list = dao.findAll();
		
		req.setAttribute("costs", list);
		//当前：/netctoss/findCost.do
		//目标：/netctoss/WEB-INF/cost/find.jsp
		req.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(req, res);
	}
	
	private void toAddCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		req.getRequestDispatcher("WEB-INF/cost/add.jsp").forward(req, res);
	}
	
	private void addCost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		req.setCharacterEncoding("utf-8");
		String name = req.getParameter("name");
		String costType = req.getParameter("costType");
		String baseDuration = req.getParameter("baseDuration");
		String baseCost = req.getParameter("baseCost");
		String unitCost = req.getParameter("unitCost");
		String descr = req.getParameter("descr");
		
		Cost cost = new Cost();
		cost.setName(name);
		cost.setCostType(costType);
		cost.setDescr(descr);
		if(baseDuration!=null && !baseDuration.equals("")){
			cost.setBaseDuration(new Integer(baseDuration));
		}
		if(baseCost!=null && !baseCost.equals("")){
			cost.setBaseCost(new Double(baseCost));
		}
		if(unitCost!=null && !unitCost.equals("")){
			cost.setUnitCost(new Double(unitCost));
		}
		
		CostDao dao = new CostDao();
		dao.save(cost);
		
		res.sendRedirect("findCost.do");
	}
	
	public static void main(String[] args) {
		CostDao dao = new CostDao();
		Cost c = new Cost();
		c.setName("tarena");
		c.setBaseDuration(880);
		c.setBaseCost(88.0);
		c.setUnitCost(0.8);
		c.setDescr("tarena好优惠");
		c.setCostType("2");
		dao.save(c);
		System.out.println("插入成功");
	}

	
}
