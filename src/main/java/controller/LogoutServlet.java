package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dbAccess.DBAccess;

/**
 * ログアウト時に呼び出されるサーブレット<br>
 * ・doGet...商品管理ページに遷移<br>
 * ・doPost...セッション破棄しログアウトする、成功時はlogout.jspに、失敗時は商品管理ページに戻る
 */

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static DBAccess dbAccess;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext context = getServletContext();
		RequestDispatcher dis = context.getRequestDispatcher("/manage.jsp");
		dis.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            // ログインページにリダイレクト
            response.sendRedirect("manageTop.html");
        } else {
            // ログアウト失敗メッセージを設定
            request.setAttribute("error", "ログアウトに失敗しました");
            // 前のページに戻る
            doGet(request, response);
        }

	}
}
