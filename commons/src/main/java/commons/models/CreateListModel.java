package commons.models;

import commons.Board;

public class CreateListModel {

	public final Board board;
	public final String name;

	public CreateListModel(Board board, String name) {
		this.board = board;
		this.name = name;
	}
}
