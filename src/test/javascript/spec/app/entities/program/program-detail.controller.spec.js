'use strict';

describe('Controller Tests', function() {

    describe('Program Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProgram, MockFk_provider_program, MockProvider, MockStatusType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProgram = jasmine.createSpy('MockProgram');
            MockFk_provider_program = jasmine.createSpy('MockFk_provider_program');
            MockProvider = jasmine.createSpy('MockProvider');
            MockStatusType = jasmine.createSpy('MockStatusType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Program': MockProgram,
                'Fk_provider_program': MockFk_provider_program,
                'Provider': MockProvider,
                'StatusType': MockStatusType
            };
            createController = function() {
                $injector.get('$controller')("ProgramDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'myappApp:programUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
