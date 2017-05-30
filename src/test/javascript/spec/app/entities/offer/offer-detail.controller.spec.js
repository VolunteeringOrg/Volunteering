'use strict';

describe('Controller Tests', function() {

    describe('Offer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOffer, MockProgram, MockStatusType, MockTerm;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOffer = jasmine.createSpy('MockOffer');
            MockProgram = jasmine.createSpy('MockProgram');
            MockStatusType = jasmine.createSpy('MockStatusType');
            MockTerm = jasmine.createSpy('MockTerm');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Offer': MockOffer,
                'Program': MockProgram,
                'StatusType': MockStatusType,
                'Term': MockTerm
            };
            createController = function() {
                $injector.get('$controller')("OfferDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'myappApp:offerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
