(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('LinkDialogController', LinkDialogController);

    LinkDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Link', 'Provider', 'LinkType'];

    function LinkDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Link, Provider, LinkType) {
        var vm = this;

        vm.link = entity;
        vm.clear = clear;
        vm.save = save;
        vm.providers = Provider.query();
        vm.linktypes = LinkType.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.link.id !== null) {
                Link.update(vm.link, onSaveSuccess, onSaveError);
            } else {
                Link.save(vm.link, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myappApp:linkUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
