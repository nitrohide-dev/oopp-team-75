package commons.models;

/**
 * Model for better communication with the server when creating board
 */
@SuppressWarnings("SpellCheckingInspection")
public class CreateBoardModel {
	public static final int MAX_KEY_LENGTH = 16;
	public static final char[] KEY_CHARS = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!-._ ")
		.toCharArray();
	public static final int MAX_TITLE_LENGTH = 64;

	public final String key;
	public final String name;
	public final boolean locked;

	/**
	 * Constructor for the model
	 * @param key - The key for the board
	 * @param name - The name for the board
	 * @param locked - Boolean that shows if the board is visible for all users or no
	 */
	public CreateBoardModel(String key, String name, boolean locked) {
		this.key = key;
		this.name = name;
		this.locked = locked;
	}

	/**
	 * Checks if the given model is valid
	 * @return true if all requirements are met, false otherwise
	 */
	public boolean isValid() {
		if (key == null) return false;
		if (name.length() > MAX_TITLE_LENGTH) return false;
		if (key.length() > MAX_KEY_LENGTH) return false;
		outer: for (char c1 : key.toCharArray()) {
			for (char c2 : KEY_CHARS) {
				if (c1 == c2) continue outer;
			} return false;
		} return true;
	}
}
