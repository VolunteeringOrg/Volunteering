(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('OfferDetailsDialogController', OfferDetailsDialogController);

    OfferDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OfferDetails', 'Offer'];

    function OfferDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OfferDetails, Offer) {
        var vm = this;

        vm.offerDetails = entity;
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
            if (vm.offerDetails.id !== null) {
                OfferDetails.update(vm.offerDetails, onSaveSuccess, onSaveError);
            } else {
                OfferDetails.save(vm.offerDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myappApp:offerDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
