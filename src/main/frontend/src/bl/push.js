import firebase from "firebase/app";
import "firebase/messaging";
import { Bus } from "./bus.js";

export function initializeFirebase(registration, properties) {
  if (!firebase.messaging.isSupported()) return;
  console.log("firebase messaging supported");
  const firebaseConfig = {
    apiKey: properties["balcaojus.firebase.api.key"],
    authDomain: properties["balcaojus.firebase.auth.domain"],
    databaseURL: properties["balcaojus.firebase.database.url"],
    projectId: properties["balcaojus.firebase.project.id"],
    storageBucket: properties["balcaojus.firebase.storage.bucket"],
    messagingSenderId: properties["balcaojus.firebase.messaging.sender.id"],
    appId: properties["balcaojus.firebase.app.id"]
  };

  firebase.initializeApp(firebaseConfig);
  console.log("firebase initialized");

  const messaging = firebase.messaging();
  messaging.useServiceWorker(registration);
  messaging
    .requestPermission()
    .then(() => {
      console.log("Permission granted");
      return messaging.getToken();
    })
    .then(token => {
      console.log(token);
      Bus.$emit("notification-permission-granted", token);
    })
    .catch(error => {
      if (error.code === "messaging/permission-blocked") {
        Bus.$emit("notification-permission-denied");
        console.log("Please Unblock Notification Request Manually");
      } else {
        messaging.getToken().then(token => {
          console.log(token);
          Bus.$emit("notification-permission-granted", token);
          return token;
        });
        Bus.$emit("notification-permission-fail", error);
        console.log("Error Occurred", error);
      }
    });
}
