angular.module('finished.controllers', ['ngCordova'])
    .controller('FinishedCtrl', function ($scope, $state, $ionicPlatform, $timeout, $cordovaNativeAudio) {

        $scope.loaded = false;
        // $ionicPlatform.ready(function () {
        //     $cordovaNativeAudio.preloadSimple('alarm', '../audio/alarm.mp3');
        // });
        //
        $scope.goToSurvey = function () {
            $scope.loaded = false;
            $state.go('app.survey');
        };
        //
        // $scope.play = function (sound) {
        //     $cordovaNativeAudio.play(sound);
        // };

        $scope.playAlarm = function () {
            $scope.loaded = true;
        };
    });
