(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('ProgramDialogController', ProgramDialogController);

    ProgramDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Program', 'Fk_provider_program', 'Provider', 'StatusType'];

    function ProgramDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Program, Fk_provider_program, Provider, StatusType) {
        var vm = this;

        vm.program = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.fk_provider_programs = Fk_provider_program.query();
        vm.providers = Provider.query();
        vm.statustypes = StatusType.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.program.id !== null) {
                Program.update(vm.program, onSaveSuccess, onSaveError);
            } else {
                Program.save(vm.program, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myappApp:programUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateTo = false;
        vm.datePickerOpenStatus.dateFrom = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
