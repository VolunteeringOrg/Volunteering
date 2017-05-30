(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('OfferDetailController', OfferDetailController);

    OfferDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Offer', 'Program', 'StatusType', 'Term'];

    function OfferDetailController($scope, $rootScope, $stateParams, previousState, entity, Offer, Program, StatusType, Term) {
        var vm = this;

        vm.offer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myappApp:offerUpdate', function(event, result) {
            vm.offer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
