angular.module('starter.controllers')
    .controller('SelectTypeForSessionCtrl', function ($scope, $state, $stateParams) {

        $scope.isSimple = $stateParams.sessionType === '0';
        $scope.sessionType = $stateParams.sessionType;
        $scope.versionTypes = {
            0: [
                {
                    id: 0 // 'pie'
                },
                {
                    id: 1 // 'fill'
                }
            ],
            1: [
                {
                    id: 0 // 'pie'
                },
                {
                    id: 1 // 'fill'
                }
            ]
        };

        $scope.goToVersion = function (sessionId) {
            if ($scope.isSimple) {
                $state.go('app.sessionConfig', {versionId: sessionId});
            } else {
                $state.go('app.session', {sessionId: 1, versionId: sessionId});
            }
        };

    });
