(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('LinkTypeDeleteController',LinkTypeDeleteController);

    LinkTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'LinkType'];

    function LinkTypeDeleteController($uibModalInstance, entity, LinkType) {
        var vm = this;

        vm.linkType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LinkType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
