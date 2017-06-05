(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('StatusTypeDeleteController',StatusTypeDeleteController);

    StatusTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'StatusType'];

    function StatusTypeDeleteController($uibModalInstance, entity, StatusType) {
        var vm = this;

        vm.statusType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StatusType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
