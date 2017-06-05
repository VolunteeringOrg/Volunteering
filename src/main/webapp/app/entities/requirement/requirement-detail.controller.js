(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('RequirementDetailController', RequirementDetailController);

    RequirementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Requirement', 'Offer'];

    function RequirementDetailController($scope, $rootScope, $stateParams, previousState, entity, Requirement, Offer) {
        var vm = this;

        vm.requirement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myappApp:requirementUpdate', function(event, result) {
            vm.requirement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
