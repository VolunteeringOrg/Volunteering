(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('DocumentDetailController', DocumentDetailController);

    DocumentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Document', 'Offer', 'User', 'Application'];

    function DocumentDetailController($scope, $rootScope, $stateParams, previousState, entity, Document, Offer, User, Application) {
        var vm = this;

        vm.document = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myappApp:documentUpdate', function(event, result) {
            vm.document = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
