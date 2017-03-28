// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.controllers' is found in controllers.js
angular.module('starter', ['ionic', 'starter.controllers', 'session.controllers', 'survey.controllers', 'final.controllers'])

    .run(function ($ionicPlatform) {
        $ionicPlatform.ready(function () {
            // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
            // for form inputs)
            if (window.cordova && window.cordova.plugins.Keyboard) {
                cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
                cordova.plugins.Keyboard.disableScroll(true);

            }
            if (window.StatusBar) {
                // org.apache.cordova.statusbar required
                StatusBar.styleDefault();
            }
        });
    })

    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider

            .state('app', {
                url: '/app',
                abstract: true,
                templateUrl: 'templates/menu.html',
                controller: 'AppCtrl'
            })

            .state('app.home', {
                url: '/home',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/home.html',
                        controller: 'HomeCtrl'
                    }
                }
            })

            .state('app.selectSession', {
                url: '/select-session',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/selectSession.html',
                        controller: 'SelectSessionCtrl'
                    }
                }
            })

            .state('app.selectTypeForSession', {
                url: '/session/:sessionType/versions',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/selectTypeForSession.html',
                        controller: 'SelectTypeForSessionCtrl'
                    }
                }
            })

            .state('app.sessionConfig', {
                url: '/config/session/:versionId',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/sessionConfig.html',
                        controller: 'SessionConfigCtrl'
                    }
                }
            })

            .state('app.session', {
                url: '/sessions/:sessionId/:versionId/:duration',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/session.html',
                        controller: 'SessionCtrl'
                    }
                }
            })

            .state('app.sessionFill', {
                url: '/fill/sessions/:sessionId/:versionId/:duration',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/sessionFill.html',
                        controller: 'SessionFillCtrl'
                    }
                }
            })

            .state('app.finished', {
                url: '/finished',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/finished.html',
                        controller: 'FinishedCtrl'
                    }
                }
            })

            .state('app.survey', {
                url: '/survey',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/survey.html',
                        controller: 'SurveyCtrl'
                    }
                }
            })

            .state('app.final', {
                url: '/final/:optionId',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/final.html',
                        controller: 'FinalCtrl'
                    }
                }
            });
        // if none of the above states are matched, use this as the fallback
        $urlRouterProvider.otherwise('/app/home');
    });
