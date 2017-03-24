angular.module('final.controllers', [])
    .controller('FinalCtrl', function ($scope, $state, $stateParams, $ionicHistory) {

        $scope.message;

        $scope.parseResult = function() {
            switch ($stateParams.optionId) {
                case '1':
                    $scope.message = "No está mal";
                    break;
                case '2':
                    $scope.message = "Habrá que mejorar";
                    break;
                default:
                    $scope.message = "Otro trabajo bien hecho";
                    break;
            }
        };

        $scope.goBack = function () {
            $ionicHistory.nextViewOptions({
                disableBack: true
            });

            $state.go('app.home');
        };

        $scope.quit = function () {
            ionic.Platform.exitApp();
            // window.close();
        };
    });
