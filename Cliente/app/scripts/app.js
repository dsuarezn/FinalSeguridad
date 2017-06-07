'use strict';

/**
 * @ngdoc overview
 * @name SeguridadApp
 * @description
 * # SeguridadApp
 *
 * Main module of the application.
 */
angular
  .module('SeguridadApp', [
    'ngAnimate',
    'ngResource',
    'ngRoute'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'SecController',
        controllerAs: 'main'
      })     
      .otherwise({
        redirectTo: '/'
      });
  });
