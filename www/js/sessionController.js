angular.module('session.controllers', ['chart.js'])
  .controller('SessionCtrl', function ($scope, $stateParams, $ionicHistory) {
    console.log($stateParams);

    var timer;

    $scope.sessionType = $stateParams.sessionId;
    $scope.labels = ["Download Sales", "Mail-Order Sales"];
    $scope.isRunning = false;
    $scope.isPaused = false;
    var currentValue = 0;
    $scope.data = [0, 60];

    $scope.drag = function (newValue) {
      $scope.data[1] = 60 - newValue;
    };

    $scope.startTimer = function () {
      $scope.isRunning = true;
      $scope.isPaused = false;
      timer = setInterval(function () {
        if ($scope.data[0] > 0) {
          $scope.data[0]--;
          currentValue = $scope.data[0];
          $scope.drag($scope.data[0]);
          $scope.$apply();
        }
      }, 1000);
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
      $ionicHistory.goBack();
    };
  });
