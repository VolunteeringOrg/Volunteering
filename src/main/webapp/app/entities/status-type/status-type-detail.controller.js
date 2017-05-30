(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('StatusTypeDetailController', StatusTypeDetailController);

    StatusTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StatusType'];

    function StatusTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, StatusType) {
        var vm = this;

        vm.statusType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myappApp:statusTypeUpdate', function(event, result) {
            vm.statusType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
