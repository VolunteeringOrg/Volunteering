(function() {
    'use strict';

    angular
        .module('myappApp')
        .controller('DocumentDialogController', DocumentDialogController);

    DocumentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Document', 'Offer', 'User', 'Application'];

    function DocumentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Document, Offer, User, Application) {
        var vm = this;

        vm.document = entity;
        vm.clear = clear;
        vm.save = save;
        vm.offers = Offer.query();
        vm.users = User.query();
        vm.applications = Application.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.document.id !== null) {
                Document.update(vm.document, onSaveSuccess, onSaveError);
            } else {
                Document.save(vm.document, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myappApp:documentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
