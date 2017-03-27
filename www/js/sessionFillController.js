angular.module('session.controllers')
    .controller('SessionFillCtrl', function ($scope, $stateParams, $ionicHistory, $ionicPopup, $state) {
        var timer;

        const maxTime = 60;
        let nonEmptyIndex = 0;

        $scope.myHeight = 90;

        $scope.sessionType = $stateParams.sessionId;
        $scope.isMultiple = $stateParams.sessionId !== '0';
        $scope.isRunning = false;
        $scope.isPaused = false;
        $scope.numberOfSteps = 2;
        $scope.percentages = [];
        $scope.cleanData;
        const colors = [
            '#ff9f8f',
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
                result.push(item*100/sum);
            });
            return result;
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
                        $scope.percentages = obtainPercentages();
                        $scope.myHeight = Math.floor($scope.percentages[nonEmptyIndex]*100/60);
                        console.log('myHeight', $scope.myHeight);
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
            $scope.init();
            $scope.isRunning = false;
            $scope.isPaused = false;
        };

        $scope.goBack = function () {
            $ionicHistory.goBack();
        };
    });
