// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();
exports.checkflag = functions.database.ref('/notification') //give your database path instead here
.onUpdate((snapshot, context) => {
    let content = "";
    const token = snapshot.after.val().token;  //replace it with your app token
    const beforeToken = snapshot.before.val().token;
    const basket1 = (snapshot.after.val().basket1).toString();
    const basket2 = (snapshot.after.val().basket2).toString();
    const basket3 = (snapshot.after.val().basket3).toString();
    const basket1Before = (snapshot.before.val().basket1).toString();
    const basket2Before = (snapshot.before.val().basket2).toString();
    const basket3Before = (snapshot.before.val().basket3).toString();
   
    const payload = {
        data:{
            notification_type: 'BASKET',
            title: 'New Message',
            body: content,
            basket1: basket1,
            basket1Before: basket1Before,
            basket2: basket2,
            basket2Before: basket2Before,
            basket3: basket3,
            basket3Before: basket3Before
        }
    };
    console.log('got here');
    return admin.messaging().sendToDevice(token,payload);
    
});