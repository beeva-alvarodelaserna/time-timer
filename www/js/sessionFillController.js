angular.module('session.controllers')
    .controller('SessionFillCtrl', function ($scope, $stateParams, $ionicHistory, $ionicPopup, $state, $rootScope) {

        var timer;
        let nonEmptyIndex = 0;

        $scope.myHeight = 0;

        $scope.sessionType = $stateParams.sessionId;
        $scope.isMultiple = $stateParams.sessionId !== '0';
        $scope.loaded = false;
        $scope.partFinished = false;
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

        $scope.boxHeight;
        $scope.box;

        $scope.movingTop = -100;

        $scope.moveInToPlace;
        $scope.remaining;

        $scope.updateStyle = function () {
            // $scope.myStyle = {
            //     'width': '100%',
            //     'height': $scope.myHeight + '%',
            //     'background-color': colors[nonEmptyIndex]
            // };
            $scope.moveInToPlace = {
                'position': 'absolute',
                'top': $scope.movingTop + '%',
                'height': '100%',
                'width': '100%',
                'background': '#ebebeb',
                'opacity': '1'
            };
        };

        $scope.init = function () {
            $scope.data = $stateParams.duration.split(',');
            parseData();
            $scope.loaded = true;
            if (!$scope.isRunning) {
                $scope.startTimer();
            }
        };

        const parseData = function () {
            $scope.cleanData = [];
            for (let i = 0; i < $scope.data.length; i++) {
                $scope.cleanData.push($scope.data[i]);
            }
            $scope.percentages = obtainPercentages();
            $scope.boxHeight = obtainBoxHeights();
            $scope.box = [];
            $scope.boxHeight.forEach(height => {
                $scope.box.push({
                    'position': 'relative',
                    'width': '100%',
                    'height': height + '%',
                    'overflow': 'hidden',
                    'border': 'black solid 1px',
                    'background': '#ffd71f'
                });
            });
            $scope.updateStyle();
        };

        const obtainBoxHeights = function () {
            let result = [];
            if ($scope.cleanData.length === 1) {
                result.push(100);
            } else {
                $scope.percentages.forEach(item => {
                    result.push(item);
                });
            }
            return result;
        };

        // $scope.onDragUp = function () {
        //     $scope.isDragging = true;
        //     $scope.stopTimer();
        //     if ($scope.data[nonEmptyIndex] < 60) {
        //         $scope.data[nonEmptyIndex]++;
        //     }
        //     parseData();
        //     if ($scope.myHeight > 0) {
        //         $scope.myHeight -= $scope.increments[nonEmptyIndex] * 100;
        //     }
        // };
        //
        // $scope.onDragDown = function () {
        //     $scope.isDragging = true;
        //     $scope.stopTimer();
        //     if ($scope.data[nonEmptyIndex] > 0) {
        //         $scope.data[nonEmptyIndex]--;
        //     }
        //     parseData();
        //     if ($scope.myHeight < 100) {
        //         $scope.myHeight += $scope.increments[nonEmptyIndex] * 100;
        //     }
        // };

        const obtainPercentages = function () {
            let sum = 0;
            let result = [];
            $scope.increments = [];
            $scope.cleanData.forEach(item => {
                sum += parseInt(item, 10);
            });
            $scope.cleanData.forEach(item => {
                let value = item * 100 / sum;
                result.push(value);
                $scope.increments.push(value / item);
            });
            return result;
        };

        $scope.startTimer = function () {
            $scope.remaining = $scope.cleanData[nonEmptyIndex];
            $scope.partFinished = false;
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
                        $scope.finishSession();
                    }
                } else {
                    timer = setInterval(function () {
                        if (angular.isDefined($scope.cleanData[nonEmptyIndex]) && $scope.cleanData[nonEmptyIndex] > 0) {
                            $scope.cleanData[nonEmptyIndex]--;
                            $scope.remaining = $scope.cleanData[nonEmptyIndex];
                            $scope.movingTop += $scope.increments[nonEmptyIndex];
                            $scope.updateStyle();
                            $scope.$apply();
                        } else {
                            if (nonEmptyIndex < $scope.cleanData.length - 1) {
                                $scope.partFinished = true;
                                $scope.pauseOrResumeTimer();
                                $scope.$apply();
                            } else {
                                $scope.startTimer();
                            }
                        }
                    }, 1000);
                }
            } else {
                $scope.stopTimer();
            }
        };

        $scope.finishSession = function () {
            $state.go('app.finished');
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

        $rootScope.$ionicGoBack = function () {
            $scope.goBack();
        };
    });
