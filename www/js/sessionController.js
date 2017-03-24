angular.module('session.controllers', ['chart.js'])
    .controller('SessionCtrl', function ($scope, $stateParams, $ionicHistory, $ionicPopup, $state) {
        var timer;

        const maxTime = 60;
        let remainingTime = 60;
        let nonEmptyIndex = 0;

        $scope.sessionType = $stateParams.sessionId;
        $scope.isMultiple = $stateParams.sessionId !== '0';
        $scope.isRunning = false;
        $scope.isPaused = false;
        $scope.numberOfSteps = 2;
        let currentValue = 0;
        $scope.data;
        $scope.labels;
        $scope.pieOptions = {
            tooltipEvents: [],
            showTooltips: true,
            tooltipCaretSize: 0,
            onAnimationComplete: function () {
                this.showTooltip(this.segments, true);
            }
        };

        $scope.init = function () {
            if ($scope.isMultiple) {
                $scope.data = [0, 0, maxTime];
            } else {
                $scope.data = [0, maxTime];
            }
            $scope.generateLabels();
        };

        $scope.range = function (count) {
            return new Array(count);
        };

        $scope.incrementSteps = function () {
            $scope.numberOfSteps++;
            $scope.data.splice($scope.data.length - 1, 0, 0);
            $scope.labels.push("");
        };

        $scope.decrementSteps = function () {
            $scope.numberOfSteps--;
            if ($scope.numberOfSteps < 2) {
                $scope.numberOfSteps = 2;
            }
            if ($scope.data.length > 2) {
                $scope.data.splice($scope.data.length - 2, 1);
                $scope.labels.pop();
            }
        };

        $scope.drag = function (newValue) {
            $scope.data[$scope.data.length - 1] = 60 - newValue;
        };

        $scope.dragIndex = function () {
            remainingTime = maxTime;
            for (let i = 0; i < $scope.data.length - 1; i++) {
                remainingTime -= $scope.data[i];
            }
            if (remainingTime > 0) {
                $scope.data[$scope.data.length - 1] = remainingTime;
            } else {
                $scope.data[$scope.data.length - 1] = 0;
            }
        };

        $scope.generateLabels = function () {
            $scope.labels = [];
            $scope.data.forEach(element => $scope.labels.push(""))
        };

        $scope.startTimer = function () {
            $scope.isRunning = true;
            $scope.isPaused = false;
            if (angular.isDefined($scope.data[nonEmptyIndex]) && $scope.data[nonEmptyIndex] == 0) {
                nonEmptyIndex++;
                if (nonEmptyIndex < $scope.data.length - 1) {
                    $scope.stopTimer();
                    $scope.startTimer();
                } else {
                    $scope.stopTimer();
                    $scope.finishSessionAndGoToSurvey();
                }
            } else {
                timer = setInterval(function () {
                    if (angular.isDefined($scope.data[nonEmptyIndex]) && $scope.data[nonEmptyIndex] > 0) {
                        $scope.data[nonEmptyIndex]--;
                        currentValue = $scope.data[nonEmptyIndex];
                        if (nonEmptyIndex == 0) {
                            $scope.drag($scope.data[nonEmptyIndex]);
                        } else {
                            $scope.dragIndex(nonEmptyIndex, $scope.data[nonEmptyIndex]);
                        }
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
            $scope.isRunning = false;
            $scope.isPaused = false;
        };

        $scope.goBack = function () {
            $ionicHistory.goBack();
        };
    });
