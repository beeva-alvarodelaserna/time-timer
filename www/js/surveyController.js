angular.module('survey.controllers', [])
    .controller('SurveyCtrl', function ($scope, $state) {

        $scope.goToFinalView = function (option) {
            $state.go('app.final', {optionId: option});
        };
    });
