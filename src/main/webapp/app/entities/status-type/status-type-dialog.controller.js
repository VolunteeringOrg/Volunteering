(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('StatusTypeDialogController', StatusTypeDialogController);

    StatusTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StatusType', 'Program', 'Offer'];

    function StatusTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StatusType, Program, Offer) {
        var vm = this;

        vm.statusType = entity;
        vm.clear = clear;
        vm.save = save;
        vm.programs = Program.query();
        vm.offers = Offer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.statusType.id !== null) {
                StatusType.update(vm.statusType, onSaveSuccess, onSaveError);
            } else {
                StatusType.save(vm.statusType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myappApp:statusTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
