(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('LinkTypeDialogController', LinkTypeDialogController);

    LinkTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LinkType'];

    function LinkTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LinkType) {
        var vm = this;

        vm.linkType = entity;
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
            if (vm.linkType.id !== null) {
                LinkType.update(vm.linkType, onSaveSuccess, onSaveError);
            } else {
                LinkType.save(vm.linkType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myappApp:linkTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
