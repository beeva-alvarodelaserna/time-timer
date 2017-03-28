angular.module('session.controllers')
    .controller('SessionConfigCtrl', function ($scope, $stateParams, $ionicHistory, $state) {

        $scope.goBack = function () {
            $ionicHistory.goBack();
        };

        $scope.predefinedSessions = [
            {
                duration: 15,
                style: {
                    'margin-top': '5%',
                    'margin-left': '30%',
                    'width': '50px'
                }
            },
            {
                duration: 30,
                style: {
                    'margin-left': '45%',
                    'width': '75px'
                }
            },
            {
                duration: 45,
                style: {
                    'margin-left': '50%',
                    'width': '100px'
                }
            },
            {
                duration: 60,
                style: {
                    'margin-left': '45%',
                    'width': '125px'
                }
            },
            {
                duration: 0,
                style: {
                    'margin-left': '25%',
                    'width': '75px'
                }
            }
        ];

        $scope.goToSimpleSession = function (duration) {
            if (duration === 0) {
                $state.go('app.session', {sessionId: 0, versionId: $stateParams.versionId});
            } else {
                $state.go('app.session', {sessionId: 0, versionId: $stateParams.versionId, duration: duration});
            }
        }
    });
