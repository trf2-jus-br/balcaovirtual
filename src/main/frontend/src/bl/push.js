import firebase from "firebase/app";
import "firebase/messaging";
import { Bus } from "./bus.js";

export function initializeFirebase(registration, properties) {
  if (!firebase.messaging.isSupported()) return;
  console.log("firebase messaging supported");
  const firebaseConfig = {
    apiKey: properties["balcaovirtual.firebase.api.key"],
    authDomain: properties["balcaovirtual.firebase.auth.domain"],
    databaseURL: properties["balcaovirtual.firebase.database.url"],
    projectId: properties["balcaovirtual.firebase.project.id"],
    storageBucket: properties["balcaovirtual.firebase.storage.bucket"],
    messagingSenderId: properties["balcaovirtual.firebase.messaging.sender.id"],
    appId: properties["balcaovirtual.firebase.app.id"]
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
