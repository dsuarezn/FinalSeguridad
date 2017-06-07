'use strict';

describe('Controller: SecController', function () {

  // load the controller's module
  beforeEach(module('clientePlanificadorApp'));

  var SecController,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    SecController = $controller('SecController', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(SecController.awesomeThings.length).toBe(3);
  });
});
