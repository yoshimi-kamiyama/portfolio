package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dto.ItemDto;
import dto.UserDto;

/**
 * DBとの接続、操作、切断を処理するクラス<br>
 * Dao...Data Access Objectの略
 * @author user
 *
 */
public class ItemDao {

	private Connection con;
	private String sql;
	PreparedStatement ps = null;
	ResultSet rs = null;
	ArrayList<ItemDto> list = null;

	/**
	 * testdbに接続するためのコンストラクタ
	 * @throws SQLException
	 */

	public ItemDao() throws SQLException{
		String url = "jdbc:mysql://localhost:3306/javaexam?serverTimezone=UTC";
		String user = "root";
		String pass = "root";
		con = DriverManager.getConnection(url, user, pass);
		System.out.println("Connection success!");
	}


	/**
	 * DB接続を切断する
	 */
	public void close() {
		try {
			if(con != null) con.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * ログイン時のデータ照合、成功時にはセッションにusernameを保存
	 * @param name 名前
	 * @param pass　パスワード
	 * @return ログイン成功時...1 <br>ログイン失敗時...0
	 * @throws SQLException
	 */

	public int getLoginInfo(String name, String pass, HttpServletRequest request) throws SQLException{
		sql = "select * from user where name = ? and password = ?";
		ps = con.prepareStatement(sql);
		ps.setString(1, name);
		ps.setString(2, pass);

		try {
			rs = ps.executeQuery();
			rs.next();
			// ログイン成功の場合
			if (rs.getRow() > 0) {
				// ユーザー名をセッションに保存
				HttpSession session = request.getSession();
				String userName = name; // ログインしたユーザー名
				session.setAttribute("loggedInUser", userName);
			}
			return rs.getRow();
		}finally {
			ps.close();
		}
//		return rs.getRow();
	}
	
	/**
	 * 新規アカウント登録
	 * @param dto (パラメータをまとめてもつオブジェクト)
	 * @return 成功件数
	 * @throws SQLException
	 */

	public int signUp(UserDto dto) throws SQLException{
		sql = "INSERT INTO user (name, password) VALUES (?, ?)";
		int n = 0;

		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, dto.getUserName());
			ps.setInt(2, dto.getPassWord());
			n = ps.executeUpdate();
		}finally {
			ps.close();
		}
		return n;
	}

	/**
	 * DBから全データを抽出する
	 * @return 全データを保持するリスト
	 * @throws SQLException
	 */

	public ArrayList<ItemDto> getItemsAll(HttpServletRequest request) throws SQLException{
		HttpSession session = request.getSession();
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
		
		sql = "select * from item where user_id = (select id from user where name = ?)";
		ps = con.prepareStatement(sql);
		ps.setString(1, loggedInUser);
		
		return search(ps);
	}

	/**
	 * DBから1レコードを抽出する(商品コードで検索)
	 * @param code 抽出したい商品コード
	 * @return 該当データ
	 * @throws SQLException
	 */
	public ItemDto getItem(int code, HttpServletRequest request) throws SQLException{
		HttpSession session = request.getSession();
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
	    
		sql = "select * from item where code = ? and user_id = (select id from user where name = ?)";
		ps = con.prepareStatement(sql);
		ps.setInt(1, code);
		ps.setString(2, loggedInUser);
		return search(ps).get(0);
	}

	/**
	 * DBから名前検索
	 * @param name 商品名
	 * @return 名前検索にヒットしたデータを持つリスト
	 * @throws SQLException
	 */
	public ArrayList<ItemDto> getItemsFromName(String name, HttpServletRequest request) throws SQLException{
		HttpSession session = request.getSession();
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
		
		sql = "select * from item where name like ? and user_id = (select id from user where name = ?)";
		ps = con.prepareStatement(sql);
		ps.setString(1, "%" + name + "%");
		ps.setString(2, loggedInUser);
		return search(ps);
	}

	/**
	 * DBからカテゴリ検索
	 * @param category カテゴリ名
	 * @return カテゴリ検索にヒットしたデータを持つリスト
	 * @throws SQLException
	 */
	public ArrayList<ItemDto> getItemsFromCategroy(String category, HttpServletRequest request) throws SQLException{
		HttpSession session = request.getSession();
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
		
		sql = "select * from item where category = ? and user_id = (select id from user where name = ?)";
		ps = con.prepareStatement(sql);
		ps.setString(1, category);
		ps.setString(2, loggedInUser);
		return search(ps);
	}

