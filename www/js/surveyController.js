angular.module('survey.controllers', [])
    .controller('SurveyCtrl', function ($scope) {

        $scope.goToFinalView = function () {
            $state.go();
        };
    });
