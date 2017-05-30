(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('AddressDeleteController',AddressDeleteController);

    AddressDeleteController.$inject = ['$uibModalInstance', 'entity', 'Address'];

    function AddressDeleteController($uibModalInstance, entity, Address) {
        var vm = this;

        vm.address = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Address.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
