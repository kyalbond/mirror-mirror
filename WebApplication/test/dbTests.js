var chai = require('chai');
var expect = chai.expect;


describe('DBTests,', function(){

  var db;

  describe('Setup Database:', function(){

    it('Create Test Database', function(done){
      db = require('./../src/js/dbManager')('MirrorMirrorTest', done);
    });

    it('Empty Users Table', function(){
      db.run('DELETE FROM Users', function(){
        db.get('SELECT COUNT(*) from Users', function(err, result){
          expect(result['COUNT(*)'] == 0);
        });
      });
    });

    it('Empty Photos Table', function(){
      db.run('DELETE FROM Photos', function(){
        db.get('SELECT COUNT(*) from Photos', function(err, result){
          expect(result['COUNT(*)'] == 0);
        });
      });
    });

    it('Empty Weights Table', function(){
      db.run('DELETE FROM Weights', function(){
        db.get('SELECT COUNT(*) from Weights', function(err, result){
          expect(result['COUNT(*)'] == 0);
        });
      });
    });
  });

  describe('Weights:', function(){

    var data = [
      {
        uid: 1,
        datetime: new Date(),
        weight: 75.0
      },
      {
        uid: 1,
        datetime: new Date(),
        weight: 75.1
      },
      {
        uid: 1,
        datetime: new Date(),
        weight: 75.4
      }
    ];

    it('Save Weight', function(done){
      db.saveWeight(data[0].uid, data[0].datetime, data[0].weight);
      db.saveWeight(data[1].uid, data[1].datetime, data[1].weight);
      db.saveWeight(data[2].uid, data[2].datetime, data[2].weight, done);
    });

    it('Get Previous Weight', function(){
      db.getPreviousWeights(1, 1, function(result){
        expect(result.length == 1);
        expect(result.uid == data[2].uid);
        expect(result.datetime == data[2].datetime);
        expect(result.weight == data[2].weight);
      });
    });

    it('Get Previous Weights', function(){
      db.getPreviousWeights(1, 2, function(result){
        expect(result.length == 2);

        expect(result[0].uid == data[2].uid);
        expect(result[0].datetime == data[2].datetime);
        expect(result[0].weight == data[2].weight);

        expect(result[1].uid == data[1].uid);
        expect(result[1].datetime == data[1].datetime);
        expect(result[1].weight == data[1].weight);
      });
    });
  });

  describe('Photos:', function(){

    var data = [
      {
        uid: 1,
        datetime: new Date(),
        filepath: './filepath1'
      },
      {
        uid: 1,
        datetime: new Date(),
        filepath: './filepath2'
      },
      {
        uid: 1,
        datetime: new Date(),
        filepath: './filepath3'
      }
    ];

    it('Save Photo', function(done){
      db.savePhoto(data[0].uid, data[0].datetime, data[0].filepath);
      db.savePhoto(data[1].uid, data[1].datetime, data[1].filepath);
      db.savePhoto(data[2].uid, data[2].datetime, data[2].filepath, done);
    });

    it('Get Previous Photo', function(){
      db.getImages(1, 1, 0, function(result){
        expect(result.length == 1);
        expect(result.uid == data[2].uid);
        expect(result.datetime == data[2].datetime);
        expect(result.weight == data[2].filepath);
      });
    });

    it('Get Previous Photos', function(){
      db.getImages(1, 2, 0, function(result){
        expect(result.length == 2);

        expect(result[0].uid == data[2].uid);
        expect(result[0].datetime == data[2].datetime);
        expect(result[0].weight == data[2].filepath);

        expect(result[1].uid == data[1].uid);
        expect(result[1].datetime == data[1].datetime);
        expect(result[1].weight == data[1].filepath);
      });
    });

    it('Get Previous Photos With Offset', function(){
      db.getImages(1, 2, 1, function(result){
        expect(result.length == 2);

        expect(result[0].uid == data[1].uid);
        expect(result[0].datetime == data[1].datetime);
        expect(result[0].weight == data[1].filepath);

        expect(result[1].uid == data[0].uid);
        expect(result[1].datetime == data[0].datetime);
        expect(result[1].weight == data[0].filepath);
      });
    });

    it('Get Photo For Given Day', function(){

    });

  });

});
