var express = require('express');
var router = express.Router();
var db = require('./dbManager')('MirrorMirror');

router.get('/', function(requ, res){
	res.redirect('/login');
});

router.get('/home', function(req, res) {
		res.redirect('/login');
});

router.get('/home/:uid', function(req, res) {
		var uid = req.params.uid;
		db.getFullName(uid, function(name){
			res.render('home', {
				uid: uid,
				name: name
			});
		});
});

router.get('/home/:uid/weights/:days', function(req, res) {
		var uid = req.params.uid;
		var days = req.params.days;
		db.getFullName(uid, function(name){
			db.getPreviousWeights(uid, days, function(data){
				res.render('weightgraph', {
					uid: uid,
					name: name,
					days: days,
					data: data
				});
			});
		});
});

router.get('/login', function(req, res) {
	res.render('login', {
	});
});

router.get('/register', function(req, res){
	res.render('register', {
	})
});

module.exports = router;
