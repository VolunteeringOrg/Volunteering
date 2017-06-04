(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('OfferDialogController', OfferDialogController);

    OfferDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Offer', 'StatusType', 'Program', 'Term', 'Application', 'Requirement', 'OfferDetails', 'Document'];

    function OfferDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Offer, StatusType, Program, Term, Application, Requirement, OfferDetails, Document) {
        var vm = this;

        vm.offer = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.statustypes = StatusType.query();
        vm.programs = Program.query();
        vm.terms = Term.query();
        vm.applications = Application.query();
        vm.requirements = Requirement.query();
        vm.offerdetails = OfferDetails.query();
        vm.documents = Document.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.offer.id !== null) {
                Offer.update(vm.offer, onSaveSuccess, onSaveError);
            } else {
                Offer.save(vm.offer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myappApp:offerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateFrom = false;
        vm.datePickerOpenStatus.dateTo = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
