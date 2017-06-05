(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('UserDetailController', UserDetailController);

    UserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'User', 'Address', 'UserType', 'Application'];

    function UserDetailController($scope, $rootScope, $stateParams, previousState, entity, User, Address, UserType, Application) {
        var vm = this;

        vm.user = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myappApp:userUpdate', function(event, result) {
            vm.user = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
