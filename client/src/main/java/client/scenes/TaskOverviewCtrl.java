package client.scenes;

import client.utils.ServerUtils;

public class TaskOverviewCtrl {

	private final MainCtrl mainCtrl;
	private final ServerUtils server;

	public TaskOverviewCtrl(MainCtrl mainCtrl, ServerUtils server) {
		this.mainCtrl = mainCtrl;
		this.server = server;
	}

}
