angular.module('starter.controllers', [])

  .controller('AppCtrl', function ($scope) {

  })

  .controller('HomeCtrl', function ($scope, $state) {
    $scope.sessions = [
      {title: 'Simple', id: 0},
      {title: 'Multi parte', id: 1}
    ];

    $scope.showSession = function (sessionId) {
      if (sessionId === 0) {
          $state.go('app.sessionConfig');
      } else {
          $state.go('app.session', {sessionId: sessionId});
      }
    }
  });
