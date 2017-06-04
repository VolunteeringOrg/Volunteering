(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('UserTypeDeleteController',UserTypeDeleteController);

    UserTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserType'];

    function UserTypeDeleteController($uibModalInstance, entity, UserType) {
        var vm = this;

        vm.userType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
