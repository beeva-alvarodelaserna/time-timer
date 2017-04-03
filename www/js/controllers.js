angular.module('starter.controllers', [])
    .controller('AppCtrl', function ($scope) {

    })
    .controller('HomeCtrl', function ($scope, $state) {
        $scope.goToSelectSession = function () {
            $state.go('app.selectSession');
        };
        $scope.sessions = [
            {title: 'One pill', id: 0},
            {title: 'Multiple pills', id: 1}
        ];

        $scope.showVersions = function (sessionId) {
            $state.go('app.selectTypeForSession', {sessionType: sessionId});
        };
    });
