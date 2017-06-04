(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('RequirementDeleteController',RequirementDeleteController);

    RequirementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Requirement'];

    function RequirementDeleteController($uibModalInstance, entity, Requirement) {
        var vm = this;

        vm.requirement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Requirement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
