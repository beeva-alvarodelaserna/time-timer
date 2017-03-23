angular.module('final.controllers', [])
    .controller('FinalCtrl', function ($scope, $ionicHistory) {

        $scope.goBack = function () {
            $ionicHistory.goToHistoryRoot();
        };
        $scope.quit = function () {
            ionic.Platform.exitApp();
        };
    });
