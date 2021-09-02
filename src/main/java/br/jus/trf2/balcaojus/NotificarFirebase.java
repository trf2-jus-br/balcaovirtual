package br.jus.trf2.balcaojus;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.SwaggerUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NotificarFirebase {
	private static class NotificationResult {
		String message_id;
		String registration_id;
		String error;
	}

	private static class NotificationBodyNotification {
		String title;
		String body;
		String click_action;
		String icon;
	}

	private static class NotificationBody {
		NotificationBodyNotification notification = new NotificationBodyNotification();
		String to;
		List<String> registration_ids = new ArrayList<>();
	}

	private static class NotificationResultBody {
		NotificationBodyNotification notification = new NotificationBodyNotification();
		long multicast_id;
		int success;
		int failure;
		NotificationResult results[];
	}

	private static class GroupBody {
		String operation;
		String notification_key_name;
		List<String> registration_ids = new ArrayList<>();
	}

	private static class GroupResp {
		String notification_key;
	}

	public static int enviarNotificacao(Dao dao, List<String> tokens, String title, String message, String clickAction, String icon)
			throws Exception {
		Gson gson = new GsonBuilder().create();

		// Envia a notificação
		NotificationBody nb = new NotificationBody();
		nb.notification.title = title;
		nb.notification.body = message;
		nb.notification.click_action = clickAction;
		nb.notification.icon = icon;
		for (String t : tokens) {
			nb.registration_ids.add(t);
		}
//		nb.registration_ids = null;
//		nb.to = tokens.get(0);
		String body = gson.toJson(nb);

		byte[] resp = fetch("key=" + BalcaojusServlet.INSTANCE.getProperty("firebase.server.key"),
				"https://fcm.googleapis.com/fcm/send", "POST", "application/json",
				body.getBytes(StandardCharsets.UTF_8));

		if (resp != null) {
			NotificationResultBody r = gson.fromJson(new String(resp, StandardCharsets.UTF_8),
					NotificationResultBody.class);
			int sucessos = r.success;
			int falhas = r.failure;
			int removidos = 0;
			if (r.results != null) {
				int i = 0;
				for (NotificationResult e : r.results) {
					if (e.error != null) {
						switch (e.error) {
						case "InvalidRegistration":
						case "NotRegistered":
							dao.removerTokenParaNotificar(tokens.get(i));
							// SwaggerUtils.log(NotificarFirebase.class).info("Removido o token: " + tokens.get(i));
							removidos++;
							break;
						}
					}
					++i;
				}
			}
			return sucessos;
		}
		return 0;
	}

	public static byte[] fetch(String authorization, String url, String method, String contentType, byte[] body)
			throws Exception {
		HttpURLConnection con = null;
		URL obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();

		if (authorization != null)
			con.setRequestProperty("Authorization", authorization);
		con.setRequestMethod(method);

		if (body != null && ("POST".equals(method) || "PUT".equals(method))) {
			con.setRequestProperty("Content-Type", contentType);
			con.setRequestProperty("Content-Length", Integer.toString(body.length));
			con.setRequestProperty("charset", "utf-8");

			con.setConnectTimeout(5000); // set timeout to 5 seconds
			con.setReadTimeout(20000); // set read timeout to 20 seconds
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(body);
			wr.flush();
			wr.close();
		}

		int responseCode = con.getResponseCode();

		if (responseCode >= 400 && responseCode < 600) {
			InputStream errorStream = null;
			errorStream = con.getErrorStream();
			if (errorStream != null)
				return IOUtils.toByteArray(errorStream);
		}
		return IOUtils.toByteArray(con.getInputStream());
	}

}
