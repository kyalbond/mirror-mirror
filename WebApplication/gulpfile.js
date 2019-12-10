var gulp = require('gulp');
var mocha = require('gulp-mocha');
var gutil = require('gulp-util');
var nodemon = require('gulp-nodemon');
var build = require('gulp-build');
var jscs = require('gulp-jscs');
var stylish = require('gulp-jscs-stylish');
var jshint = require('gulp-jshint');

var jsFiles = ['*.js', 'test/*.js'];

gulp.task('serve', [], function() {
	var options = {
		script: 'app.js',
		delayTime: 1,
		watch: jsFiles
	};
	return nodemon(options)
	.on('restart', function() {
		gulp.run('mocha');
		gulp.run('style');
		console.log('Restarting...');
	});
});

gulp.task('style', function() {
	gulp.src(jsFiles)
	.pipe(jscs())// enforce style guide
	.pipe(jscs())
	.pipe(stylish.combineWithHintResults())
	.pipe(jshint.reporter('jshint-stylish'));
});

gulp.task('mocha', function() {
	return gulp.src(['./test/*.js'], { read: false })
	.pipe(mocha({ reporter: 'list' }))
	.on('error', gutil.log);
});

gulp.task('build', function() {
	gulp.src('*.js')
	.pipe(build({ GA_ID: '123456' }))
	.pipe(gulp.dest('dist'));
});
