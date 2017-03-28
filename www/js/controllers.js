angular.module('starter.controllers', [])

    .controller('AppCtrl', function ($scope) {

    })

    .controller('HomeCtrl', function ($scope, $state) {
        $scope.goToSelectSession = function () {
            $state.go('app.selectSession');
        };
    });
