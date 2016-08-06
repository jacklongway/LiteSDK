package com.longway.framework.version;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat.Builder;

public class NotificationUtil {
	public static void sendNotification(Context ctx, String title,
			String content, int max, int progress, int iconRes, int id) {

		Builder builder = new Builder(ctx);
		builder.setContentTitle(title);
		builder.setAutoCancel(false);
		builder.setContentText(content);
		builder.setProgress(max, progress, true);
		builder.setSmallIcon(iconRes);
		NotificationManager manager = (NotificationManager) ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(id, builder.build());
	}

	public static void cancelNotification(Context ctx, int id) {
		NotificationManager manager = (NotificationManager) ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(id);
	}
	public static void sendNotification(Context ctx, String title,
			String content,int iconRes, int id) {
		Builder builder = new Builder(ctx);
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setSmallIcon(iconRes);
		NotificationManager manager = (NotificationManager) ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(id, builder.build());
	}
}
