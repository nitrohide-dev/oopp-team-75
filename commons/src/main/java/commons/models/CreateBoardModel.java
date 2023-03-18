package commons.models;

public class CreateBoardModel {
	public static final int MAX_KEY_LENGTH = 16;
	public static final char[] KEY_CHARS = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!-._ ").toCharArray();
	public static final int MAX_TITLE_LENGTH = 64;

	public final String key;
	public final String name;
	public final boolean locked;

	public CreateBoardModel(String key, String name, boolean locked) {
		this.key = key;
		this.name = name;
		this.locked = locked;
	}

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
