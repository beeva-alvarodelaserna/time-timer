angular.module('session.controllers')
    .controller('SessionConfigCtrl', function ($scope, $stateParams, $ionicHistory, $state) {

        $scope.goBack = function () {
            $ionicHistory.goBack();
        };

        $scope.predefinedSessions = [
            {
                duration: 15
            },
            {
                duration: 30
            },
            {
                duration: 45
            },
            {
                duration: 60
            },
            {
                duration: 0
            }
        ];

        $scope.showSession = function (duration) {
            if (duration === 0) {
                $state.go('app.session', {sessionId: 0});
            } else {
                $state.go('app.session', {sessionId: 0, duration: duration});
            }
        }
    });
