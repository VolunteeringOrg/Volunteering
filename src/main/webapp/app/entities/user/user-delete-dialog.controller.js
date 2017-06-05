(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('UserDeleteController',UserDeleteController);

    UserDeleteController.$inject = ['$uibModalInstance', 'entity', 'User'];

    function UserDeleteController($uibModalInstance, entity, User) {
        var vm = this;

        vm.user = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            User.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
