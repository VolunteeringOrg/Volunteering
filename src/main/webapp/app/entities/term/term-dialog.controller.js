(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('TermDialogController', TermDialogController);

    TermDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Term', 'Offer'];

    function TermDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Term, Offer) {
        var vm = this;

        vm.term = entity;
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
            if (vm.term.id !== null) {
                Term.update(vm.term, onSaveSuccess, onSaveError);
            } else {
                Term.save(vm.term, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myappApp:termUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
