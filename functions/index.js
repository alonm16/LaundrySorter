const admin = require ('firebase-admin');
const functions = require ('firebase-functions');
admin.initializeApp(functions.config().firebase);

const functionTriggers = functions.region("us-central1").firestore;
const db = admin.firestore();




exports.sendMessageCopy = functionTriggers.document('users/{userId}')
	.onCreate((snap,context)=>{
		const userId = context.params.userId;
		console.log('got here');
		const payload = {
			data:{
				notification_type: 'BASKET',
				title: 'New Message',
				body: 'hello'
			}
		};
		
		return db.collection('users')
			.doc(userId)
			.get()
			.then(receiverDoc=>{
				const tokens = receiverDoc.data().deviceToken;
				return admin.messaging().sendToDevice(tokens, payload);
			});
	});

	

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
