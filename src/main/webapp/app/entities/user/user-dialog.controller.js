(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('UserDialogController', UserDialogController);

    UserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'User', 'Address', 'UserType', 'Application'];

    function UserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, User, Address, UserType, Application) {
        var vm = this;

        vm.user = entity;
        vm.clear = clear;
        vm.save = save;
        vm.addresses = Address.query();
        vm.usertypes = UserType.query();
        vm.applications = Application.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.user.id !== null) {
                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myappApp:userUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
