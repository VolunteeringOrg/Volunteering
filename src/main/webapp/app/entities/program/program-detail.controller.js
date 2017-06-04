(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('ProgramDetailController', ProgramDetailController);

    ProgramDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Program', 'Provider', 'StatusType'];

    function ProgramDetailController($scope, $rootScope, $stateParams, previousState, entity, Program, Provider, StatusType) {
        var vm = this;

        vm.program = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myappApp:programUpdate', function(event, result) {
            vm.program = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
