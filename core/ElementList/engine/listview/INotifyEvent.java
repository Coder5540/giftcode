package engine.listview;

import engine.listview.ListView.Item;

public interface INotifyEvent {
	public void broadcastEvent(Item item, NotifyEvent notifyEvent);
}
