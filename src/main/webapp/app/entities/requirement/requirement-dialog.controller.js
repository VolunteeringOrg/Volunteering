(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('RequirementDialogController', RequirementDialogController);

    RequirementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Requirement', 'Offer'];

    function RequirementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Requirement, Offer) {
        var vm = this;

        vm.requirement = entity;
        vm.clear = clear;
        vm.save = save;
        vm.offers = Offer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.requirement.id !== null) {
                Requirement.update(vm.requirement, onSaveSuccess, onSaveError);
            } else {
                Requirement.save(vm.requirement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myappApp:requirementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
