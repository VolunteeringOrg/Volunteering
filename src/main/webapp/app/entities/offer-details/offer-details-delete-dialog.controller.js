(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('OfferDetailsDeleteController',OfferDetailsDeleteController);

    OfferDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'OfferDetails'];

    function OfferDetailsDeleteController($uibModalInstance, entity, OfferDetails) {
        var vm = this;

        vm.offerDetails = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OfferDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
