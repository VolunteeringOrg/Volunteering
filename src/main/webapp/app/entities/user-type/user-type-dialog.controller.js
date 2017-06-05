(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('UserTypeDialogController', UserTypeDialogController);

    UserTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserType'];

    function UserTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserType) {
        var vm = this;

        vm.userType = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userType.id !== null) {
                UserType.update(vm.userType, onSaveSuccess, onSaveError);
            } else {
                UserType.save(vm.userType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myappApp:userTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
