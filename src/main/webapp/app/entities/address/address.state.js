(function() {
    'use strict';

    angular
        .module('myappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('address', {
            parent: 'entity',
            url: '/address?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myappApp.address.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/address/addresses.html',
                    controller: 'AddressController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('address');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('address-detail', {
            parent: 'address',
            url: '/address/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myappApp.address.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/address/address-detail.html',
                    controller: 'AddressDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('address');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Address', function($stateParams, Address) {
                    return Address.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'address',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('address-detail.edit', {
            parent: 'address-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/address/address-dialog.html',
                    controller: 'AddressDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Address', function(Address) {
                            return Address.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('address.new', {
            parent: 'address',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/address/address-dialog.html',
                    controller: 'AddressDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                email: null,
                                street: null,
                                city: null,
                                postalCode: null,
                                country: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('address', null, { reload: 'address' });
                }, function() {
                    $state.go('address');
                });
            }]
        })
        .state('address.edit', {
            parent: 'address',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/address/address-dialog.html',
                    controller: 'AddressDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Address', function(Address) {
                            return Address.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('address', null, { reload: 'address' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('address.delete', {
            parent: 'address',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/address/address-delete-dialog.html',
                    controller: 'AddressDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Address', function(Address) {
                            return Address.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('address', null, { reload: 'address' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
