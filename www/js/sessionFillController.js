angular.module('session.controllers')
    .controller('SessionFillCtrl', function ($scope, $stateParams, $ionicHistory, $ionicPopup, $state) {
        var timer;

        let increment = 0;
        let nonEmptyIndex = 0;

        $scope.myHeight = 0;

        $scope.sessionType = $stateParams.sessionId;
        $scope.isMultiple = $stateParams.sessionId !== '0';
        $scope.isRunning = false;
        $scope.isPaused = false;
        $scope.numberOfSteps = 2;
        $scope.percentages = [];
        $scope.increments = [];
        $scope.cleanData;
        const colors = [
            '#ebebeb',
            '#aa9ada',
            '#119bdb',
            '#d49aaa',
            '#149aaa',
            '#9a9aaa'
        ];

        $scope.updateStyle = function() {
            $scope.myStyle = {
                'width': '100%',
                'height': $scope.myHeight + '%',
                'background-color': colors[nonEmptyIndex]
            };
        };

        $scope.init = function () {
            $scope.data = $stateParams.duration.split(',');
            $scope.cleanData = [];
            for (let i = 0; i < $scope.data.length - 1; i++) {
                $scope.cleanData.push($scope.data[i]);
            }
            $scope.percentages = obtainPercentages();
            $scope.updateStyle();
            if (!$scope.isRunning) {
                $scope.startTimer();
            }
        };

        const obtainPercentages = function () {
            let sum = 0;
            let result = [];
            $scope.data.forEach(item => {
               sum += parseInt(item, 10);
            });
            $scope.data.forEach(item => {
                let value = item*100/sum;
                result.push(value);
                $scope.increments.push(1/item);
            });
            return result;
        };

        $scope.startTimer = function () {
            $scope.isRunning = true;
            $scope.isPaused = false;
            if (angular.isDefined($scope.cleanData[nonEmptyIndex]) && $scope.cleanData[nonEmptyIndex] == 0) {
                if (nonEmptyIndex < $scope.cleanData.length - 1) {
                    $scope.stopTimer();
                    nonEmptyIndex++;
                    $scope.startTimer();
                } else {
                    $scope.stopTimer();
                    $scope.finishSessionAndGoToSurvey();
                }
            } else {
                timer = setInterval(function () {
                    if (angular.isDefined($scope.cleanData[nonEmptyIndex]) && $scope.cleanData[nonEmptyIndex] > 0) {
                        $scope.cleanData[nonEmptyIndex]--;
                        console.log($scope.cleanData[nonEmptyIndex]);
                        $scope.percentages = obtainPercentages();
                        // $scope.myHeight = 100 - Math.floor($scope.percentages[nonEmptyIndex]*100/60);
                        $scope.myHeight += $scope.increments[nonEmptyIndex]*100;
                        $scope.updateStyle();
                        $scope.$apply();
                    } else {
                        $scope.startTimer();
                    }
                }, 1000);
            }
        };

        $scope.finishSessionAndGoToSurvey = function () {
            $ionicPopup.alert({
                title: 'Se acabó',
                content: '¡Reunión finalizada!'
            }).then(function () {
                $state.go('app.survey');
            });
        };

        $scope.pauseOrResumeTimer = function () {
            if ($scope.isPaused) {
                $scope.startTimer();
            } else {
                $scope.isRunning = false;
                $scope.isPaused = true;
                clearInterval(timer);
            }
        };

        $scope.stopTimer = function () {
            clearInterval(timer);
        };

        $scope.goBack = function () {
            $ionicHistory.goBack();
        };
    });
