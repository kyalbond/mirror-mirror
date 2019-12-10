var sqlite3 = require('sqlite3').verbose();
var db;
var dateFormat = require('dateformat');

function formatDateTime(datetime){
	return dateFormat(datetime, "yyyy-mm-dd hh:MM:ss");
}

function savePhoto(uid, datetime, filepath, callback){
	var stmt = db.prepare('INSERT INTO photos(UID, DateTime, FilePath) VALUES (?, ?, ?)');
	stmt.run(uid, datetime, filepath);
	stmt.finalize();
	if(callback){
		callback();
	}
}

function getImages(uid, numImages, offset, callback){
	db.all("SELECT * FROM photos WHERE UID = " + uid + " ORDER BY DateTime DESC",  function (err, results){
		if(err){
			console.log(err);
		}else{
			if(results.length < offset){
				callback([]);
			}else if(results.length < numImages + offset){
				callback(results.slice(offset, results.length));
			}else{
				callback(results.slice(offset, offset + numImages));
			}
		}
	});
}

function saveWeight(uid, datetime, weight, callback){
	weight = weight.toFixed(1);
	var stmt = db.prepare('INSERT INTO weights(UID, DateTime, Weight) VALUES (?, ?, ?)');
	stmt.run(uid, formatDateTime(datetime), weight);
	stmt.finalize();
	if(callback){
		callback();
	}
}

function saveHeight(uid, datetime, height, callback){
	height = height.toFixed(1);
	var stmt = db.prepare('INSERT INTO heights(UID, DateTime, Height) VALUES (?, ?, ?)');
	stmt.run(uid, formatDateTime(datetime), height);
	stmt.finalize();
	if(callback){
		callback();
	}
}

function getPreviousWeights(uid, numDays, callback){
	db.all("SELECT datetime, weight FROM weights WHERE DateTime > (SELECT DATETIME('now', '-" + (numDays - 1) + " day')) AND uid = " + uid + " ORDER BY DateTime DESC", function(err, results){
	//db.all("SELECT datetime, weight FROM weights WHERE DateTime > (SELECT DATETIME('now', '-" + (numDays - 1) + " day')) AND uid = " + uid + " ORDER BY DateTime DESC", function(err, results){
		if(err){
			console.log(err);
		}else{
			callback(results);
		}
	});
}

function getFullName(uid, callback){
	db.all("SELECT FirstName, LastName FROM users WHERE uid = " + uid, function(err, results){
		callback(results[0].FirstName + " " + results[0].LastName);
	});
}

function getPreviousHeights(uid, numDays, callback){
	db.all("SELECT datetime, height FROM heights WHERE DateTime > (SELECT DATETIME('now', '-" + (numDays - 1) + " day')) AND uid = " + uid + " ORDER BY DateTime DESC", function(err, results){
		console.log(results);
		if(err){
			console.log(err);
		}else{
			callback(results);
		}
	});
}

function getLatestHeight(uid, callback){
	db.all("SELECT height FROM heights WHERE uid = " + uid + " ORDER BY DateTime DESC LIMIT 1", function(err, results){
		if(err){
			console.log("ERROR: " + err);
		}else{
			callback(results);
		}
	})
}

function checkLoginDetails(username, password, callback){
	db.all("SELECT UID FROM Users WHERE Username = '" + username + "' AND Password = '" + password + "'", function(err, results){
		if(err){
			console.log(err);
		}else{
			if(results.length == 0){
				callback(null);
			}else{
				callback(results[0]);
			}
		}
	});
}

function getUID(username, callback){
	db.all("SELECT UID FROM Users WHERE Username = '" + username + "'", function(err, results){
		callback(results);
	});
}

function newAccount(username, password, firstname, lastname, height, callback){
	var stmt = db.prepare('INSERT INTO users(LastName, FirstName, Username, Password) VALUES (?, ?, ?, ?)');
	stmt.run(lastname, firstname, username, password);
	stmt.finalize();
	getUID(username, function(results){
		callback(results);
	});
}

function saveFlow(uid, datetime, peakflow){
	var stmt = db.prepare('INSERT INTO peakflow(UID, DateTime, PeakFlow) VALUES (?, ?, ?)');
	stmt.run(uid, formatDateTime(datetime), peakflow);
	stmt.finalize();
}

function openDatabase(dbname, callback){
	db = new sqlite3.Database(dbname);
	require('fs').readFile('./databaseCreatorScript.sql', function(err, script){
		if(err){
			throw err;
		}
		db.serialize(function(){
			db.run(script.toString(), function(){
				console.log("Database " + dbname + " created.");
			});
		});
	});
	db.savePhoto = savePhoto;
	db.saveWeight = saveWeight;
	db.saveHeight = saveHeight;
	db.getImages = getImages;
	db.checkLoginDetails = checkLoginDetails;
	db.getFullName = getFullName;
	db.getPreviousWeights = getPreviousWeights;
	db.getPreviousHeights = getPreviousHeights;
	db.getLatestHeight = getLatestHeight;
	db.getUID = getUID;
	db.newAccount = newAccount;
	db.saveFlow = saveFlow;
	if(callback){
		callback();
	}
	return db;
}

module.exports = openDatabase;
