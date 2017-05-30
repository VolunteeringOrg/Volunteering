(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('LinkTypeDetailController', LinkTypeDetailController);

    LinkTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LinkType'];

    function LinkTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, LinkType) {
        var vm = this;

        vm.linkType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myappApp:linkTypeUpdate', function(event, result) {
            vm.linkType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
