'use strict';

describe('Controller Tests', function() {

    describe('StatusType Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockStatusType, MockProgram, MockOffer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockStatusType = jasmine.createSpy('MockStatusType');
            MockProgram = jasmine.createSpy('MockProgram');
            MockOffer = jasmine.createSpy('MockOffer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'StatusType': MockStatusType,
                'Program': MockProgram,
                'Offer': MockOffer
            };
            createController = function() {
                $injector.get('$controller')("StatusTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'myappApp:statusTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
