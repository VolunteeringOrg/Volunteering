(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('OfferDetailController', OfferDetailController);

    OfferDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Offer', 'StatusType', 'Program', 'Term', 'Application', 'Requirement', 'OfferDetails', 'Document'];

    function OfferDetailController($scope, $rootScope, $stateParams, previousState, entity, Offer, StatusType, Program, Term, Application, Requirement, OfferDetails, Document) {
        var vm = this;

        vm.offer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myappApp:offerUpdate', function(event, result) {
            vm.offer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
