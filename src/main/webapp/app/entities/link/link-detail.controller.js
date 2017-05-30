(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('LinkDetailController', LinkDetailController);

    LinkDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Link', 'Provider', 'LinkType'];

    function LinkDetailController($scope, $rootScope, $stateParams, previousState, entity, Link, Provider, LinkType) {
        var vm = this;

        vm.link = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myappApp:linkUpdate', function(event, result) {
            vm.link = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
