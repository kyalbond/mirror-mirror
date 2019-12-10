var app = require('express')();

app.set('view engine', 'ejs');

app.get('/', function(req, res){
	res.render('index',
		{
			names: ['Kyals', 'Jonathan', 'Simon']

		});
});

app.listen(3000, function(){
	console.log("Listening on port 3000");
});