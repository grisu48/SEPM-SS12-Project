package org.smartsnip.core;

public interface INotificationObserver extends IObserver {
	public void receiveNotification(Notification notification);
}
