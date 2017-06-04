(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('ApplicationDetailController', ApplicationDetailController);

    ApplicationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Application', 'Offer', 'User', 'Document'];

    function ApplicationDetailController($scope, $rootScope, $stateParams, previousState, entity, Application, Offer, User, Document) {
        var vm = this;

        vm.application = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myappApp:applicationUpdate', function(event, result) {
            vm.application = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
