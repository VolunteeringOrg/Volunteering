(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('ProviderDeleteController',ProviderDeleteController);

    ProviderDeleteController.$inject = ['$uibModalInstance', 'entity', 'Provider'];

    function ProviderDeleteController($uibModalInstance, entity, Provider) {
        var vm = this;

        vm.provider = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Provider.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
