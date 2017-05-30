(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('LinkDeleteController',LinkDeleteController);

    LinkDeleteController.$inject = ['$uibModalInstance', 'entity', 'Link'];

    function LinkDeleteController($uibModalInstance, entity, Link) {
        var vm = this;

        vm.link = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Link.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
