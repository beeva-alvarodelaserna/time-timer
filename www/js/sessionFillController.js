angular.module('session.controllers')
    .controller('SessionFillCtrl', function ($scope, $stateParams, $ionicHistory, $ionicPopup, $state, $rootScope) {

        var timer;
        let nonEmptyIndex = 0;

        $scope.myHeight = 0;

        $scope.sessionType = $stateParams.sessionId;
        $scope.isMultiple = $stateParams.sessionId !== '0';
        $scope.isRunning = false;
        $scope.isPaused = false;
        $scope.isDragging = false;
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

        $scope.updateStyle = function () {
            $scope.myStyle = {
                'width': '100%',
                'height': $scope.myHeight + '%',
                'background-color': colors[nonEmptyIndex]
            };
        };

        $scope.init = function () {
            $scope.data = $stateParams.duration.split(',');
            parseData();
            if (!$scope.isRunning) {
                $scope.startTimer();
            }
        };

        const parseData = function () {
            $scope.cleanData = [];
            for (let i = 0; i < $scope.data.length - 1; i++) {
                $scope.cleanData.push($scope.data[i]);
            }
            $scope.percentages = obtainPercentages();
            $scope.updateStyle();
        };

        $scope.onDragUp = function () {
            $scope.isDragging = true;
            $scope.stopTimer();
            if ($scope.data[nonEmptyIndex] < 60) {
                $scope.data[nonEmptyIndex]++;
            }
            parseData();
            if ($scope.myHeight > 0) {
                $scope.myHeight -= $scope.increments[nonEmptyIndex] * 100;
            }
        };

        $scope.onDragDown = function () {
            $scope.isDragging = true;
            $scope.stopTimer();
            if ($scope.data[nonEmptyIndex] > 0) {
                $scope.data[nonEmptyIndex]--;
            }
            parseData();
            if ($scope.myHeight < 100) {
                $scope.myHeight += $scope.increments[nonEmptyIndex] * 100;
            }
        };

        const obtainPercentages = function () {
            let sum = 0;
            let result = [];
            $scope.data.forEach(item => {
                sum += parseInt(item, 10);
            });
            $scope.data.forEach(item => {
                let value = item * 100 / sum;
                result.push(value);
                $scope.increments.push(1 / item);
            });
            return result;
        };

        $scope.startTimer = function () {
            if (!$scope.isDragging) {
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
                            $scope.percentages = obtainPercentages();
                            $scope.myHeight += $scope.increments[nonEmptyIndex] * 100;
                            $scope.updateStyle();
                            $scope.$apply();
                        } else {
                            $scope.startTimer();
                        }
                    }, 1000);
                }
            } else {
                $scope.stopTimer();
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
            $scope.isDragging = false;
        };

        $scope.stopTimer = function () {
            clearInterval(timer);
            $scope.isRunning = false;
            $scope.isPaused = true;
            $scope.isDragging = false;
        };

        $scope.goBack = function () {
            $ionicHistory.goBack();
            $scope.stopTimer();
        };

        $rootScope.$ionicGoBack = function() {
            $scope.goBack();
        };
    });
