'use strict';

describe('Controller Tests', function() {

    describe('Offer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOffer, MockStatusType, MockProgram, MockTerm, MockApplication, MockRequirement, MockOfferDetails, MockDocument;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOffer = jasmine.createSpy('MockOffer');
            MockStatusType = jasmine.createSpy('MockStatusType');
            MockProgram = jasmine.createSpy('MockProgram');
            MockTerm = jasmine.createSpy('MockTerm');
            MockApplication = jasmine.createSpy('MockApplication');
            MockRequirement = jasmine.createSpy('MockRequirement');
            MockOfferDetails = jasmine.createSpy('MockOfferDetails');
            MockDocument = jasmine.createSpy('MockDocument');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Offer': MockOffer,
                'StatusType': MockStatusType,
                'Program': MockProgram,
                'Term': MockTerm,
                'Application': MockApplication,
                'Requirement': MockRequirement,
                'OfferDetails': MockOfferDetails,
                'Document': MockDocument
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
