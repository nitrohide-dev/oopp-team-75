package commons.models;

import commons.Board;

/**
 * Model for better communication with the server when creating taskList
 */
public class CreateListModel {

	public final Board board;
	public final String name;

	/**
	 * Constructor for the model
	 * @param board - The board where we want to put the taskList
	 * @param name - The name for the list
	 */
	public CreateListModel(Board board, String name) {
		this.board = board;
		this.name = name;
	}
}
