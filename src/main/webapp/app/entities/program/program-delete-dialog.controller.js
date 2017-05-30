(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('ProgramDeleteController',ProgramDeleteController);

    ProgramDeleteController.$inject = ['$uibModalInstance', 'entity', 'Program'];

    function ProgramDeleteController($uibModalInstance, entity, Program) {
        var vm = this;

        vm.program = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Program.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
