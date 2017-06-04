(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('OfferDetailsDetailController', OfferDetailsDetailController);

    OfferDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OfferDetails', 'Offer'];

    function OfferDetailsDetailController($scope, $rootScope, $stateParams, previousState, entity, OfferDetails, Offer) {
        var vm = this;

        vm.offerDetails = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myappApp:offerDetailsUpdate', function(event, result) {
            vm.offerDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
