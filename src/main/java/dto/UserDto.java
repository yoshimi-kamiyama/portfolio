package dto;

/**
 * ユーザーデータを保持するクラス<br>
 * ユーザー検索時のデータ管理やデータ登録時の際にも使用され、以下のフィールドを持つ<br>
 * ・ユーザーネーム<br>
 * ・パスワード<br>
 * @author user
 *
 */

public class UserDto {
	/**
	 * ユーザーネーム
	 */
	String userName;

	/**
	 * パスワード
	 */
	int passWord;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPassWord() {
		return passWord;
	}

	public void setPassWord(int passWord) {
		this.passWord = passWord;
	}
}