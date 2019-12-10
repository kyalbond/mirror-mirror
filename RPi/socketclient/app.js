var fs = require('fs');
var fileName = '../wiiboard/weights.json';

var socket = require('socket.io-client')('http://130.195.6.76:3000');
socket.on('connect', function(){console.log('Connected!');});

function sendWeight(){
	setTimeout(function(){
		fs.readFile(fileName, 'utf8', function(err, data){
			var weight = JSON.parse(data);
			console.log(weight);
			if(!weight.sent){
				socket.emit("weight event", {"uid": 3, "weight":weight.weight})
				weight.sent = true;
				fs.writeFile(fileName, JSON.stringify(weight), function(err){
					console.log("Sent weight");
				});
			}
			sendWeight();
		});
	}, 500);
}

sendWeight();