	/**
	 * DBから範囲指定の値段検索
	 * @param p1 最低値
	 * @param p2 最高値
	 * @return 値段検索にヒットしたデータを持つリスト
	 * @throws SQLException
	 */
	public ArrayList<ItemDto> getItemsFromPrice(int p1, int p2, HttpServletRequest request) throws SQLException{
		HttpSession session = request.getSession();
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
		
		sql = "select * from item where price between ? and ? and user_id = (select id from user where name = ?)";
		ps = con.prepareStatement(sql);
		ps.setInt(1, p1);
		ps.setInt(2, p2);
		ps.setString(3, loggedInUser);
		return search(ps);
	}

	/**
	 * DBから10,000円以上の値段検索
	 * @return 検索にヒットしたデータを持つリスト
	 * @throws SQLException
	 */
	public ArrayList<ItemDto> getItemsMoreThan10000(HttpServletRequest request) throws SQLException{
		HttpSession session = request.getSession();
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
		
		sql = "select * from item where price >= 10000 and user_id = (select id from user where name = ?)";
		ps = con.prepareStatement(sql);
		ps.setString(1, loggedInUser);
		return search(ps);
	}

	/**
	 * select文を実行するメソッド
	 * @param ps パラメータがセットされたSQLを持つオブジェクト
	 * @return 検索にヒットしたデータを持つリスト
	 * @throws SQLException
	 */

	private ArrayList<ItemDto> search(PreparedStatement ps) throws SQLException{
		try {
			rs = ps.executeQuery();
			list = new ArrayList<>();

			ItemDto dto;

			while(rs.next()) {
				dto = new ItemDto();
				dto.setId(rs.getInt("id"));
				dto.setCode(rs.getInt("code"));
				dto.setName(rs.getString("name"));
				dto.setCategory(parseCategory(rs.getString("category")));
				dto.setPrice(rs.getInt("price"));
				list.add(dto);
			}
		}finally {
			ps.close();
		}
		return list;
	}

	/**
	 * 画面から受け取ったデータをDBに挿入するメソッド
	 * @param dto (パラメータをまとめてもつオブジェクト)
	 * @return 成功件数
	 * @throws SQLException
	 */

	public int insert(ItemDto dto, HttpServletRequest request) throws SQLException{
		HttpSession session = request.getSession();
	    String loggedInUser = (String) session.getAttribute("loggedInUser");
		
		sql = "INSERT INTO item (code, name, category, price, user_id) VALUES (?, ?, ?, ?, (SELECT id FROM user WHERE name = ?))";
		int n = 0;
		
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, dto.hashCode());
			ps.setString(2, dto.getName());
			ps.setString(3, dto.getCategory());
			ps.setInt(4, dto.getPrice());
			ps.setString(5, loggedInUser);
			n = ps.executeUpdate();
		}finally {
			ps.close();
		}
		return n;
	}

	/**
	 * DBへの更新処理
	 * @param dto 既存商品の更新情報を持つオブジェクト
	 * @return 成功件数
	 * @throws SQLException
	 */

	public int update(ItemDto dto) throws SQLException{
		sql = "update item set name = ?, category = ?, price = ? where code = ?";
		int n = 0;
		
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, dto.getName());
			ps.setString(2, dto.getCategory());
			ps.setInt(3, dto.getPrice());
			ps.setInt(4, dto.getCode());
			n = ps.executeUpdate();
		}finally {
			ps.close();
		}
		
		return n;
	}

	/**
	 * DBへの削除処理
	 * @param code 商品コード
	 * @return 成功件数
	 * @throws SQLException
	 */

	public int delete(int code) throws SQLException{
		sql = "delete from item where code = ?";
		int n = 0;

		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, code);
			n = ps.executeUpdate();
		}finally {
			ps.close();
		}

		return n;
	}

	/**
	 * カテゴリを変換するメソッド
	 * @param category DB用カテゴリ名
	 * @return 画面用カテゴリ名
	 */
	private String parseCategory(String category) {

		String ctgr = null;

		switch(category) {
		case "general":
			ctgr = "雑貨";
			break;
		case "electric":
			ctgr = "家電";
			break;
		case "book":
			ctgr = "書籍";
			break;
		case "food":
			ctgr = "食品";
			break;
		case "fashion":
			ctgr = "ファッション";
			break;
		default:
			ctgr = "未分類";
		}

		return ctgr;
	}
}
