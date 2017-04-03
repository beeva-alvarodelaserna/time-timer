angular.module('session.controllers', ['chart.js'])
    .controller('SessionCtrl', function ($scope, $stateParams, $ionicHistory, $ionicPopup, $state) {
        var timer;

        const maxTime = 60;
        let remainingTime = 60;
        let nonEmptyIndex = 0;

        $scope.myHeight = 100;
        $scope.myStyle = {
            'width': '100%',
            'height': $scope.myHeight,
            'background-color': '#ff9f8f'
        };
        $scope.sessionType = $stateParams.sessionId;
        $scope.isPie = $stateParams.versionId !== '1';
        $scope.isMultiple = $stateParams.sessionId !== '0';
        $scope.isRunning = false;
        $scope.isPaused = false;
        $scope.numberOfSteps = 2;
        $scope.data;
        $scope.sliderData;
        $scope.labels;
        $scope.slider;
        $scope.pieOptions = {
            tooltipEvents: [],
            showTooltips: true,
            tooltipCaretSize: 0,
            onAnimationComplete: function () {
                this.showTooltip(this.segments, true);
            }
        };

        $scope.init = function () {
            nonEmptyIndex = 0;
            if ($scope.isMultiple) {
                $scope.data = [0, 0, maxTime];
            } else {
                $scope.data = [0, maxTime];
            }
            if (angular.isDefined($stateParams.duration) && $stateParams.duration > 0) {
                $scope.data[0] = $stateParams.duration;
                $scope.drag($stateParams.duration);
            }
            $scope.generateLabels();

            $scope.slider = {
                options: {
                    floor: 0,
                    ceil: 60,
                    step: 5,
                    showTicks: true,
                    showTicksValues: false,
                    translate: function (value) {
                        let marker;
                        if (value !== 0 && value !== 60) {
                            marker = '<div class="pin"><span>' + value + '</span></div>';
                        } else {
                            marker = value;
                        }
                        return marker;
                    },
                    onChange: function(sliderId, modelValue) {
                        $scope.data[nonEmptyIndex] = modelValue;
                        $scope.drag(modelValue);
                    }
                }
            };
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
            $scope.sliderData = [];
            $scope.data.forEach(element => {
                $scope.labels.push("");
                $scope.sliderData.push(element);
            })
        };

        $scope.startTimer = function () {
            if ($scope.isPie) {
                $scope.isRunning = true;
                $scope.isPaused = false;
                console.log('dale');
                console.log('$scope.data[nonEmptyIndex]', $scope.data[nonEmptyIndex]);
                if (angular.isDefined($scope.data[nonEmptyIndex]) && $scope.data[nonEmptyIndex] == 0) {
                    nonEmptyIndex++;
                    console.log('nonEmptyIndex', nonEmptyIndex);
                    console.log('$scope.data[nonEmptyIndex]', $scope.data[nonEmptyIndex]);
                    if (nonEmptyIndex < $scope.data.length - 1) {
                        console.log('stop and start');
                        $scope.stopTimer();
                        $scope.startTimer();
                    } else {
                        console.log('stop and finish');
                        $scope.stopTimer();
                        $scope.finishSessionAndGoToSurvey();
                    }
                } else {
                    timer = setInterval(function () {
                        if (angular.isDefined($scope.data[nonEmptyIndex]) && $scope.data[nonEmptyIndex] > 0) {
                            $scope.data[nonEmptyIndex]--;
                            if ($scope.data[nonEmptyIndex] % 5 == 0) {
                                $scope.sliderData[nonEmptyIndex] = $scope.data[nonEmptyIndex];
                            }
                            console.log('nonEmptyIndex', nonEmptyIndex);
                            console.log('$scope.data[nonEmptyIndex]', $scope.data[nonEmptyIndex]);
                            if (nonEmptyIndex == 0) {
                                console.log('drag');
                                $scope.drag($scope.data[nonEmptyIndex]);
                            } else {
                                console.log('dragIndex');
                                $scope.dragIndex(nonEmptyIndex, $scope.data[nonEmptyIndex]);
                            }
                            $scope.$apply();
                        } else {
                            $scope.startTimer();
                        }
                    }, 1000);
                }
            } else {
                $state.go('app.sessionFill', {sessionId: $stateParams.sessionId, versionId: $stateParams.versionId, duration: $scope.data.join(',')});
            }
        };

        $scope.finishSessionAndGoToSurvey = function () {
            // $ionicPopup.alert({
            //     title: 'Se acabó',
            //     content: '¡Reunión finalizada!'
            // }).then(function () {
            //     $state.go('app.survey');
            // });
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
        };

        $scope.stopTimer = function () {
            clearInterval(timer);
            // $scope.init();
            $scope.isRunning = false;
            $scope.isPaused = false;
        };

        $scope.goBack = function () {
            $ionicHistory.goBack();
        };
    });
