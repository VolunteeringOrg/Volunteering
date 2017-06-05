(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('ProviderDialogController', ProviderDialogController);

    ProviderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Provider', 'Program', 'Link'];

    function ProviderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Provider, Program, Link) {
        var vm = this;

        vm.provider = entity;
        vm.clear = clear;
        vm.save = save;
        vm.programs = Program.query();
        vm.links = Link.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.provider.id !== null) {
                Provider.update(vm.provider, onSaveSuccess, onSaveError);
            } else {
                Provider.save(vm.provider, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myappApp:providerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
