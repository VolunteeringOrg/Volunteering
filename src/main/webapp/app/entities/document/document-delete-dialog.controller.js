(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('DocumentDeleteController',DocumentDeleteController);

    DocumentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Document'];

    function DocumentDeleteController($uibModalInstance, entity, Document) {
        var vm = this;

        vm.document = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Document.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
