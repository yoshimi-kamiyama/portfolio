package dbAccess;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import dao.ItemDao;
import dto.UserDto;
import filter.Judge;



/**
 * DBAccessインターフェースを実装するデータ追加クラス<br>
 * DBへ入力したユーザーネーム,パスワードを追加する<br>
 */
public class SignUp implements DBAccess {

	@Override
	public void execute(HttpServletRequest request) throws SQLException {
		ItemDao dao = null;
		int n = 0;
		String userName = request.getParameter("userName");
		String passWord = request.getParameter("passWord");

		if(userName != null && !userName.isEmpty() && passWord != null && !passWord.isEmpty() && Judge.isNumber(passWord)) {
			UserDto dto = new UserDto();
			dto.setUserName(userName);
			dto.setPassWord(Integer.parseInt(passWord));

				try {
					dao = new ItemDao();
					n = dao.signUp(dto);
						if(n > 0) {
							request.setAttribute("message", "ユーザー登録が完了しました");
						}else {
							request.setAttribute("message", "ユーザー登録に失敗しました");
						}
				}finally {
					if(dao != null) dao.close();
				}
		}else {
			request.setAttribute("message", "入力が不正です");
		}
	}
}
