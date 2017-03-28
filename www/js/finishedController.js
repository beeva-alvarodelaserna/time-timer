angular.module('final.controllers')
    .controller('FinishedCtrl', function ($scope, $state) {

        $scope.goToSurvey = function () {
            $state.go('app.survey');
        };
    });
