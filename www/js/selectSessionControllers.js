angular.module('starter.controllers')
    .controller('SelectSessionCtrl', function ($scope, $state) {
        $scope.sessions = [
            {title: 'Simple', id: 0},
            {title: 'Multi parte', id: 1}
        ];

        $scope.showVersions = function (sessionId) {
            $state.go('app.selectTypeForSession', {sessionType: sessionId});
        };

    });
