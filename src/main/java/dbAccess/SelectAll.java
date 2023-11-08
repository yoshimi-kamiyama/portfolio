/**
 *
 */
package dbAccess;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import dao.ItemDao;
import dto.ItemDto;

/**
 * daoオブジェクトを用い、全体検索処理を行ってその結果によってjspでの表示を振り分ける
 */
public class SelectAll implements DBAccess {

	@Override
	public void execute(HttpServletRequest request) throws SQLException {
		ItemDao dao = null;

		try {
			dao = new ItemDao();
			ArrayList<ItemDto> list = dao.getItemsAll(request);

			if(list.size() > 0) {
				request.setAttribute("list", list);
			}else {
				request.setAttribute("message", "まだデータがありません");
			}

		}finally {
			if(dao != null) {
				dao.close();
			}
		}
	}

}
