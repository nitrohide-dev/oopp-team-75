package commons;

import lombok.Getter;
import lombok.Setter;

/**
 * Class to validate and create a board.
 */
public class CreateBoardModel {

	@Getter
	@Setter
	public String key;
	@Getter
	@Setter
	public String title;

	@Getter
	@Setter
	public String password;

	public CreateBoardModel(){} // for object mappers, please don't use.

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
		return true;
	}
}
