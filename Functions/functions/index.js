const functions = require("firebase-functions");
const admin=require("firebase-admin");
admin.initializeApp(functions.config().firebase);
exports.notifyVendor=functions.database.ref("Notifications/{userID}/").onWrite((data,context)=>{
    console.log("Push Notification triggered");
    const valueObject=change.after.val();

    const payLoad={
        notification:{
            title:valueObject.title,
            body:valueObject.message,
            sound:"default"
        }
    };
    const options={
        priority:"high",
        timeToLive:60*60*24
    };
    return admin.messaging.sendToTopic("Notify Vendor",payload,options);
});