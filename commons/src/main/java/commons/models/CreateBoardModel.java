package commons.models;

/**
 * Class to validate and create a board.
 */
public class CreateBoardModel {

	public final String key;
	public final String title;
	public final String password;

	public CreateBoardModel(String key, String title, String password) {
		this.key = key;
		this.title = title;
		this.password = password;
	}

	/**
	 * Checks if the given model is valid
	 * @return true if all requirements are met, false otherwise
	 */
	public boolean isValid() {
		if (key == null)
			return false;
		if (title == null)
			return false;
		if (password == null)
			return false;
//		if (key.length() > Board.MAX_KEY_LENGTH)
//			return false;
//		if (title.length() > Board.MAX_TITLE_LENGTH)
//			return false;
//		if (password.length() > Board.MAX_PASSWORD_LENGTH)
////			return false;
//		for (char c : key.toCharArray())
//			if (!Board.KEY_CHARS_SET.contains(c))
//				return false;
		return true;
	}
}
